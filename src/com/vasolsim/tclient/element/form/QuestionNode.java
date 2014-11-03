package com.vasolsim.tclient.element.form;

import com.vasolsim.common.file.AnswerChoice;
import com.vasolsim.common.node.StringPane;
import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.tclient.element.core.CenterNode;
import com.vasolsim.tclient.element.tree.QuestionTreeElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 8/7/14 <p></p>
 */
public class QuestionNode implements DrawableNode
{
	protected Node                questionInfoNode;
	public    QuestionTreeElement boundTreeElement;

	public QuestionNode()
	{
		redrawNode(false);
	}

	public void redrawNode(boolean apply)
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label infoLabel = new Label("Question Information Overview");
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		Label typeLabel = new Label("Type: " +
				                            ((boundTreeElement == null || boundTreeElement.question == null)
				                             ? "none"
				                             : boundTreeElement.question.getQuestionType()));
		typeLabel.getStyleClass().add("lbltext");
		typeLabel.setWrapText(true);

		final Label questionTextInfoLabel = new Label("Question Text:\n" + (boundTreeElement == null
				                                                                    || boundTreeElement.question ==
				null
				                                                                    || boundTreeElement.question
				.getQuestion() == null
				                                                                    || boundTreeElement.question
				.getQuestion().equals("")
		                                                                    ? "none"
		                                                                    : boundTreeElement.question.getQuestion
				                                                                    ()));
		questionTextInfoLabel.getStyleClass().add("lbltext");
		questionTextInfoLabel.setWrapText(true);

		final TextArea questionTextArea = new TextArea(
				boundTreeElement == null
						|| boundTreeElement.question == null
						|| boundTreeElement.question.getQuestion() == null
						|| boundTreeElement.question.getQuestion().equals("")
				? ""
				: boundTreeElement.question.getQuestion());
		questionTextArea.setPrefWidth(2000);
		questionTextArea.setPrefHeight(180);
		questionTextArea.setWrapText(true);

		Button applyQuestionButton = new Button("Apply");

		HBox spacer = new HBox();
		spacer.setPrefHeight(1);
		spacer.setPrefWidth(2000);
		spacer.getStyleClass().add("lblspacer");

		/*
		 * question text updater
		 */
		applyQuestionButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (questionTextArea.getText() != null && !questionTextArea.getText().equals(""))
					boundTreeElement.question.setQuestion(questionTextArea.getText());

