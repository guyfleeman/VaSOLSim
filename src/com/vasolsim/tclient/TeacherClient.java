package com.vasolsim.tclient;

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
import javafx.concurrent.Task;
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
import java.util.ArrayList;

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
	public static String pathToExamsIcon           = rscPathRoot + "img/exams.png";
	public static String pathToExamIcon            = rscPathRoot + "img/exam.png";
	public static String pathToAddIcon             = rscPathRoot + "img/add.png";
	public static String pathToRemoveIcon          = rscPathRoot + "img/remove.png";
	public static String pathToQuestionSetIcon     = rscPathRoot + "img/set.png";
	public static String pathToQuestionIcon        = rscPathRoot + "img/question.png";
	public static String pathToCorrectAnswerIcon   = rscPathRoot + "img/answerCorrect.png";
	public static String pathToIncorrectAnswerIcon = rscPathRoot + "img/answerIncorrect.png";

	/*
	 * style classes
	 */
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

	public void start(final Stage primaryStage)
	{
		stage = primaryStage;
		TeacherClient.primaryScene = new Scene(new VBox(), 960, 720);

		Preload.preloadTitle = TeacherClient.preloadTitle;
		Preload.load(getInitRoutine(),
		             getOnSuccessRoutine(),
		             Preload.getDefaultOnFailHandler());

		System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
	}

	public static Task<Void> getInitRoutine()
	{
		return new Task<Void>()
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

					System.out.println(System.getProperty("user.home"));
					File vasolsimFileRoot = new File(System.getProperty("user.home") + "/.vasolsim");
					if (vasolsimFileRoot.isFile())
					{
						updateMessage("Persistence found. Checking data...");
						GenericUtils.pause();
					}
					else
					{
						updateMessage("Persistence absent. Creating...");
						GenericUtils.pause();


					}

					/*
					 * get style specified by persistence
					 */
					updateMessage("Loading external style...");
					GenericUtils.pause();

					TeacherClient.primaryScene.getStylesheets().add(
							TeacherClient.class.getResource(TeacherClient.pathToStyle).toExternalForm());
					TeacherClient.primaryScene.getStylesheets().add(
							TeacherClient.class.getResource(TeacherClient.pathToCommonStyle).toExternalForm());

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
				Preload.getStage().hide();

				/*
				 * reset stage
				 */
				TeacherClient.stage.setTitle(TeacherClient.title);
				TeacherClient.stage.setScene(TeacherClient.primaryScene);
				TeacherClient.stage.initStyle(StageStyle.DECORATED);

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

	public static void main(String[] args)
	{
		new JFXPanel();
		System.setProperty("javafx.animation.fullspeed", Boolean.toString(true));
		System.setProperty("javafx.userAgentStylesheetUrl", "caspian");
		launch(args);
	}
}