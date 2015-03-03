/*
 * Copyright (c) 2015.
 *
 *     This file is part of VaSOLSim.
 *
 *     VaSOLSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     VaSOLSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with VaSOLSim.  If not, see <http://www.gnu.org/licenses/>.
 */

package main.java.vasolsim.sclient;

import javafx.scene.layout.VBox;
import main.java.vasolsim.common.ExternalTask;
import main.java.vasolsim.common.GenericUtils;
import main.java.vasolsim.common.Preload;
import main.java.vasolsim.common.node.DrawableParent;
import main.java.vasolsim.common.notification.DebugWindow;
import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.sclient.core.LoginNode;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

/**
 * Runner class for student user interface.
 * @author optimalpandemic, guyfleeman
 * @date 11/3/14 <p></p>
 */
public class StudentClient extends Application
{
	/*
	 * titles
	 */
	public static String preloadTitle = "VaSOLSim Student Client. Loading...";
	public static String title        = "Virginia Standards Of Learning Simulator (VaSOLSim) - Student Client";


	public static Stage          stage;
	public static Scene          primaryScene;
	public static DrawableParent loginNode;

	/*
	 * resources
	 */
	public static String cssRoot = "/css/";
	public static String appStyle = cssRoot + "appglobal.css";
	public static String loginStyle = cssRoot + "login.css";

	/*
	 * logging
	 */
	public static String logFormat = "%d{ISO8601} [%t] %-5p %c %x - %m%n";
	public static Logger teacherClientLogger;
	public static DebugWindow debugWindow = new DebugWindow(false);

	public static Rectangle2D screenSize;

	public StudentClient() {}

	public static void main(String[] args)
	{
		if (!Arrays.asList(args).contains("--disable-dep-check"))
		{
			try
			{
				System.out.println("apache commons: " + StudentClient.class.getClassLoader().getResources(
						"org/apache/commons/lang3/exception/ExceptionUtils.class"));
				System.out.println("apache io:      " + StudentClient.class.getClassLoader().getResources(
						"org/apache/commons/io/FileUtils.class"));
				System.out.println("apache log4j:   " + StudentClient.class.getClassLoader().getResources(
						"org/apache/log4j/Logger.class"));
				System.out.println("apache pdfbox:  " + StudentClient.class.getClassLoader().getResources(
						"org/apache/pdfbox/pdmodel.PDDocument.class"));
				System.out.println("javamail:       " + StudentClient.class.getClassLoader().getResources(
						"javax/mail/Version.class"));
			}
			catch (Exception e)
			{
				System.out.println("error enumerating dependencies");
				throw new RuntimeException("error enumerating dependencies");
			}
		}
		else
		{
			System.out.println("skipped dependency link check");
		}

		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout(logFormat));
		console.setThreshold(Level.TRACE);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);

		WriterAppender debugWindowAppender = new WriterAppender(
				new PatternLayout(logFormat), StudentClient.debugWindow);

		debugWindowAppender.setThreshold(Level.INFO);
		if (Arrays.asList(args).contains("--debug"))
			debugWindowAppender.setThreshold(Level.DEBUG);
		if (Arrays.asList(args).contains("--trace"))
			debugWindowAppender.setThreshold(Level.TRACE);

		debugWindowAppender.activateOptions();
		Logger.getRootLogger().addAppender(debugWindowAppender);

		teacherClientLogger = Logger.getLogger(StudentClient.class.getName());
		teacherClientLogger.setLevel(Level.ALL);

		teacherClientLogger.info("starting VSS student client");
		teacherClientLogger.trace("init system properties");
		new JFXPanel();
		if (!Arrays.asList(args).contains("--disable-fullspeed"))
			System.setProperty("javafx.animation.fullspeed", Boolean.toString(true));
		if (!Arrays.asList(args).contains("--disable-caspian"))
			System.setProperty("javafx.userAgentStylesheetUrl", "caspian");
		teacherClientLogger.trace("launch");

		try
		{
			launch(args);
		}
		catch (Exception e)
		{
			teacherClientLogger.fatal("unhandled root exception:\n\n" + GenericUtils.exceptionToString(e));
			PopupManager.showMessage("unhandled root exception:\n\n" + GenericUtils.exceptionToString(e));
		}
	}

	@Override
	public void start(final Stage primaryStage)
	{
		/*
		 * primary stage and scene initialization
		 */
		StudentClient.stage = primaryStage;
		StudentClient.stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent windowEvent)
			{
				System.out.println("TODO: save open data");
				Platform.exit();
			}
		});
		StudentClient.primaryScene = new Scene(new VBox(), 960, 720);

		/*
		 * size stage
		 */
		StudentClient.screenSize = Screen.getPrimary().getVisualBounds();
		StudentClient.stage.setWidth(StudentClient.screenSize.getWidth());
		StudentClient.stage.setHeight(StudentClient.screenSize.getHeight());

		/*
		 * create preload thread
		 */
		Preload.stage = StudentClient.stage;
		Preload.preloadTitle = StudentClient.preloadTitle;
		Preload.load(getInitRoutine(),
		             getOnSuccessRoutine(),
		             Preload.getDefaultOnFailHandler());
	}

	public static ExternalTask<Void> getInitRoutine()
	{
		return new ExternalTask<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{

						/*
						 * resource initialization
						 */
						StudentClient.primaryScene.getStylesheets().addAll(
								StudentClient.class.getResource(StudentClient.loginStyle).toExternalForm(),
								StudentClient.class.getResource(StudentClient.appStyle).toExternalForm());

						//TODO initialize fx nodes
						loginNode = new LoginNode();

						StudentClient.primaryScene.setRoot(StudentClient.loginNode.getParent());
					}
				});

				return null;
			}
		};
	}

	/**
	 * creates the on success EventHandler for the preloader
	 * @return event handler
	 */
	public static EventHandler<WorkerStateEvent> getOnSuccessRoutine()
	{
		return new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent workerStateEvent)
			{
				/*
				 * hide and brief pause
				 */
				StudentClient.stage.hide();
				GenericUtils.pause();

				/*
				 * reset stage
				 */
				StudentClient.stage.setTitle(StudentClient.title);
				StudentClient.stage.setScene(StudentClient.primaryScene);

				/*
				 * show
				 */
				StudentClient.stage.show();
			}
		};
	}
}
