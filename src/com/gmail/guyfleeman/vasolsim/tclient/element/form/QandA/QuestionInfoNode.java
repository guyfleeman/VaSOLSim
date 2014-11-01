package com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA;

import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.common.struct.Question;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.AnswerTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.QuestionTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Iterator;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 8/7/14 <p></p>
 */
public class QuestionInfoNode
{
	protected static Node questionInfoNode;
	public static QuestionTreeElement boundTreeElement;

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

		Label infoLabel = new Label("Question Information Overview");
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		HBox spacer = new HBox();
		spacer.setPrefWidth(2000);
		spacer.getStyleClass().add("rectspacer");

		if (boundTreeElement != null
				&& boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE)
		{
			HBox symbolButtonRoot = new HBox();
			symbolButtonRoot.getStyleClass().add("helementspacing");
			Button genAnswerSet = new Button("Generate Symbol Set");
			Button delAnswerSet = new Button("Delete Current Symbol Set");
			symbolButtonRoot.getChildren().addAll(genAnswerSet, delAnswerSet);

			HBox answerButtonRoot = new HBox();
			answerButtonRoot.getStyleClass().add("helementspacing");
			Button genAnswer = new Button("Generate Visual Answer");
			Button delAnswer = new Button("Delete Visual Answer");
			answerButtonRoot.getChildren().addAll(genAnswer, delAnswer);

			verticalRoot.getChildren().addAll(infoLabel,
			                                  spacer,
			                                  symbolButtonRoot,
			                                  answerButtonRoot);

			/*
		     * Init listeners
		     */

			/*
			 * Create answers for the selected grammar symbols
			 */
			genAnswerSet.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					/*
					 * Clear answers for the selected grammar symbols
			         */
					for (int index = 0;
					     index < QuestionInfoNode.boundTreeElement.treeElementReference.getChildren().size();
					     index++)
					{
						@SuppressWarnings("unchecked")
						TreeItem<TreeElement> element = (TreeItem<TreeElement>)
								QuestionInfoNode.boundTreeElement.treeElementReference.getChildren().get(index);
						if (element.getValue() instanceof AnswerTreeElement)
							if (((AnswerTreeElement) element.getValue()).answerChoice.getAnswerText().length() == 1)
							{
								QuestionInfoNode.boundTreeElement.treeElementReference.getChildren().remove(element);
								index--;
							}
					}
					QuestionInfoNode.boundTreeElement.answers.clear();

					/*
					 * Redirect to symbol creation node
					 */
					CenterNode.removeScrollRoot();
					CenterNode.addScrollRoot();
					GrammarSymbolInitNode.parent = boundTreeElement;
					CenterNode.getScrollRoot().setContent(GrammarSymbolInitNode.getGrammarQuestionSymbolInitNode());
				}
			});

			/*
			 * Clear answers for the selected grammar symbols
			 */
			delAnswerSet.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (PopupManager.askYesNo("This will also delete the answer for the question if it has been set " +
							                          "already. Continue?"))
					{
						for (int index = 0;
						     index < QuestionInfoNode.boundTreeElement.treeElementReference.getChildren().size();
						     index++)
						{
							@SuppressWarnings("unchecked")
							TreeItem<TreeElement> element = (TreeItem<TreeElement>)
									QuestionInfoNode.boundTreeElement.treeElementReference.getChildren().get(index);
							if (element.getValue() instanceof AnswerTreeElement)
								if (((AnswerTreeElement) element.getValue()).answerChoice.getAnswerText().length() == 1)
								{
									QuestionInfoNode.boundTreeElement.treeElementReference.getChildren().remove(element);
									index--;
								}
						}
						QuestionInfoNode.boundTreeElement.answers.clear();
					}
				}
			});

			genAnswer.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{

				}
			});
		}

		questionInfoNode = horizontalRoot;
	}

	public static Node getQuestionInfoNode()
	{
		return questionInfoNode;
	}
}
