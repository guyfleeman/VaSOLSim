package com.vasolsim.tclient;

import com.vasolsim.common.ExternalTask;
import com.vasolsim.common.GenericUtils;
import com.vasolsim.common.Preload;
import com.vasolsim.tclient.element.core.BottomNode;
import com.vasolsim.tclient.element.core.CenterNode;
import com.vasolsim.tclient.element.core.LeftNode;
import com.vasolsim.tclient.element.core.MenuNode;
import com.vasolsim.tclient.element.form.*;
import com.vasolsim.tclient.element.tree.TreeElement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

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
	public static String rscPathRoot               = "/com/vasolsim/rsc/";
	public static String pathToStyle               = rscPathRoot + "style/tClientStyle.css";
	public static String pathToCommonStyle         = rscPathRoot + "style/commonStyle.css";
	public static String pathToBackgroundImage     = rscPathRoot + "img/background2.jpg";
	public static String pathToExamsIcon           = rscPathRoot + "img/exams.png";
	public static String pathToExamIcon            = rscPathRoot + "img/exam.png";
	public static String pathToAddIcon             = rscPathRoot + "img/add.png";
	public static String pathToRemoveIcon          = rscPathRoot + "img/remove.png";
	public static String pathToQuestionSetIcon     = rscPathRoot + "img/set.png";
	public static String pathToQuestionIcon        = rscPathRoot + "img/question.png";
	public static String pathToCorrectAnswerIcon   = rscPathRoot + "img/answerCorrect.png";
	public static String pathToIncorrectAnswerIcon = rscPathRoot + "img/answerIncorrect.png";
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

	/*
	 * grammar charset
	 */
	public static ArrayList<Character> charset = new ArrayList<Character>();
	public static String defaultCharsetString = ". , ? ! ; : - \u2013 \u2014";

	/**
	 * JavaFX entry call
	 * @param primaryStage stage
	 */
	public void start(final Stage primaryStage)
	{
		stage = primaryStage;
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
					BorderPane border = new BorderPane();
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
					File propFile         = new File(vasolsimFileRoot.getAbsolutePath() + "/teacherClient.prop");
					File rsc              = new File(vasolsimFileRoot.getAbsolutePath() + "/rsc");
					File data             = new File(vasolsimFileRoot.getAbsolutePath() + "/data");
					File img              = new File(vasolsimFileRoot.getAbsolutePath() + "/img");

					boolean canLoadPersistence = true;
					if (!(vasolsimFileRoot.isDirectory()
							&& propFile.isFile()
							&& rsc.isDirectory()
							&& data.isDirectory()
							&& img.isDirectory()))
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

								if (!(new File(img.getAbsolutePath() + "/background2.jpg")).isFile())
									GenericUtils.exportResource(pathToBackgroundImage,
									                            img.getAbsolutePath() + "/background2.jpg");
							}
							/*
							 * failure to copy master to visible or persistent resources
							 */
							catch (Exception e)
							{
								updateMessage("Error creating persistence. Attempting to recover...");
								GenericUtils.pause();
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
						}
					});

					TeacherClient.primaryScene.setRoot(border);

					updateProgress(1, 1);
					updateMessage("Done.");

					GenericUtils.pause(1000);
				}
				catch (Exception e)
				{
					System.out.println(e.getCause());
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
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				TeacherClient.stage.setWidth(screenSize.getWidth());
				TeacherClient.stage.setHeight(screenSize.getHeight());

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
	 * @param args
	 */
	public static void main(String[] args)
	{
		new JFXPanel();
		System.setProperty("javafx.animation.fullspeed", Boolean.toString(true));
		System.setProperty("javafx.userAgentStylesheetUrl", "caspian");
		launch(args);
	}
}