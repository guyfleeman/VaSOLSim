package com.vasolsim.tclient;

import com.vasolsim.common.ExternalTask;
import com.vasolsim.common.GenericUtils;
import com.vasolsim.common.Preload;
import com.vasolsim.common.notification.DebugWindow;
import com.vasolsim.common.notification.PopupManager;
import com.vasolsim.tclient.core.BottomNode;
import com.vasolsim.tclient.core.CenterNode;
import com.vasolsim.tclient.core.LeftNode;
import com.vasolsim.tclient.core.MenuNode;
import com.vasolsim.tclient.form.*;
import com.vasolsim.tclient.tree.TreeElement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

/**
 * @author guyfleeman
 * @date 7/15/14
 * <p>Runner for the teacher client. Initialization done in Preload.</p>
 */
public class TeacherClient extends Application
{
	public static boolean showExtendedInfo = false;

	/*
	 * titles
	 */
	public static String preloadTitle = "VaSOLSim Teacher Client. Loading...";
	public static String title        = "Virginia Standards Of Learning Simulator (VaSOLSim) - Teacher Client";

	/*
	 * resource paths
	 */
	public static String rscPathRoot                = "/com/vasolsim/rsc/";
	public static String pathToStyle                = rscPathRoot + "style/tClientStyle.css";
	public static String pathToCommonStyle          = rscPathRoot + "style/commonStyle.css";
	public static String pathToBackgroundImage      = rscPathRoot + "img/background.png";
	public static String pathToExamsIcon            = rscPathRoot + "img/exams.png";
	public static String pathToExamIcon             = rscPathRoot + "img/exam.png";
	public static String pathToAddIcon              = rscPathRoot + "img/add.png";
	public static String pathToRemoveIcon           = rscPathRoot + "img/remove.png";
	public static String pathToQuestionSetIcon      = rscPathRoot + "img/set.png";
	public static String pathToQuestionIcon         = rscPathRoot + "img/question.png";
	public static String pathToCorrectAnswerIcon    = rscPathRoot + "img/answerCorrect.png";
	public static String pathToIncorrectAnswerIcon  = rscPathRoot + "img/answerIncorrect.png";
	public static String pathToMasterPropertiesFile = rscPathRoot + "persistence/teacherClient.prop";

	/*
	 * style
	 */
	public static String defaultStylesheets = "commonStyle.css tClientStyle.css";

	public static String treeButtonDefaultStyleClass = "btnDefault";
	public static String treeButtonCreateStyleClass  = "btnHoverCreate";
	public static String treeButtonDestroyStyleClass = "btnHoverDestroy";

	public static String stringPaneDefaultStyleClass              = "charPaneDefaultSuper";
	public static String stringPaneDefaultSubStyleClass           = "charPaneDefault";
	public static String stringPaneDefaultSubActiveStyleClass     = "charPaneDefaultActive";
	public static String stringPaneDefaultTextStyleClass          = "charPaneDefaultText";
	public static String stringPaneDefaultSmallTextStyleClass     = "charPaneSmallText";
	public static String stringPaneDefaultVerySmallTextStyleClass = "charPaneVerySmallText";

	/*
	 * high-level containers
	 */
	public static Stage stage;
	public static Scene primaryScene;
	public static Node topNode    = new HBox();
	public static Node leftNode   = new HBox();
	public static HBox centerNode = new HBox();
	public static Node rightNode  = new HBox();
	public static Node bottomNode = new HBox();
	public static TreeItem<TreeElement> examsRoot;

	/*
	 * globally accessible nodes
	 */
	public static ExamInitNode    examInitNode;
	public static ExamNode        examNode;
	public static QuestionSetNode questionSetNode;

	public static QuestionNode     questionNode;
	public static QuestionInitNode questionInitNode;
	public static QuestionTypeNode questionTypeNode;

	public static ExamExportNode examExportNode;

	/*
	 * grammar charset
	 */
	public static ArrayList<Character> charset              = new ArrayList<Character>();
	public static String               defaultCharsetString = ". , ? ! ; : -";// /u2013 (em dash) /u2014 (en dash)

	/*
	 * logging
	 */
	public static String logFormat = "%d{ISO8601} [%t] %-5p %c %x - %m%n";
	public static Logger teacherClientLogger;
	public static DebugWindow debugWindow = new DebugWindow(false);

	public TeacherClient() {}

