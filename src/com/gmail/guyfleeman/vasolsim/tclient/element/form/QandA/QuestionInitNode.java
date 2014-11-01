package com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA;

import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.QuestionSetTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.QuestionTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;
import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.createTreeItem;

/**
 * @author guyfleeman
 * @date 8/2/14 <p></p>
 */
public class QuestionInitNode
{
	protected static Node                   questionInitNode;
	public static    QuestionSetTreeElement parent;

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

		Label questionInitInfoLabel = new Label("Please enter the QandA or statement.");
		questionInitInfoLabel.getStyleClass().add("lbltext");
		questionInitInfoLabel.setWrapText(true);

		TextArea questionTextArea = new TextArea();
		questionTextArea.setPrefWidth(2000);
		questionTextArea.setPrefHeight(400);
		questionTextArea.setWrapText(true);

		Button continueButton = new Button(continueButtonText);

		verticalRoot.getChildren().addAll(questionInitInfoLabel, questionTextArea, continueButton);

		/*
		 * Init listeners
		 */
		continueButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();

				/*
				switch (QuestionTypeNode.questionType)
				{
					case TE_D_AND_D_VENN_DIAGRAM: //TODO break;
					case TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE:
					{
						CenterNode.getScrollRoot().setContent(
								GrammarSymbolInitNode.grammarQuestionSymbolInitNode);
						break;
					}
					default:
					{
						QuestionTreeElement newQuestion = new QuestionTreeElement();
						newQuestion.parent = QuestionInitNode.parent;
						newQuestion.label.setText(
								"New " + QuestionTypeNode.questionType.toString().replaceAll("_", " ").toLowerCase());
						newQuestion.question.setQuestionType(QuestionTypeNode.questionType);

						TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
						                                               newQuestion,
						                                               TeacherClient.pathToQuestionIcon,
						                                               24);
						newQuestion.treeElementReference = element;
						newQuestion.parent = QuestionInitNode.parent;
						QuestionInitNode.parent.treeElementReference.getChildren().add(element);

						newQuestion.initListeners();
						QuestionInitNode.parent.questions.add(newQuestion);

						CenterNode.removeScrollRoot();
					}

				}
				*/

				QuestionTreeElement newQuestion = new QuestionTreeElement();
				newQuestion.parent = QuestionInitNode.parent;
				newQuestion.label.setText(
						"New " + QuestionTypeNode.questionType.toString().replaceAll("_", " ").toLowerCase());
				newQuestion.question.setQuestionType(QuestionTypeNode.questionType);

				TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
				                                               newQuestion,
				                                               TeacherClient.pathToQuestionIcon,
				                                               24);
				newQuestion.treeElementReference = element;
				newQuestion.parent = QuestionInitNode.parent;
				QuestionInitNode.parent.treeElementReference.getChildren().add(element);

				newQuestion.initListeners();
				QuestionInitNode.parent.questions.add(newQuestion);

				CenterNode.removeScrollRoot();
			}
		});

		questionInitNode = verticalRoot;
	}

	public static Node getQuestionInitNode()
	{
		return questionInitNode;
	}
}
