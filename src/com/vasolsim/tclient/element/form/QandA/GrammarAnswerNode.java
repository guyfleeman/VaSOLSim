package com.vasolsim.tclient.element.form.QandA;

import com.vasolsim.tclient.element.tree.QuestionTreeElement;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author guyfleeman
 * @date 8/3/14 <p></p>
 */
public class GrammarAnswerNode
{
	protected static Node                grammarQuestionAnswerNode;
	public static    QuestionTreeElement parent;

	public static void updateNode()
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);
	}

	public static Node getGrammarQuestionAnswerNode()
	{
		return grammarQuestionAnswerNode;
	}
}
