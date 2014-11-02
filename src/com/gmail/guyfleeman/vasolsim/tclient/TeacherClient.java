package com.gmail.guyfleeman.vasolsim.tclient;

import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.tclient.element.ImageButton;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.ExamInfoNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.ExamStatsInfoNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.QuestionSetInfoNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.ExamTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.ExamsTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;


/**
 * @author guyfleeman
 * @date 7/15/14 <p></p>
 */
public class TeacherClient extends Application
{
	public static boolean showExtendedInfo = false;

	public static String title                     = "Virginia Standards Of Learning Simulator (VaSOLSim) - Teacher " +
			"Client";
	public static String rscPathRoot               = "/com/gmail/guyfleeman/vasolsim/rsc/";
	public static String pathToStyle               = rscPathRoot + "style/tClientStyle.css";
	public static String pathToExamsIcon           = rscPathRoot + "img/exams.png";
	public static String pathToExamIcon            = rscPathRoot + "img/exam.png";
	public static String pathToAddIcon             = rscPathRoot + "img/add.png";
	public static String pathToRemoveIcon          = rscPathRoot + "img/remove.png";
	public static String pathToQuestionSetIcon     = rscPathRoot + "img/set.png";
	public static String pathToQuestionIcon        = rscPathRoot + "img/question.png";
	public static String pathToCorrectAnswerIcon   = rscPathRoot + "img/answerCorrect.png";
	public static String pathToIncorrectAnswerIcon = rscPathRoot + "img/answerIncorrect.png";

	@SuppressWarnings("all")
	public static Stage stage;
	@SuppressWarnings("all")
	public static Scene scene;
	@SuppressWarnings("all")
	public static Node topNode    = new VBox();
	@SuppressWarnings("all")
	public static Node leftNode   = new HBox();
	@SuppressWarnings("all")
	public static HBox centerNode = new HBox();
	@SuppressWarnings("all")
	public static Node rightNode  = new HBox();
	@SuppressWarnings("all")
	public static Node bottomNode = new VBox();
	@SuppressWarnings("all")
	public static TreeItem<TreeElement> examsRoot;

	public static ExamStatsInfoNode examStatsInfoNode;
	public static ExamInfoNode examInfoNode;
	public static QuestionSetInfoNode questionSetInfoNode;

	public void start(Stage primaryStage)
	{
		stage = primaryStage;

		topNode = createTopNode();
		leftNode = createLeftNode();
		centerNode = CenterNode.getCenterRoot();


		BorderPane border = new BorderPane();
		//border.getStyleClass().add("borders");
		border.setTop(topNode);
		border.setLeft(leftNode);
		border.setCenter(centerNode);
		border.setRight(rightNode);
		border.setBottom(bottomNode);

		Scene scene = new Scene(border, 960, 720);
		TeacherClient.scene = scene;
		scene.getStylesheets().add(TeacherClient.class.getResource(pathToStyle).toExternalForm());
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static Node createTopNode()
	{
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		Menu helpMenu = new Menu("Help");
		menuBar.getMenus().addAll(fileMenu, helpMenu);
		return menuBar;
	}

	private static int index = 1;

	public static HBox createLeftNode()
	{
		HBox leftHorizRoot = new HBox();
		leftHorizRoot.getStyleClass().add("borders");

		VBox leftVertRoot = new VBox();
		leftVertRoot.getStyleClass().add("leftvbox");
		leftVertRoot.setMinWidth(320);
		leftHorizRoot.getChildren().add(leftVertRoot);

		ExamsTreeElement exams = new ExamsTreeElement();

		examsRoot = createTreeItem(TeacherClient.class, exams, pathToExamsIcon, 24);
		TreeView<TreeElement> view = new TreeView<TreeElement>(examsRoot);
		leftVertRoot.getChildren().add(view);

		return leftHorizRoot;
	}

	public static void initializeStaticAssets()
	{
		examStatsInfoNode = new ExamStatsInfoNode();
		examInfoNode = new ExamInfoNode();
		questionSetInfoNode = new QuestionSetInfoNode();
	}

	public static void main(String[] args)
	{
		initializeStaticAssets();
		launch(args);
	}
}