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

import javax.annotation.Nonnull;
import main.java.vasolsim.common.file.AnswerChoice;
import main.java.vasolsim.common.node.DrawableNode;
import main.java.vasolsim.common.node.StringPane;
import main.java.vasolsim.common.support.notification.PopupManager;
import main.java.vasolsim.teacherclient.core.CenterNode;
import main.java.vasolsim.teacherclient.tree.QuestionTreeElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.util.ArrayList;

import static main.java.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 8/7/14 <p></p>
 */
public class QuestionNode implements DrawableNode
{
	public static Logger logger = Logger.getLogger(QuestionNode.class.getName());

	public    QuestionTreeElement boundTreeElement;
	protected Node                questionInfoNode;
	protected Pane                styledRootNode;
	@Nonnull
	private StringPane draggablePane = new StringPane("",
	                                                  null, "charPaneDefault", null, "charPaneSmallText",
	                                                  40, 40, 3);

	public QuestionNode()
	{
		redrawNode(false);
	}

	public void redrawNode(boolean apply)
	{
		logger.info("displaying question -> " + (boundTreeElement == null ? "none" : boundTreeElement.question.getName()));

		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);
		styledRootNode = verticalRoot;

		draggablePane.setMouseTransparent(true);
		draggablePane.setPickOnBounds(false);

		drawHeaderNode(verticalRoot);
		if (boundTreeElement != null
				&& boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE)
			drawGrammarNode(verticalRoot);
		else if (boundTreeElement != null &&
				(boundTreeElement.question.getQuestionType() == QuestionType.MULTIPLE_CHOICE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_MULTIPLE_CHOICE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_MULTIPLE_CHOICE))
			drawMultipleChoiceNode(verticalRoot);
		else if (boundTreeElement != null &&
				(boundTreeElement.question.getQuestionType() == QuestionType.MULTIPLE_RESPONSE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_MULTIPLE_RESPONSE ||
						boundTreeElement.question.getQuestionType() == QuestionType.TE_D_AND_D_MULTIPLE_RESPONSE))
			drawMultipleResponseNode(verticalRoot);

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

