package com.vasolsim.tclient.form;

import com.vasolsim.common.node.DrawableNode;
import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.tclient.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/28/14 <p></p>
 */
public class QuestionTypeNode implements DrawableNode
{
	protected static Node questionTypeNode;
	public static QuestionType questionType = QuestionType.MULTIPLE_CHOICE;

	public QuestionTypeNode()
	{
		redrawNode(false);
	}

	public void redrawNode(boolean apply)
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label questionInfoLabel = new Label("Select the QandA type: ");
		questionInfoLabel.getStyleClass().add("lbltext");

		ToggleGroup rbGroup = new ToggleGroup();

		final RadioButton multipleChoiceRB = new RadioButton(TeacherClient.MULTIPLE_CHOICE_TEXT);
		multipleChoiceRB.setToggleGroup(rbGroup);
		multipleChoiceRB.getStyleClass().add("lbltextsub");
		multipleChoiceRB.setWrapText(true);

		final RadioButton multipleResponseRB = new RadioButton(TeacherClient.MULTIPLE_RESPONSE_TEXT);
		multipleResponseRB.setToggleGroup(rbGroup);
		multipleResponseRB.getStyleClass().add("lbltextsub");
		multipleResponseRB.setWrapText(true);

		final RadioButton teMultipleChoiceRB = new RadioButton(TeacherClient.TECH_ENHANCED_MULTIPLE_CHOICE_TEXT);
		teMultipleChoiceRB.setToggleGroup(rbGroup);
		teMultipleChoiceRB.getStyleClass().add("lbltextsub");
		teMultipleChoiceRB.setWrapText(true);

		final RadioButton teMultipleResponseRB = new RadioButton(TeacherClient.TECH_ENHANCED_MULTIPLE_RESPONSE_TEXT);
		teMultipleResponseRB.setToggleGroup(rbGroup);
		teMultipleResponseRB.getStyleClass().add("lbltextsub");
		teMultipleResponseRB.setWrapText(true);

		final RadioButton teDDMultipleChoiceRB = new RadioButton(TeacherClient.TECH_ENHANCED_DD_MULTIPLE_CHOICE_TEXT);
		teDDMultipleChoiceRB.setToggleGroup(rbGroup);
		teDDMultipleChoiceRB.getStyleClass().add("lbltextsub");
		teDDMultipleChoiceRB.setWrapText(true);

		final RadioButton teDDMultipleResponseRB = new RadioButton(TeacherClient.TECH_ENHANCED_DD_MULTIPLE_RESPONSE_TEXT);
		teDDMultipleResponseRB.setToggleGroup(rbGroup);
		teDDMultipleResponseRB.getStyleClass().add("lbltextsub");
		teDDMultipleResponseRB.setWrapText(true);

		final RadioButton teDDGrammarMultipleResponseRB = new RadioButton(TeacherClient.TECH_ENCHANCED_DD_GRAMMAR_TEXT);
		teDDGrammarMultipleResponseRB.setToggleGroup(rbGroup);
		teDDGrammarMultipleResponseRB.getStyleClass().add("lbltextsub");
		teDDGrammarMultipleResponseRB.setWrapText(true);

		final RadioButton teDDVennDiagramRB = new RadioButton(TeacherClient.TECH_ENCHANCED_VENN_DIAGRAM_TEXT);
		teDDVennDiagramRB.setToggleGroup(rbGroup);
		teDDVennDiagramRB.setDisable(true);
		teDDVennDiagramRB.getStyleClass().add("lbltextsub");
		teDDVennDiagramRB.setWrapText(true);

		Button continueButton = new Button(TeacherClient.CONTINUE_BUTTON_TEXT);

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
				CenterNode.getScrollRoot().setContent(TeacherClient.questionInitNode.getNode());
			}
		});

		questionTypeNode = horizontalRoot;

		if (apply)
		{
			CenterNode.addScrollRoot();
			CenterNode.getScrollRoot().setContent(questionTypeNode);
		}
	}

	public Node getNode()
	{
		return questionTypeNode;
	}
}
