/*
 * Copyright (c) 2015.
 *
 *     This file is part of VaSOLSim.
 *
 *     VaSOLSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     VaSOLSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with VaSOLSim.  If not, see <http://www.gnu.org/licenses/>.
 */

package main.java.vasolsim.teacherclient.form;

import main.java.vasolsim.common.node.DrawableNode;
import main.java.vasolsim.teacherclient.TeacherClient;
import main.java.vasolsim.teacherclient.core.CenterNode;
import main.java.vasolsim.teacherclient.tree.QuestionSetTreeElement;
import main.java.vasolsim.teacherclient.tree.QuestionTreeElement;
import main.java.vasolsim.common.node.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static main.java.vasolsim.common.GenericUtils.*;
import static main.java.vasolsim.common.GenericUtils.createTreeItem;

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

		Button continueButton = new Button(TeacherClient.CONTINUE_BUTTON_TEXT);

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
