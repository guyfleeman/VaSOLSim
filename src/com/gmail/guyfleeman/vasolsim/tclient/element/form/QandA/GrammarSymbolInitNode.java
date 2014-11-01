package com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA;

import com.gmail.guyfleeman.vasolsim.common.struct.AnswerChoice;
import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.AnswerTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.QuestionTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;
import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.createTreeItem;

/**
 * @author guyfleeman
 * @date 8/3/14 <p></p>
 */
public class GrammarSymbolInitNode
{
	protected static Node grammarQuestionSymbolInitNode;
	public static ArrayList<Character> chars = new ArrayList<Character>();
	public static QuestionTreeElement parent;

	static
	{
		updateNode();
	}

	public static void updateNode()
	{
		HBox horizontalRoot = new HBox();

		final VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label infoLabel = new Label();
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		final CheckBox commaCB = new CheckBox();
		commaCB.setText(", (comma)");
		commaCB.getStyleClass().add("lbltextsub");
		commaCB.setIndeterminate(false);

		final CheckBox periodCB = new CheckBox();
		periodCB.setText(". (period)");
		periodCB.getStyleClass().add("lbltextsub");
		periodCB.setIndeterminate(false);

		final CheckBox questionCB = new CheckBox();
		questionCB.setText("? (QandA mark)");
		questionCB.getStyleClass().add("lbltextsub");
		questionCB.setIndeterminate(false);

		final CheckBox exclamationCB = new CheckBox();
		exclamationCB.setText("! (exclamation point)");
		exclamationCB.getStyleClass().add("lbltextsub");
		exclamationCB.setIndeterminate(false);

		final CheckBox semiColonCB = new CheckBox();
		semiColonCB.setText("; (semi colon)");
		semiColonCB.getStyleClass().add("lbltextsub");
		semiColonCB.setIndeterminate(false);

		final CheckBox colonCB = new CheckBox();
		colonCB.setText(": (colon)");
		colonCB.getStyleClass().add("lbltextsub");
		colonCB.setIndeterminate(false);

		final CheckBox hyphenCB = new CheckBox();
		hyphenCB.setText("- (hyphen/minus sign)");
		hyphenCB.getStyleClass().add("lbltextsub");
		hyphenCB.setIndeterminate(false);

		final CheckBox enDashCB = new CheckBox();
		enDashCB.setText("\u2013 (en dash)");
		enDashCB.getStyleClass().add("lbltextsub");
		enDashCB.setIndeterminate(false);

		final CheckBox emDashCB = new CheckBox();
		emDashCB.setText("\u2014 (em dash)");
		emDashCB.getStyleClass().add("lbltextsub");
		emDashCB.setIndeterminate(false);

		final CheckBox nullCB = new CheckBox();
		nullCB.setText("space (option to leave a spot blank)");
		nullCB.getStyleClass().add("lbltextsub");
		emDashCB.setIndeterminate(false);

		Button continueButton = new Button(continueButtonText);

		verticalRoot.getChildren().addAll(infoLabel,
		                                  commaCB,
		                                  periodCB,
		                                  questionCB,
		                                  exclamationCB,
		                                  semiColonCB,
		                                  colonCB,
		                                  hyphenCB,
		                                  enDashCB,
		                                  emDashCB,
		                                  nullCB,
		                                  continueButton);

		continueButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				chars.clear();

				if (commaCB.isSelected())
					chars.add(',');

				if (periodCB.isSelected())
					chars.add('.');

				if (questionCB.isSelected())
					chars.add('?');

				if (exclamationCB.isSelected())
					chars.add('!');

				if (semiColonCB.isSelected())
					chars.add(';');

				if (colonCB.isSelected())
					chars.add(':');

				if (hyphenCB.isSelected())
					chars.add('-');

				if (enDashCB.isSelected())
					chars.add('\u2013');

				if (emDashCB.isSelected())
					chars.add('\u2014');

				if (nullCB.isSelected())
					chars.add(' ');

				char asciiCharCode = 'A';
				for (char c : chars)
				{
					AnswerTreeElement newAnswer = new AnswerTreeElement();
					newAnswer.answerChoice = new AnswerChoice(c != '.' ? Character.getName(c).toLowerCase() : "period",
					                                          String.valueOf(asciiCharCode++),
					                                          String.valueOf(c),
					                                          false);
					newAnswer.label.setText(c != '.' ? Character.getName(c).toLowerCase() : "period");
					newAnswer.initListeners();
					newAnswer.parent = GrammarSymbolInitNode.parent;

					TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
					                                               newAnswer,
					                                               TeacherClient.pathToQuestionIcon,
					                                               24);
					newAnswer.treeElementReference = element;
					newAnswer.parent = GrammarSymbolInitNode.parent;
					newAnswer.setCorrectIcon(false);
					parent.treeElementReference.getChildren().add(element);

					parent.answers.add(newAnswer);

				}

				for (Node n : verticalRoot.getChildren())
				    if (n instanceof CheckBox)
					    ((CheckBox) n).setSelected(false);

				CenterNode.removeScrollRoot();
			}
		});

		grammarQuestionSymbolInitNode = horizontalRoot;
	}

	public static Node getGrammarQuestionSymbolInitNode()
	{
		return grammarQuestionSymbolInitNode;
	}
}