	/**
	 * JavaFX entry call
	 * @param primaryStage stage
	 */
	public void start(final Stage primaryStage)
	{
		TeacherClient.stage = primaryStage;
		TeacherClient.stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent windowEvent)
			{
				System.out.println("TODO: save open data");
				Platform.exit();
			}
		});

		TeacherClient.primaryScene = new Scene(new VBox(), 960, 720);

		Preload.stage = TeacherClient.stage;
		Preload.preloadTitle = TeacherClient.preloadTitle;
		Preload.load(getInitRoutine(),
		             getOnSuccessRoutine(),
		             Preload.getDefaultOnFailHandler());

		System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
	}

	/**
	 * creates the initialization task for the preloader
	 * @return the initialization task
	 */
	public static ExternalTask<Void> getInitRoutine()
	{
		return new ExternalTask<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				try
				{
					updateMessage("Initializing core elements...");
					GenericUtils.pause();

					/*
					 * create top node, the menu bar
					 */
					TeacherClient.topNode = MenuNode.getMenuNode();

					/*
					 * create left node, will contain the tree
					 */
					TeacherClient.leftNode = LeftNode.getLeftNode();

					/*
					 * create the center node
					 */
					TeacherClient.centerNode = CenterNode.getCenterRoot();

					/*
					 * create the bottom node
					 */
					TeacherClient.bottomNode = BottomNode.getBottomNode();

					/*
					 * bind nodes to the scene root
					 */
					final BorderPane border = new BorderPane();
					border.setTop(TeacherClient.topNode);
					border.setLeft(TeacherClient.leftNode);
					border.setCenter(TeacherClient.centerNode);
					border.setRight(TeacherClient.rightNode);
					border.setBottom(TeacherClient.bottomNode);

					/*
					 * check for filesystem persistence (saved preferences and data)
					 */
					updateMessage("Checking for persistence...");
					GenericUtils.pause();

					File vasolsimFileRoot = new File(System.getProperty("user.home") + "/.vasolsim");
					File rsc              = new File(vasolsimFileRoot.getAbsolutePath() + "/rsc");
					File data             = new File(vasolsimFileRoot.getAbsolutePath() + "/data");
					File img              = new File(vasolsimFileRoot.getAbsolutePath() + "/img");

					File propFile         = new File(vasolsimFileRoot.getAbsolutePath() + "/teacherClient.prop");
					File imgFile          = new File(img.getAbsolutePath() + "/background.png");

					boolean canLoadPersistence = true;
					if (!(vasolsimFileRoot.isDirectory()
							&& rsc.isDirectory()
							&& data.isDirectory()
							&& img.isDirectory()
							&& propFile.isFile()
							&& imgFile.isFile()))
					{
						updateMessage("Persistence absent. Creating...");
						GenericUtils.pause();

						vasolsimFileRoot.mkdirs();
						rsc.mkdirs();
						data.mkdirs();
						img.mkdirs();

						/*
						 * failure to initialize required resources for persistence
						 */
						if (!(vasolsimFileRoot.isDirectory()
								&& rsc.isDirectory()
								&& data.isDirectory()
								&& img.isDirectory()))
						{
							updateMessage("Error creating persistence. Attempting to recover...");
							GenericUtils.pause();
							loadDefaultResources(this);
							canLoadPersistence = false;
						}
						else
						{
							try
							{
								if (!(new File(vasolsimFileRoot.getAbsolutePath() + "/teacherClient.prop").isFile()))
									GenericUtils.exportResource(pathToMasterPropertiesFile,
									                            vasolsimFileRoot.getAbsolutePath() +
											                            "/teacherClient.prop");

								if (!(new File(rsc.getAbsolutePath() + "/commonStyle.css").isFile()))
									GenericUtils.exportResource(pathToCommonStyle,
									                            rsc.getAbsolutePath() + "/commonStyle.css");

								if (!(new File(rsc.getAbsolutePath() + "/tClientStyle.css").isFile()))
									GenericUtils.exportResource(pathToStyle,
									                            rsc.getAbsolutePath() + "/tClientStyle.css");

								if (!(new File(img.getAbsolutePath() + "/background.png")).isFile())
									GenericUtils.exportResource(pathToBackgroundImage,
									                            img.getAbsolutePath() + "/background.png");
							}
							/*
							 * failure to copy master to visible or persistent resources
							 */
							catch (Exception e)
							{
								updateMessage("Error creating persistence. Attempting to recover...");
								GenericUtils.pause();
								loadDefaultResources(this);
								canLoadPersistence = false;
							}
						}
					}

					if (canLoadPersistence)
					{
						updateMessage("Persistence found. Checking data...");
						GenericUtils.pause();

						FileInputStream fis = new FileInputStream(propFile);
						Properties properties = new Properties();
						properties.load(fis);
						fis.close();

						for (String s : properties.getProperty("charset", defaultCharsetString).split(" "))
							TeacherClient.charset.add(s.charAt(0));
						TeacherClient.charset.add(' ');

						for (String s : properties.getProperty("stylesheets", defaultStylesheets).split(" "))
							TeacherClient.primaryScene.getStylesheets().add("file:///" + rsc.getAbsolutePath() + "/" + s);
					}

					/*
					 * initialize assets that require a FX thread for initialization.
					 */
					updateMessage("Scheduling visual initialization...");
					GenericUtils.pause();

					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							TeacherClient.examInitNode = new ExamInitNode();
							TeacherClient.examNode = new ExamNode();
							TeacherClient.questionSetNode = new QuestionSetNode();

							TeacherClient.questionNode = new QuestionNode();
							TeacherClient.questionInitNode = new QuestionInitNode();
							TeacherClient.questionTypeNode = new QuestionTypeNode();

							TeacherClient.examExportNode = new ExamExportNode();

							TeacherClient.primaryScene.setRoot(border);
						}
					});

					updateProgress(1, 1);
					updateMessage("Done.");

					GenericUtils.pause(1000);
				}
				catch (Exception e)
				{
					teacherClientLogger.fatal(GenericUtils.exceptionToString(e));
				}

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
				TeacherClient.stage.hide();
				GenericUtils.pause();

				/*
				 * reset stage
				 */
				TeacherClient.stage.setTitle(TeacherClient.title);
				TeacherClient.stage.setScene(TeacherClient.primaryScene);

				/*
				 * size stage
				 */
				Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
				TeacherClient.stage.setWidth(bounds.getWidth());
				TeacherClient.stage.setHeight(bounds.getHeight());

				/*
				 * show
				 */
				TeacherClient.stage.show();
			}
		};
	}

	/**
	 * loads default resources in the event the persistence creation fails
	 * @param task the task to be logged to
	 */
	public static void loadDefaultResources(ExternalTask task)
	{
		/*
		 * get default style
		 */
		task.updateMessage("Loading external style...");
		GenericUtils.pause();

		TeacherClient.primaryScene.getStylesheets().add(
				TeacherClient.class.getResource(TeacherClient.pathToStyle).toExternalForm());
		TeacherClient.primaryScene.getStylesheets().add(
				TeacherClient.class.getResource(TeacherClient.pathToCommonStyle).toExternalForm());

		/*
		 * get default charset
		 */
		task.updateMessage("Loading default charset...");
		for (String s : defaultCharsetString.split(" "))
			TeacherClient.charset.add(s.charAt(0));
		TeacherClient.charset.add(' ');
	}

	/**
	 * main
	 * @param args, program args: --disable-dep-check, --disable-fullspeed, --disable-caspian, <--debug | --trace>
	 */
	public static void main(String[] args)
	{
		if (!Arrays.asList(args).contains("--disable-dep-check"))
		{
			try
			{
				System.out.println("apache commons: " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/commons/lang3/exception/ExceptionUtils.class"));
				System.out.println("apache io:      " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/commons/io/FileUtils.class"));
				System.out.println("apache log4j:   " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/log4j/Logger.class"));
				System.out.println("apache pdfbox:  " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/pdfbox/pdmodel.PDDocument.class"));
				System.out.println("javamail:       " + TeacherClient.class.getClassLoader().getResources(
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
				new PatternLayout(logFormat), TeacherClient.debugWindow);

		debugWindowAppender.setThreshold(Level.INFO);
		if (Arrays.asList(args).contains("--debug"))
			debugWindowAppender.setThreshold(Level.DEBUG);
		if (Arrays.asList(args).contains("--trace"))
			debugWindowAppender.setThreshold(Level.TRACE);

		debugWindowAppender.activateOptions();
		Logger.getRootLogger().addAppender(debugWindowAppender);

		teacherClientLogger = Logger.getLogger(TeacherClient.class.getName());
		teacherClientLogger.setLevel(Level.ALL);

		teacherClientLogger.info("starting VSS teacher client");
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
			teacherClientLogger.fatal("unhandled root exception: " + GenericUtils.exceptionToString(e));
			PopupManager.showMessage("unhandled root exception: " + GenericUtils.exceptionToString(e));
		}
	}
}