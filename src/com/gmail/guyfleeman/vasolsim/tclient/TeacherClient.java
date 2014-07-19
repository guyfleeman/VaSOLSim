package com.gmail.guyfleeman.vasolsim.tclient;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
 * @date 7/15/14
 * <p></p>
 */
public class TeacherClient extends Application
{
	protected static String title           = "Virginia Standards Of Learning Simulator (VaSOLSim) - Teacher Client";
	protected static String rscPathRoot     = "/com/gmail/guyfleeman/vasolsim/rsc/";
	protected static String pathToStyle     = rscPathRoot + "style/tClientStyle.css";
	protected static String pathToExamsIcon = rscPathRoot + "img/exams.png";
	protected static String pathToExamIcon  = rscPathRoot + "img/exam.png";

	@SuppressWarnings("all")
	private static Stage stage;
	@SuppressWarnings("all")
	private static Scene scene;
	@SuppressWarnings("all")
	private static Node topNode    = new VBox();
	@SuppressWarnings("all")
	private static Node leftNode   = new HBox();
	@SuppressWarnings("all")
	private static Node centerNode = new HBox();
	@SuppressWarnings("all")
	private static Node rightNode  = new HBox();
	@SuppressWarnings("all")
	private static Node bottomNode = new VBox();
	@SuppressWarnings("all")
	private static TreeItem<String> examsRoot;

	@Override
	public void start(Stage primaryStage)
	{
		stage = primaryStage;

		topNode = createTopNode();
		leftNode = createLeftNode();
		centerNode = createCenterNode();

		BorderPane border = new BorderPane();
		//border.getStyleClass().add("borders");
		border.setTop(topNode);
		border.setLeft(leftNode);
		border.setCenter(centerNode);
		border.setRight(rightNode);
		border.setBottom(bottomNode);

		Scene scene = new Scene(border, 1000, 800);
		TeacherClient.scene = scene;
		scene.getStylesheets().add(TeacherClient.class.getResource(pathToStyle).toExternalForm());
		System.out.println(TeacherClient.class.getResource(pathToStyle).toExternalForm());
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
	public static Node createLeftNode()
	{
		HBox leftHorizRoot = new HBox();
		leftHorizRoot.getStyleClass().add("borders");

		VBox leftVertRoot = new VBox();
		leftVertRoot.getStyleClass().add("leftvbox");
		leftVertRoot.setMinWidth(320);
		leftHorizRoot.getChildren().add(leftVertRoot);

		examsRoot = createTreeItem(TeacherClient.class, "Exams", pathToExamsIcon, 24);
		examsRoot.getChildren().add(createTreeItem(TeacherClient.class, "Exam" + index++, pathToExamIcon, 24));
		leftVertRoot.getChildren().add(new TreeView<String>(examsRoot));

		Button b = new Button();
		b.setText("Button");
		b.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println("EVENT!");
				examsRoot.getChildren().add(createTreeItem(TeacherClient.class, "Exam" + index++, pathToExamIcon, 24));
			}
		});
		leftVertRoot.getChildren().add(b);

		return leftHorizRoot;
	}

	public static Node createCenterNode()
	{
		HBox centerHorizRoot = new HBox();
		centerHorizRoot.getStyleClass().add("borders");

		VBox centerVertRoot = new VBox();
		centerVertRoot.getStyleClass().add("centervbox");
		centerVertRoot.setMinWidth(500);
		centerHorizRoot.getChildren().add(centerVertRoot);

		return centerHorizRoot;
	}

	public static void main(String[] args)
	{
	 	launch(args);
	}
}