				redrawNode(true);
			}
		});

		verticalRoot.getChildren().addAll(infoLabel,
		                                  typeLabel,
		                                  questionTextInfoLabel,
		                                  questionTextArea,
		                                  applyQuestionButton,
		                                  spacer);

		if (boundTreeElement != null
				&& boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE)
		{
			TilePane charDisplay = new TilePane();
			charDisplay.setPrefWidth(2000);
			charDisplay.setAlignment(Pos.TOP_LEFT);

			for (final AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
			{
				System.out.println(ac.getAnswerText());
				System.out.println(ac.isActive());
				StringPane sp = new StringPane(ac.getAnswerText());
				sp.getActiveProperty().setValue(ac.isActive());
				charDisplay.getChildren().add(sp);

				sp.getActiveProperty().addListener(new ChangeListener<Boolean>()
				{
					@Override
					public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					                    Boolean newValue)
					{
						ac.setActive(newValue);
					}
				});
			}

			HBox spacerTwo = new HBox();
			spacer.setPrefHeight(1);
			spacer.setPrefWidth(2000);
			spacer.getStyleClass().add("lblspacer");

			verticalRoot.getChildren().addAll(charDisplay,
			                               spacerTwo);

			//StringPane stringPane = new StringPane(":");
			//verticalRoot.getChildren().add(stringPane);

//			HBox symbolButtonRoot = new HBox();
//			symbolButtonRoot.getStyleClass().add("helementspacing");
//			Button genAnswerSet = new Button("Generate Symbol Set");
//			Button delAnswerSet = new Button("Delete Current Symbol Set");
//			symbolButtonRoot.getChildren().addAll(genAnswerSet, delAnswerSet);
//
//			HBox answerButtonRoot = new HBox();
//			answerButtonRoot.getStyleClass().add("helementspacing");
//			Button genAnswer = new Button("Generate Visual Answer");
//			Button delAnswer = new Button("Delete Visual Answer");
//			answerButtonRoot.getChildren().addAll(genAnswer, delAnswer);
//
//			verticalRoot.getChildren().addAll(symbolButtonRoot,
//			                                  answerButtonRoot);
//
//			/*
//			 * Init listeners
//		     */
//
//			/*
//			 * Create answers for the selected grammar symbols
//			 */
//			genAnswerSet.setOnMouseClicked(new EventHandler<MouseEvent>()
//			{
//				@Override
//				public void handle(MouseEvent mouseEvent)
//				{
//					/*
//					 * Clear answers for the selected grammar symbols
//			         */
//					for (int index = 0;
//					     index < TeacherClient.questionNode.boundTreeElement.
//							     treeElementReference.getChildren().size();
//					     index++)
//					{
//						@SuppressWarnings("unchecked")
//						TreeItem<TreeElement> element = (TreeItem<TreeElement>)
//								TeacherClient.questionNode.boundTreeElement.
//										treeElementReference.getChildren().get(index);
//						if (element.getValue() instanceof AnswerTreeElement)
//							if (((AnswerTreeElement) element.getValue()).answerChoice.getAnswerText().length() == 1)
//							{
//								TeacherClient.questionNode.boundTreeElement.
//										treeElementReference.getChildren().remove(element);
//								index--;
//							}
//					}
//					TeacherClient.questionNode.boundTreeElement.answers.clear();
//
//					/*
//					 * Redirect to symbol creation node
//					 */
//					CenterNode.removeScrollRoot();
//					CenterNode.addScrollRoot();
//					GrammarSymbolInitNode.parent = boundTreeElement;
//					CenterNode.getScrollRoot().setContent(GrammarSymbolInitNode.getGrammarQuestionSymbolInitNode());
//				}
//			});
//
//			/*
//			 * Clear answers for the selected grammar symbols
//			 */
//			delAnswerSet.setOnMouseClicked(new EventHandler<MouseEvent>()
//			{
//				@Override
//				public void handle(MouseEvent mouseEvent)
//				{
//					if (PopupManager.askYesNo("This will also delete the answer for the question if it has been set " +
//							                          "already. Continue?"))
//					{
//						for (int index = 0;
//						     index < TeacherClient.questionNode.boundTreeElement.treeElementReference.getChildren
//								     ().size();
//						     index++)
//						{
//							@SuppressWarnings("unchecked")
//							TreeItem<TreeElement> element = (TreeItem<TreeElement>)
//									TeacherClient.questionNode.boundTreeElement.treeElementReference.getChildren()
//									                                                                    .get(index);
//							if (element.getValue() instanceof AnswerTreeElement)
//								if (((AnswerTreeElement) element.getValue()).answerChoice.getAnswerText().length()
//										== 1)
//								{
//									TeacherClient.questionNode.boundTreeElement.treeElementReference.getChildren()
//									                                                                    .remove
//											                                                                    (element);
//									index--;
//								}
//						}
//						TeacherClient.questionNode.boundTreeElement.answers.clear();
//					}
//				}
//			});
//
//			genAnswer.setOnMouseClicked(new EventHandler<MouseEvent>()
//			{
//				@Override
//				public void handle(MouseEvent mouseEvent)
//				{
//
//				}
//			});
		}
		else if (boundTreeElement != null &&
				(boundTreeElement.question.getQuestionType() == QuestionType.MULTIPLE_CHOICE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_MULTIPLE_CHOICE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_MULTIPLE_CHOICE))
		{
			if (boundTreeElement.question.getAnswerChoices() != null)
			{
				final GridPane answerContainer = new GridPane();
				answerContainer.setHgap(10);
				answerContainer.setVgap(10);

				Label activeLabel = new Label("active");
				activeLabel.getStyleClass().add("lbltext");
				activeLabel.setWrapText(true);
				activeLabel.setMinWidth(80);
				answerContainer.add(activeLabel, 0, 0);

				Label correctLabel = new Label("correct");
				correctLabel.getStyleClass().add("lbltext");
				correctLabel.setWrapText(true);
				correctLabel.setMinWidth(80);
				answerContainer.add(correctLabel, 1, 0);

				Label idLabel = new Label("id");
				idLabel.getStyleClass().add("lbltext");
				idLabel.setWrapText(true);
				idLabel.setMinWidth(60);
				answerContainer.add(idLabel, 2, 0);

				Label choiceLabel = new Label("answer choice");
				choiceLabel.getStyleClass().add("lbltext");
				choiceLabel.setWrapText(true);
				choiceLabel.setPrefWidth(2000);
				answerContainer.add(choiceLabel, 3, 0);

				ToggleGroup toggleGroup = new ToggleGroup();

				int count = 1;
				boolean hasFoundCorrect = false;
				for (final AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
				{
					final CheckBox active = new CheckBox();
					active.setAllowIndeterminate(false);
					active.setSelected(ac.isActive());
					answerContainer.add(active, 0, count);

					if (!hasFoundCorrect && ac.isCorrect())
						hasFoundCorrect = true;
					else if (hasFoundCorrect && ac.isCorrect())
					{
						//log error
						ac.setCorrect(false);
					}

					final RadioButton correct = new RadioButton();
					correct.setSelected(ac.isCorrect());
					correct.setDisable(!ac.isActive());
					correct.setToggleGroup(toggleGroup);
					answerContainer.add(correct, 1, count);

					final TextField answerID = new TextField(
							ac.getVisibleChoiceID() == null || ac.getVisibleChoiceID().equals("")
							? Integer.toString(count) + "."
							: ac.getVisibleChoiceID());
					answerID.setPrefWidth(50);
					answerID.setDisable(!ac.isActive());
					answerContainer.add(answerID, 2, count);

					final TextField answerText = new TextField(
							ac.getAnswerText() == null || ac.getAnswerText().equals("")
							? "Answer: " + Integer.toString(count)
							: ac.getAnswerText());
					answerText.setPrefWidth(2000);
					answerText.setDisable(!ac.isActive());
					answerContainer.add(answerText, 3, count);

					/*
					 *
					 */
					active.selectedProperty().addListener(new ChangeListener<Boolean>()
					{
						@Override
						public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
						                    Boolean newValue)
						{
							if (oldValue && !newValue)
							{
								ac.setActive(false);

								correct.setDisable(true);
								answerID.setDisable(true);
								answerText.setDisable(true);
							}
							else if (!oldValue && newValue)
							{
								ac.setActive(true);

								correct.setDisable(false);
								answerID.setDisable(false);
								answerText.setDisable(false);
							}
						}
					});

					/*
					 *
					 */
					correct.selectedProperty().addListener(new ChangeListener<Boolean>()
					{
						@Override
						public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
						                    Boolean newValue)
						{
							if (oldValue && !newValue)
								ac.setCorrect(false);
							else if (!oldValue && newValue)
								ac.setCorrect(true);
						}
					});

					count++;
				}

				Button applyAnswersButton = new Button("Apply");
				/*
				 * hideously apply answer and ID updates through fields
				 */
				applyAnswersButton.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent mouseEvent)
					{
						ObservableList<Node> nodes = answerContainer.getChildren();
						ArrayList<AnswerChoice> answers = boundTreeElement.question.getAnswerChoices();
						if (nodes.size() % 4 != 0 || (nodes.size() / 4) - 1 != answers.size())
						{
							//err out, malformed internal structure
							return;
						}

						for (int i = 4; i < nodes.size(); i += 4)
						{
							if (nodes.get(i + 2) instanceof TextField
									&& (((TextField) nodes.get(i + 2)).getText() != null
									|| !((TextField) nodes.get(i + 2)).getText().equals("")))
								answers.get((i / 4) - 1).setVisibleChoiceID(((TextField) nodes.get(i + 2)).getText());

							if (nodes.get(i + 3) instanceof TextField
									&& (((TextField) nodes.get(i + 3)).getText() != null
									|| !((TextField) nodes.get(i + 3)).getText().equals("")))
								answers.get((i / 4) - 1).setText(((TextField) nodes.get(i + 3)).getText());
						}

						redrawNode(true);
					}
				});

				verticalRoot.getChildren().addAll(answerContainer,
				                                  applyAnswersButton);
			}
			else
			{
				Label label = new Label("no answer structure defined");
				label.getStyleClass().add("lbltext");
				label.setWrapText(true);

				verticalRoot.getChildren().add(label);
			}
		}
		else if (boundTreeElement != null &&
				(boundTreeElement.question.getQuestionType() == QuestionType.MULTIPLE_RESPONSE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_MULTIPLE_RESPONSE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_MULTIPLE_RESPONSE))
		{
			if (boundTreeElement.question.getAnswerChoices() != null)
			{
				final GridPane answerContainer = new GridPane();
				answerContainer.setHgap(10);
				answerContainer.setVgap(10);

				Label activeLabel = new Label("active");
				activeLabel.getStyleClass().add("lbltext");
				activeLabel.setWrapText(true);
				activeLabel.setMinWidth(80);
				answerContainer.add(activeLabel, 0, 0);

				Label correctLabel = new Label("correct");
				correctLabel.getStyleClass().add("lbltext");
				correctLabel.setWrapText(true);
				correctLabel.setMinWidth(80);
				answerContainer.add(correctLabel, 1, 0);

				Label idLabel = new Label("id");
				idLabel.getStyleClass().add("lbltext");
				idLabel.setWrapText(true);
				idLabel.setMinWidth(60);
				answerContainer.add(idLabel, 2, 0);

				Label choiceLabel = new Label("answer choice");
				choiceLabel.getStyleClass().add("lbltext");
				choiceLabel.setWrapText(true);
				choiceLabel.setPrefWidth(2000);
				answerContainer.add(choiceLabel, 3, 0);

				int count = 1;
				for (final AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
				{
					final CheckBox active = new CheckBox();
					active.setAllowIndeterminate(false);
					active.setSelected(ac.isActive());
					answerContainer.add(active, 0, count);

					final CheckBox correct = new CheckBox();
					correct.setSelected(ac.isCorrect());
					correct.setDisable(!ac.isActive());
					answerContainer.add(correct, 1, count);

					final TextField answerID = new TextField(
							ac.getVisibleChoiceID() == null || ac.getVisibleChoiceID().equals("")
							? Integer.toString(count) + "."
							: ac.getVisibleChoiceID());
					answerID.setPrefWidth(50);
					answerID.setDisable(!ac.isActive());
					answerContainer.add(answerID, 2, count);

					final TextField answerText = new TextField(
							ac.getAnswerText() == null || ac.getAnswerText().equals("")
							? "Answer: " + Integer.toString(count)
							: ac.getAnswerText());
					answerText.setPrefWidth(2000);
					answerText.setDisable(!ac.isActive());
					answerContainer.add(answerText, 3, count);

					/*
					 *
					 */
					active.selectedProperty().addListener(new ChangeListener<Boolean>()
					{
						@Override
						public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
						                    Boolean newValue)
						{
							if (oldValue && !newValue)
							{
								ac.setActive(false);

								correct.setDisable(true);
								answerID.setDisable(true);
								answerText.setDisable(true);
							}
							else if (!oldValue && newValue)
							{
								ac.setActive(true);

								correct.setDisable(false);
								answerID.setDisable(false);
								answerText.setDisable(false);
							}
						}
					});

					/*
					 *
					 */
					correct.selectedProperty().addListener(new ChangeListener<Boolean>()
					{
						@Override
						public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
						                    Boolean newValue)
						{
							if (oldValue && !newValue)
								ac.setCorrect(false);
							else if (!oldValue && newValue)
								ac.setCorrect(true);
						}
					});

					count++;
				}

				Button applyAnswersButton = new Button("Apply");
				/*
				 * hideously apply answer and ID updates through fields
				 */
				applyAnswersButton.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent mouseEvent)
					{
						ObservableList<Node> nodes = answerContainer.getChildren();
						ArrayList<AnswerChoice> answers = boundTreeElement.question.getAnswerChoices();
						if (nodes.size() % 4 != 0 || (nodes.size() / 4) - 1 != answers.size())
						{
							//err out, malformed internal structure
							return;
						}

						for (int i = 4; i < nodes.size(); i += 4)
						{
							if (nodes.get(i + 2) instanceof TextField
									&& (((TextField) nodes.get(i + 2)).getText() != null
									|| !((TextField) nodes.get(i + 2)).getText().equals("")))
								answers.get((i / 4) - 1).setVisibleChoiceID(((TextField) nodes.get(i + 2)).getText());

							if (nodes.get(i + 3) instanceof TextField
									&& (((TextField) nodes.get(i + 3)).getText() != null
									|| !((TextField) nodes.get(i + 3)).getText().equals("")))
								answers.get((i / 4) - 1).setText(((TextField) nodes.get(i + 3)).getText());
						}

						redrawNode(true);
					}
				});

				verticalRoot.getChildren().addAll(answerContainer,
				                                  applyAnswersButton);
			}
			else
			{
				Label label = new Label("no answer structure defined");
				label.getStyleClass().add("lbltext");
				label.setWrapText(true);

				verticalRoot.getChildren().add(label);
			}
		}

		questionInfoNode = horizontalRoot;

		if (apply)
		{
			CenterNode.addScrollRoot();
			CenterNode.getScrollRoot().setContent(questionInfoNode);
		}
	}

	public Node getNode()
	{
		return questionInfoNode;
	}
}
