package com.vasolsim.tclient.form;

import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.tclient.core.CenterNode;
import com.vasolsim.tclient.tree.QuestionSetTreeElement;
import com.vasolsim.tclient.tree.QuestionTreeElement;
import com.vasolsim.tclient.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.vasolsim.common.GenericUtils.*;
import static com.vasolsim.common.GenericUtils.createTreeItem;

/**
 * @author guyfleeman
 * @date 8/2/14 <p></p>
 */
public class QuestionInitNode implements DrawableNode
{
	protected Node                   questionInitNode;
	public QuestionSetTreeElement parent;

	public QuestionInitNode()
	{
		redrawNode(false);
	}

	public void redrawNode(boolean apply)
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label questionInitInfoLabel = new Label("Please enter the question text.");
		questionInitInfoLabel.getStyleClass().add("lbltext");
		questionInitInfoLabel.setWrapText(true);

		final TextArea questionTextArea = new TextArea();
		questionTextArea.setPrefWidth(2000);
		questionTextArea.setPrefHeight(280);
		questionTextArea.setWrapText(true);

		/*
		final HTMLEditor questionTextArea = new HTMLEditor();
		questionTextArea.setPrefWidth(2000);
		questionTextArea.setPrefHeight(280);
		*/

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
						newQuestion.label.setOverlay(
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
				newQuestion.question.setQuestion(questionTextArea.getText()/*getHtmlText()*/);
				newQuestion.parent = TeacherClient.questionInitNode.parent;
				newQuestion.label.setText(questionTypeToString(QuestionTypeNode.questionType));
				newQuestion.question.setQuestionType(QuestionTypeNode.questionType);
				newQuestion.question.initializeAnswers();

				TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
				                                               newQuestion,
				                                               TeacherClient.pathToQuestionIcon,
				                                               24);
				newQuestion.treeElementReference = element;
				newQuestion.parent = TeacherClient.questionInitNode.parent;
				TeacherClient.questionInitNode.parent.treeElementReference.getChildren().add(element);

				newQuestion.initListeners();
				TeacherClient.questionInitNode.parent.questions.add(newQuestion);

				CenterNode.removeScrollRoot();
			}
		});

		questionInitNode = verticalRoot;
	}

	public Node getNode()
	{
		return questionInitNode;
	}
}
