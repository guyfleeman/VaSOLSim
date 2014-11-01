package com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * @author guyfleeman
 * @date 8/5/14 <p></p>
 */
public class MultipleResponseAnswersInitNode
{
	public static Node multipleResponseAnswersInitNode;

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

		ArrayList<HBox> questionNodes = new ArrayList<HBox>();
		for (int index = 0; index < 8; index++)
		{
			HBox answerRoot = new HBox();
			answerRoot.setPrefWidth(2000);
			answerRoot.setPrefHeight(300);

			CheckBox usingAnswer = new CheckBox();
			usingAnswer.setIndeterminate(false);

			Label countLabel = new Label((index + 1) +  ".");
			countLabel.getStyleClass().add("lbltextsub");

			TextField idField = new TextField();
			idField.setPrefWidth(26);

			TextArea questionArea = new TextArea();
			questionArea.setWrapText(true);
			questionArea.setPrefHeight(150);
			questionArea.setPrefWidth(2000);

			CheckBox isCorrectAnswer = new CheckBox();
			isCorrectAnswer.setIndeterminate(false);

			questionNodes.add(answerRoot);
		}

	}
}
