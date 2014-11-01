package com.gmail.guyfleeman.vasolsim.tclient.element.form;

import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.common.struct.QuestionSet;
import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;
import static org.apache.commons.io.FilenameUtils.*;

/**
 * @author guyfleeman
 * @date 7/28/14
 * <p></p>
 */
public class QuestionSetInfoNode
{
	protected static Node questionSetNode;
	public static QuestionSet qSet = new QuestionSet();
	public static TreeElement boundTreeElement;

	static
	{
		updateNode();
	}

	public static void updateNode()
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label questionSetInfoLabel = new Label(questionSetInfoLabelText);
		questionSetInfoLabel.getStyleClass().add("lbltext");

		Label questionSetNameLabel = new Label(questionSetNameLabelText);
		questionSetNameLabel.getStyleClass().add("lbltext");

		final TextField questionSetNameField = new TextField();
		questionSetNameField.setPrefWidth(400);

		Button applyNameButton = new Button("Apply");

		HBox spacer = new HBox();
		spacer.setPrefHeight(2);
		spacer.setPrefWidth(2000);
		spacer.getStyleClass().add("lblspacer");

		Label resourceFileInfoLabel = new Label(resourceFileInfoLabelText);
		resourceFileInfoLabel.getStyleClass().add("lbltext");
		resourceFileInfoLabel.setWrapText(true);

		final Label resourceFileLabel = new Label("File: none");
		resourceFileLabel.getStyleClass().add("lbltext");
		resourceFileLabel.setWrapText(true);

		HBox buttonBox = new HBox();
		buttonBox.getStyleClass().add("helementspacing");

		Button loadResourceButton = new Button("Load");
		Button removeResourceButton = new Button("Remove");

		buttonBox.getChildren().addAll(loadResourceButton, removeResourceButton);

		/*
		 * add elements
		 */
		verticalRoot.getChildren().addAll(questionSetInfoLabel,
		                                  questionSetNameLabel,
		                                  questionSetNameField,
		                                  applyNameButton,
		                                  spacer,
		                                  resourceFileLabel,
		                                  buttonBox);

		/*
		 * Init listeners
		 */
		applyNameButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (questionSetNameField.getText() != null
						&& questionSetNameField.getText().trim().length() > 0)
				{
					boundTreeElement.label.setText(questionSetNameField.getText());
					questionSetNameField.clear();
				}
			}
		});

		loadResourceButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				FileChooser fc = new FileChooser();
				File resource = fc.showOpenDialog(TeacherClient.stage);
				String path;
				try
				{
					path = resource.getCanonicalPath();
				}
				catch (IOException e)
				{
					path = resource.getAbsolutePath();
				}

				try
				{
					qSet.loadResource(
							ResourceType.valueOf(getExtension(path).toUpperCase()),
							resource);

					resourceFileLabel.setText("File: " + path);
				}
				catch (IllegalArgumentException e)
				{
					PopupManager.showMessage(invalidFileTypeMessage + getExtension(path), invalidFileTypeTitle);
				}
			}
		});

		removeResourceButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				qSet.removeResource();
				resourceFileLabel.setText("File: none");
			}
		});

		questionSetNode = horizontalRoot;
	}

	public static Node getQuestionSetNode()
	{
		return questionSetNode;
	}
}
