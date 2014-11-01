package com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA;

import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/28/14
 * <p></p>
 */
public class QuestionTypeNode
{
	protected static Node questionTypeNode;
	public static QuestionType questionType = QuestionType.MULTIPLE_CHOICE;

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

		Label questionInfoLabel = new Label("Select the QandA type: ");
		questionInfoLabel.getStyleClass().add("lbltext");

		ToggleGroup rbGroup = new ToggleGroup();

		final RadioButton multipleChoiceRB = new RadioButton(multipleChoiceText);
		multipleChoiceRB.setToggleGroup(rbGroup);
		multipleChoiceRB.getStyleClass().add("lbltextsub");
		multipleChoiceRB.setWrapText(true);

		final RadioButton multipleResponseRB = new RadioButton(multipleResponseText);
		multipleResponseRB.setToggleGroup(rbGroup);
		multipleResponseRB.getStyleClass().add("lbltextsub");
		multipleResponseRB.setWrapText(true);

		final RadioButton teMultipleChoiceRB = new RadioButton(techEnhancedMultipleChoiceText);
		teMultipleChoiceRB.setToggleGroup(rbGroup);
		teMultipleChoiceRB.getStyleClass().add("lbltextsub");
		teMultipleChoiceRB.setWrapText(true);

		final RadioButton teMultipleResponseRB = new RadioButton(techEnchancedMultipleResponseText);
		teMultipleResponseRB.setToggleGroup(rbGroup);
		teMultipleResponseRB.getStyleClass().add("lbltextsub");
		teMultipleResponseRB.setWrapText(true);

		final RadioButton teDDMultipleChoiceRB = new RadioButton(techEnhancedDDMultipleChoiceText);
		teDDMultipleChoiceRB.setToggleGroup(rbGroup);
		teDDMultipleChoiceRB.getStyleClass().add("lbltextsub");
		teDDMultipleChoiceRB.setWrapText(true);

		final RadioButton teDDMultipleResponseRB = new RadioButton(techEnhancedDDMultipleResponseText);
		teDDMultipleResponseRB.setToggleGroup(rbGroup);
		teDDMultipleResponseRB.getStyleClass().add("lbltextsub");
		teDDMultipleResponseRB.setWrapText(true);

		final RadioButton teDDGrammarMultipleResponseRB = new RadioButton(techEnchancedDDGrammarText);
		teDDGrammarMultipleResponseRB.setToggleGroup(rbGroup);
		teDDGrammarMultipleResponseRB.getStyleClass().add("lbltextsub");
		teDDGrammarMultipleResponseRB.setWrapText(true);

		final RadioButton teDDVennDiagramRB = new RadioButton(techEnchancedVennDiagramText);
		teDDVennDiagramRB.setToggleGroup(rbGroup);
		teDDVennDiagramRB.setDisable(true);
		teDDVennDiagramRB.getStyleClass().add("lbltextsub");
		teDDVennDiagramRB.setWrapText(true);

		Button continueButton = new Button(continueButtonText);

		verticalRoot.getChildren().addAll(questionInfoLabel,
				multipleChoiceRB,
				multipleResponseRB,
				teMultipleChoiceRB,
				teMultipleResponseRB,
				teDDMultipleChoiceRB,
				teDDMultipleResponseRB,
				teDDGrammarMultipleResponseRB,
				teDDVennDiagramRB,
				continueButton);

		continueButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (multipleChoiceRB.isSelected())
					questionType = QuestionType.MULTIPLE_CHOICE;
				else if (multipleResponseRB.isSelected())
					questionType = QuestionType.MULTIPLE_RESPONSE;
				else if (teMultipleChoiceRB.isSelected())
					questionType = QuestionType.TE_MULTIPLE_CHOICE;
				else if (teMultipleResponseRB.isSelected())
					questionType = QuestionType.TE_MULTIPLE_RESPONSE;
				else if (teDDMultipleChoiceRB.isSelected())
					questionType = QuestionType.TE_D_AND_D_MULTIPLE_CHOICE;
				else if (teDDMultipleResponseRB.isSelected())
					questionType = QuestionType.TE_D_AND_D_MULTIPLE_RESPONSE;
				else if (teDDGrammarMultipleResponseRB.isSelected())
					questionType = QuestionType.TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE;
				else if (teDDVennDiagramRB.isSelected())
					questionType = QuestionType.TE_D_AND_D_VENN_DIAGRAM;

				CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(QuestionInitNode.getQuestionInitNode());
			}
		});

		questionTypeNode = horizontalRoot;
	}

	public static Node getQuestionTypeNode()
	{
		return questionTypeNode;
	}
}