	protected Node drawHeaderNode(Pane root)
	{
		Label infoLabel = new Label("Question Information Overview");
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		Label typeLabel = new Label("Type: " +
				                            ((boundTreeElement == null || boundTreeElement.question == null)
				                             ? "none"
				                             : boundTreeElement.question.getQuestionType()));
		typeLabel.getStyleClass().add("lbltext");
		typeLabel.setWrapText(true);

		final Label questionTextInfoLabel = new Label(
				"Question Text:\n" + (boundTreeElement == null
						                      || boundTreeElement.question == null
						                      || boundTreeElement.question.getQuestion() == null
						                      || boundTreeElement.question.getQuestion()
						                                                  .equals("")
				                      ? "none"
				                      : boundTreeElement.question.getQuestion()));
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
				{
					boundTreeElement.question.setQuestion(questionTextArea.getText());
					questionTextInfoLabel.setText(questionTextArea.getText());

					if (boundTreeElement.question.getQuestionType()
							== QuestionType.TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE)
					{
						redrawNode(true);
						for (AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
							if (ac.isCorrect())
								ac.clearVisualPersistence();
					}
				}
			}
		});

		root.getChildren().addAll(infoLabel,
		                          typeLabel,
		                          questionTextInfoLabel,
		                          questionTextArea,
		                          applyQuestionButton,
		                          spacer);

		return root;
	}

	/////////////////////////////////
	//  begin grammar node methods //
	/////////////////////////////////

	protected Node drawGrammarNode(Pane root)
	{
		Label chooseCharsLabel = new Label("Choose characters:\n");
		chooseCharsLabel.getStyleClass().add("lbltext");

		/*
		 * this will draw the set of character tiles that allow the selection of the symbol set, then it will draw
		 * the paragraph with draggable docks, and lastly it will draw the draggables
		 */
		TilePane charSelectionFlowPane = new TilePane();
		final FlowPane paragraphFlowPane = new FlowPane();
		final FlowPane draggableCharsFlowPane = new FlowPane();

		/*
		 * rsc.style the char selection section
		 */
		charSelectionFlowPane.setPrefWidth(2000);
		charSelectionFlowPane.setVgap(15);
		charSelectionFlowPane.setHgap(15);
		charSelectionFlowPane.setAlignment(Pos.CENTER);

		/*
		 * initialize the choice tiles
		 */
		for (final AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
			if (!ac.isCorrect())
			{
				final StringPane sp = new StringPane(ac.getAnswerText());
				sp.getActiveProperty().setValue(ac.isActive());
				charSelectionFlowPane.getChildren().add(sp);

				sp.getActiveProperty().addListener(new ChangeListener<Boolean>()
				{
					@Override
					public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					                    Boolean newValue)
					{
						ac.setActive(newValue);
						draggableCharsFlowPane.getChildren().clear();
						initDraggableChoices(draggableCharsFlowPane);

						if (!newValue)
							for (int index = 0; index < paragraphFlowPane.getChildren().size(); index++)
								if (paragraphFlowPane.getChildren().get(index) instanceof StringPane
										&& ((StringPane) paragraphFlowPane.getChildren().get(index)).getOverlay().equals(

										sp.getOverlay()))
								{
									StackPane rect = new StackPane();
									rect.setMinSize(40, 40);
									rect.setMaxSize(40, 40);
									rect.getStyleClass().add("grammarSpace");
									initDragTargetRoutine(rect, paragraphFlowPane);

									paragraphFlowPane.getChildren().set(index, rect);
								}

					}
				});
			}

		/*
		 * create a spacer between the selection tiles and the draggable section
		 */
		HBox spacerTwo = new HBox();
		spacerTwo.setPrefHeight(1);
		spacerTwo.setPrefWidth(2000);
		spacerTwo.getStyleClass().add("lblspacer");

		/*
		 * add the current data
		 */
		root.getChildren().addAll(chooseCharsLabel,
		                          charSelectionFlowPane,
		                          spacerTwo);

		if (boundTreeElement.question.getQuestion() != null && !boundTreeElement.question.getQuestion().equals(""))
		{
			/*
			 * create the labels
			 */
			Label title = new Label("Answer:\n\n");
			title.getStyleClass().add("lbltext");

			/*
			 * rsc.style the paragraph flow pane, then create it
			 */
			paragraphFlowPane.setHgap(8);
			paragraphFlowPane.setVgap(8);
			paragraphFlowPane.setAlignment(Pos.TOP_LEFT);
			initParagraphSection(paragraphFlowPane);

			/*
			 * rsc.style draggable chars flow pane, then create them
			 */
			draggableCharsFlowPane.setPrefWidth(2000);
			draggableCharsFlowPane.setVgap(15);
			draggableCharsFlowPane.setHgap(15);
			draggableCharsFlowPane.setAlignment(Pos.CENTER);
			initDraggableChoices(draggableCharsFlowPane);

			Button applyButton = new Button("Apply");
			applyButton.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					AnswerChoice correctHolder = null;
					for (AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
						if (ac.isCorrect())
						{
							correctHolder = ac;
							break;
						}

					if (correctHolder == null)
					{
						PopupManager.showMessage("Internal answer handler error");
						return;
					}

					correctHolder.setVisualPersistence(paragraphFlowPane.getChildren());
				}
			});

			/*
			 * append everything
			 */
			root.getChildren().addAll(title,
			                          paragraphFlowPane,
			                          draggableCharsFlowPane,
			                          applyButton);
		}
		else
		{
			Label title = new Label("Answer: none");
			title.getStyleClass().add("lbltext");

			root.getChildren().add(title);
		}

		return root;
	}

	/*
	 * creates the nodes that are draggable
	 */
	private Node initDraggableChoices(FlowPane draggableCharsFlowPane)
	{
		if (boundTreeElement.question.getQuestion() != null
				&& !boundTreeElement.question.getQuestion().equals(""))
			for (AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
				if (ac.isActive())
				{
					final StringPane sp = new StringPane(ac.getAnswerText(),
					                                     null,
					                                     "charPaneDefault",
					                                     null,
					                                     "charPaneDefaultText",
					                                     60,
					                                     60,
					                                     3);
					sp.getActiveProperty().setValue(false);
					draggableCharsFlowPane.getChildren().add(sp);

					initDragRoutine(sp);
				}

		return draggableCharsFlowPane;
	}

	/*
	 * creates the nodes that initialize the answer
	 */
	private Node initParagraphSection(FlowPane paragraphFlowPane)
	{

		if (boundTreeElement.question.getQuestion() != null
				&& !boundTreeElement.question.getQuestion().equals(""))
		{
			AnswerChoice correctChoice = null;
			for (AnswerChoice ac : boundTreeElement.question.getAnswerChoices())
				if (ac.isCorrect() && ac.getVisualPersistence() != null)
				{
					correctChoice = ac;
					break;
				}

			if (correctChoice != null && !correctChoice.getVisualPersistence().isEmpty())
			{
				paragraphFlowPane.getChildren().clear();
				//TODO remove
				System.out.println(correctChoice.getVisualPersistence());
				System.out.println(paragraphFlowPane.getChildren());
				System.out.println(correctChoice.getVisualPersistence().size());
				paragraphFlowPane.getChildren().addAll(correctChoice.getVisualPersistence());
				for (Node n : paragraphFlowPane.getChildren())
				{
					if (n instanceof StringPane)
					{
						StackPane rect = new StackPane();
						rect.setMinSize(40, 40);
						rect.setMaxSize(40, 40);
						rect.getStyleClass().add("grammarSpace");
						initDragTargetRoutine(rect, paragraphFlowPane);
						initDragRoutine(n, true, rect, paragraphFlowPane);

						initDragTargetRoutine(n, paragraphFlowPane);
					}
					else if (n instanceof StackPane)
					{
						initDragTargetRoutine(n, paragraphFlowPane);
					}
				}
			}
			else
				for (String s : boundTreeElement.question.getQuestion().split(" "))
				{
					Label label = new Label(s);
					label.getStyleClass().add("lbltext");
					label.setWrapText(false);

					StackPane sp = new StackPane();
					sp.setMinSize(40, 40);
					sp.setMaxSize(40, 40);
					sp.getStyleClass().add("grammarSpace");
					initDragTargetRoutine(sp, paragraphFlowPane);

					paragraphFlowPane.getChildren().addAll(label, sp);
				}
		}




		return paragraphFlowPane;
	}

	/**
	 * created a drag routine linked to a floating StringPane
	 *
	 * @param source the source of the init of the drag routine
	 */
	private void initDragRoutine(final Node source)
	{
		initDragRoutine(source, false, null, null);
	}

	/**
	 * created a drag routine linked to a floating StringPane
	 *
	 * @param source        the source of the init of the drag routine
	 * @param replaceSource will the source be replaced
	 * @param replaceWith   what it will be replaced with
	 * @param replaceFrom   the location in which it will be replaced
	 */
	private void initDragRoutine(final Node source,
	                             final boolean replaceSource,
	                             final Node replaceWith,
	                             final Pane replaceFrom)
	{
		/*
		 * add the draggable pane and create its text
		 */
		source.setOnDragDetected(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				System.out.println("drag initialized");

				//update the content of the draggable instance with that of the fixed position source
				if (mouseEvent.getSource() instanceof StringPane)
					draggablePane.setOverlay(((StringPane) mouseEvent.getSource()).getOverlay());

				//add to scene
				styledRootNode.getChildren().add(draggablePane);

				draggablePane.startFullDrag();

				//replace source
				if (replaceSource && replaceFrom != null)
					for (int index = 0; index < replaceFrom.getChildren().size(); index++)
						if (replaceFrom.getChildren().get(index) == source)
						{
							replaceFrom.getChildren().get(index).setManaged(false);
							replaceFrom.getChildren().get(index).setVisible(false);
							replaceFrom.getChildren().add(index + 1, replaceWith);
						}

				mouseEvent.consume();
			}
		});

		/*
		 * update the position
		 */
		source.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				System.out.println("dragged");

				//update position
				Point2D localPoint = questionInfoNode.sceneToLocal(
						new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
				draggablePane.setTranslateX(localPoint.getX() -
						                            draggablePane.getLayoutX() -
						                            draggablePane.getBoundsInLocal().getWidth() / 2);
				draggablePane.setTranslateY(localPoint.getY() -
						                            draggablePane.getLayoutY() -
						                            draggablePane.getBoundsInLocal().getHeight() / 2);


				//source.setCursor(Cursor.NONE);
				mouseEvent.consume();
			}
		});

		/*
		 * adjust cursor on entry
		 */
		source.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e)
			{
				source.setCursor(Cursor.HAND);
				e.consume();
			}
		});

		/*
		 * adjust cursor on pressed
		 */
		source.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e)
			{
				System.out.println("mouse pressed");

				source.setMouseTransparent(true);
				draggablePane.setMouseTransparent(true);
				source.setCursor(Cursor.CLOSED_HAND);

				e.consume();

				System.out.println("mouse pressed done");
			}
		});

		/*
		 * adjust cursor on released
		 */
		source.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e)
			{
				System.out.println("mouse released");

				source.setMouseTransparent(false);
				draggablePane.setMouseTransparent(false);
				source.setCursor(Cursor.DEFAULT);
				styledRootNode.getChildren().remove(draggablePane);

				//replace source
				if (replaceSource && replaceFrom != null)
					for (int index = 0; index < replaceFrom.getChildren().size(); index++)
						if (replaceFrom.getChildren().get(index) == source)
							replaceFrom.getChildren().remove(index);

				e.consume();

				System.out.println("mouse released done");
			}
		});
	}

	private void initDragTargetRoutine(final Node target, final Pane paragraphFlowPane)
	{
		target.setOnMouseDragEntered(new EventHandler<MouseDragEvent>()
		{
			public void handle(MouseDragEvent e)
			{

				target.getStyleClass().clear();
				target.getStyleClass().add("grammarSpaceActive");
				e.consume();
			}
		});

		target.setOnMouseDragExited(new EventHandler<MouseDragEvent>()
		{
			public void handle(MouseDragEvent e)
			{

				target.getStyleClass().clear();

				if (target instanceof StringPane)
				{
					target.getStyleClass().add("charPaneDefaultSuper");
					target.getStyleClass().remove("grammarSpaceActive");
				}
				else
					target.getStyleClass().add("grammarSpace");

				e.consume();
			}
		});

		target.setOnMouseDragReleased(new EventHandler<MouseDragEvent>()
		{
			public void handle(MouseDragEvent e)
			{

				System.out.println("target drag released");
				if (e.getGestureSource() instanceof StringPane)
					for (int index = 0; index < paragraphFlowPane.getChildren().size(); index++)
						if (paragraphFlowPane.getChildren().get(index) == e.getTarget())
							if (e.getTarget() instanceof StringPane)
								((StringPane) e.getSource()).setOverlay(
										((StringPane) e.getGestureSource()).getOverlay());
							else
							{
								StringPane sp = new StringPane(((StringPane) e.getGestureSource()).getOverlay(),
								                               "charPaneDefaultSuper",
								                               "charPaneDefault",
								                               null,
								                               "charPaneSmallText",
								                               40,
								                               40,
								                               3);

								paragraphFlowPane.getChildren().set(index, sp);

								StackPane rect = new StackPane();
								rect.setMinSize(40, 40);
								rect.setMaxSize(40, 40);
								rect.getStyleClass().add("grammarSpace");
								initDragTargetRoutine(rect, paragraphFlowPane);
								initDragRoutine(sp, true, rect, paragraphFlowPane);

								initDragTargetRoutine(sp, paragraphFlowPane);
							}

				e.consume();
			}
		});
	}

	////////////////////////////////
	//  end grammar node methods  //
	////////////////////////////////

	protected Node drawMultipleChoiceNode(Pane root)
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
						{
							ac.setCorrect(false);
							boundTreeElement.question.getCorrectAnswerChoices().remove(ac);
						}
						else if (!oldValue && newValue)
						{
							ac.setCorrect(true);
							boundTreeElement.question.getCorrectAnswerChoices().add(ac);
						}
					}
				});

				count++;
			}

			/*
			 * apply answer and ID updates through fields
			 */
			Button applyAnswersButton = new Button("Apply");
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

			root.getChildren().addAll(answerContainer,
			                          applyAnswersButton);
		}
		else
		{
			Label label = new Label("no answer structure defined");
			label.getStyleClass().add("lbltext");
			label.setWrapText(true);

			root.getChildren().add(label);
		}

		return root;
	}

	protected Node drawMultipleResponseNode(Pane root)
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

			root.getChildren().addAll(answerContainer,
			                          applyAnswersButton);
		}
		else
		{
			Label label = new Label("no answer structure defined");
			label.getStyleClass().add("lbltext");
			label.setWrapText(true);

			root.getChildren().add(label);
		}

		return root;
	}
}
