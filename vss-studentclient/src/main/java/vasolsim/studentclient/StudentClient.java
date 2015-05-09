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

package main.java.vasolsim.studentclient;

import main.java.vasolsim.common.ExternalTask;
import main.java.vasolsim.common.GenericUtils;
import main.java.vasolsim.common.Preloader;
import main.java.vasolsim.common.auth.DefaultLocalUserAuthenticator;
import main.java.vasolsim.common.auth.DefaultRemoteUserAuthenticator;
import main.java.vasolsim.common.auth.LocalUserAuthenticator;
import main.java.vasolsim.common.auth.RemoteUserAuthenticator;
import main.java.vasolsim.common.auth.VSSAuthToken;
import main.java.vasolsim.common.node.DrawableParent;
import main.java.vasolsim.common.support.notification.DebugWindow;
import main.java.vasolsim.common.support.notification.PopupManager;
import main.java.vasolsim.common.support.notification.remote.RemoteStandaloneNotifier;
import main.java.vasolsim.studentclient.core.LoginNode;

import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.vasolsim.studentclient.node.ExamSelectorNode;
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
	 * deploy flag (enables remote bug reporting)
	 */
	public static final boolean deploy = false;

	/*
	 * titles
	 */
	public static String preloadTitle = "VaSOLSim Student Client. Loading...";
	public static String title        = "Virginia Standards Of Learning Simulator (VaSOLSim) - Student Client";

	/*
	 * core
	 */
	public static final int intermediateStageHeight = 864;
	public static final int intermediateStageWidth  = 1152;
	public static Stage stage;
	public static Scene primaryScene;

	/*
	 * nodes
	 */
	public static DrawableParent loginNode;
	public static DrawableParent examSelectorNode;

	/*
	 * resources
	 */
	public static String   cssRoot = "/css/";
	public static String[] styles  = new String[]
			{
					cssRoot + "appglobal.css",
					cssRoot + "initcore.css",
					cssRoot + "login.css",
					cssRoot + "examselector.css"
			};

	/*
	 * logging
	 */
	public static String logFormat = "%d{ISO8601} [%t] %-5p %c %x - %m%n";
	public static Logger teacherClientLogger;
	public static DebugWindow              debugWindow    = new DebugWindow(false);
	public static RemoteStandaloneNotifier remoteNotifier = RemoteStandaloneNotifier.getInstance();

	/*
	 * auth
	 */
	public static LocalUserAuthenticator  localUserAuthenticator;
	public static RemoteUserAuthenticator remoteUserAuthenticator;
	public static VSSAuthToken            activeAuthorization;

	public static Rectangle2D screenSize;

	public StudentClient() {}

	public static void main(String[] args)
	{
		System.out.println("----- DEVELOPER NOTE -----\n" +
				                   "If you're actively developing VSS, please set deploy to false.\n" +
				                   "---------- DONE ----------");

		/*
		 * perform dependency link check, used to debug the addition of new libraries and fatJar creation
		 */
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

		/*
		 * initialize logging
		 */

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

		/*
		 * set system keys
		 */
		teacherClientLogger.info("starting VSS student client");
		teacherClientLogger.trace("init system properties");
		new JFXPanel();
		if (!Arrays.asList(args).contains("--disable-fullspeed"))
			System.setProperty("javafx.animation.fullspeed", Boolean.toString(true));
		if (!Arrays.asList(args).contains("--disable-caspian"))
			System.setProperty("javafx.userAgentStylesheetUrl", "caspian");
		teacherClientLogger.trace("launch");

		/*
		 * launch the application
		 */
		try
		{
			launch(args);
		}
		catch (Exception e)
		{
			teacherClientLogger.fatal("unhandled root exception:\n\n" + GenericUtils.exceptionToString(e));
			if (PopupManager.askYesNo("unhandled root exception:\n\n" + GenericUtils.exceptionToString(e) +
					                          "\n\nWould you like to send a bug report?"))
				if (deploy)
				{
					boolean success = remoteNotifier.sendRemoteNotification("Student Client - Root Exception",
					                                                        GenericUtils.exceptionToString(e));
					PopupManager.showMessage("The report " + (success ? "was successfully sent." : "failed to send."));
				}
				else
					PopupManager.showMessage("The report was not sent. Bug reports are only sent in deploy mode.");

			System.exit(-1);
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

		/*
		 * create preload thread
		 */
		Preloader.stage = StudentClient.stage;
		Preloader.preloadTitle = StudentClient.preloadTitle;
		Preloader.load(getInitRoutine(),
		               getOnSuccessRoutine(),
		               Preloader.getDefaultOnFailHandler());
	}

	/**
	 * task given to the preload thread, initializes all assets for the primary application. This will be handled in a
	 * non-JavaFX thread so blocking routines are ok.
	 * @return task to be submitted to the preloader
	 */
	public static ExternalTask<Void> getInitRoutine()
	{
		return new ExternalTask<Void>()
		{
			@Override
			protected Void call() throws Exception
			{

				/*
				 * authentication
				 */
				//TODO load by key
				StudentClient.localUserAuthenticator = DefaultLocalUserAuthenticator.getInstance();
				StudentClient.remoteUserAuthenticator = DefaultRemoteUserAuthenticator.getInstance();

				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{

						/*
						 * resource initialization
						 */
						for (String style : styles)
						{
							StudentClient.primaryScene.getStylesheets().add(
									StudentClient.class.getResource(style).toExternalForm());
							System.out.println(StudentClient.class.getResource(style).toExternalForm());
						}

						/*
						 * node initialization
						 */
						//TODO initialize fx nodes
						StudentClient.loginNode = new LoginNode();
						StudentClient.examSelectorNode = new ExamSelectorNode();


						/*
						 * set root
						 */
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
				 * maximize, listen for de-maximization for prevent the window from sticking off screen
				 */
				StudentClient.stage.setWidth(StudentClient.screenSize.getWidth());
				StudentClient.stage.setHeight(StudentClient.screenSize.getHeight());
				StudentClient.stage.setMaximized(true);
				stage.maximizedProperty().addListener(new ChangeListener<Boolean>()
				{
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
					{
						if (!newValue)
						{
							StudentClient.stage.setWidth(intermediateStageWidth);
							StudentClient.stage.setHeight(intermediateStageHeight);
						}
					}
				});

				/*
				 * show
				 */
				StudentClient.stage.show();
			}
		};
	}
}
