package com.vasolsim.common;

import com.sun.istack.internal.NotNull;
import com.vasolsim.common.notification.PopupManager;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author willstuckey
 * @date 11/7/14
 * <p>VaSOLSim preloader framework.</p>
 */
public class Preload
{
	public static String preloadTitle = "Title";
	public static String pathToPreloadImage = "/com/vasolsim/rsc/img/preload.png";
	public static Stage stage;

	protected static ProgressBar progressBar  = new ProgressBar();
	protected static Label       displayLabel = new Label("");

	/**
	 * This method will initialize a JavaFX program as specified by the Task loadRoutine.
	 * @param loadRoutine the initialization task. All operations in the task must run outside of a FX thread. Use
	 *                    Platform.runLater() to invoke FX thread dependent operations.
	 * //@param stage the stage
	 * @param onSuccess success handler
	 * @param onFail failure handler
	 */
	public static void load(@NotNull Task<Void> loadRoutine,
	                        @NotNull EventHandler<WorkerStateEvent> onSuccess,
	                        @NotNull EventHandler<WorkerStateEvent> onFail)
	{
		stage.setTitle(preloadTitle);
		stage.setScene(getPreloadScene());
		stage.show();

		Thread loadThread = new Thread(loadRoutine);
		loadThread.start();
		progressBar.progressProperty().bind(loadRoutine.progressProperty());
		displayLabel.textProperty().bind(loadRoutine.messageProperty());

		loadRoutine.setOnSucceeded(onSuccess);
		loadRoutine.setOnFailed(onFail);
	}

	/**
	 * creates the preload scene
	 * @return scene
	 */
	protected static Scene getPreloadScene()
	{
		StackPane preloadRoot = new StackPane();

		ImageView view = new ImageView(new Image(
				Preload.class.getResource(Preload.pathToPreloadImage).toExternalForm()));
		view.setFitWidth(800);
		view.setFitHeight(480);
		view.setPreserveRatio(false);

		BorderPane preLoadBorder = new BorderPane();
		preLoadBorder.setPrefWidth(2000);

		displayLabel.setStyle("-fx-font-size: 16; -fx-text-fill: rgb(255, 255, 255); -fx-font-weight: bold;");
		progressBar.setPrefWidth(2000);
		progressBar.setPrefHeight(20);
		VBox bottom = new VBox();
		bottom.getChildren().addAll(displayLabel, progressBar);
		preLoadBorder.setBottom(bottom);

		preloadRoot.getChildren().addAll(view, preLoadBorder);

		return new Scene(preloadRoot, 800, 480);
	}

	/**
	 * Creates the default handler for preload failures.
	 * @return the event handler
	 */
	public static EventHandler<WorkerStateEvent> getDefaultOnFailHandler()
	{
		return new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent workerStateEvent)
			{
				PopupManager.showMessage("A non-recoverable initialization error has occurred.");
				System.exit(-1);
			}
		};
	}

	/**
	 * gets the stage
	 * @return stage
	 */
	public static Stage getStage()
	{
		return stage;
	}
}
