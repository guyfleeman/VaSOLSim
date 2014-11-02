package com.gmail.guyfleeman.vasolsim.tclient.element.tree;

import com.gmail.guyfleeman.vasolsim.common.struct.Question;
import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.ImageButton;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA.GrammarSymbolInitNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA.QuestionInfoNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
 */
public class QuestionTreeElement extends TreeElement
{
	public QuestionSetTreeElement parent;
	public QuestionTreeElement    instance;
	public Question                  question = new Question();
	public Vector<AnswerTreeElement> answers  = new Vector<AnswerTreeElement>();

	public QuestionTreeElement()
	{
		super(new Label("New Question"),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToAddIcon,
				      "btnconfirmhover",
				      "btnnormal",
				      12),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToRemoveIcon,
				      "btndenyhover",
				      "btnnormal",
				      12)
		);

		instance = this;
	}

	public void initListeners()
	{
		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				switch (question.getQuestionType())
				{
					case TE_D_AND_D_VENN_DIAGRAM: /* TODO */ break;
					case TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE:
					{
						CenterNode.removeScrollRoot();
						CenterNode.addScrollRoot();
						CenterNode.getScrollRoot().setContent(GrammarSymbolInitNode.getGrammarQuestionSymbolInitNode());
						break;
					}
					case MULTIPLE_RESPONSE:
					case TE_MULTIPLE_RESPONSE:
					case TE_D_AND_D_MULTIPLE_RESPONSE:
					{

						break;
					}
					case MULTIPLE_CHOICE:
					case TE_MULTIPLE_CHOICE:
					case TE_D_AND_D_MULTIPLE_CHOICE:
					{

						break;
					}
				}
			}
		});

		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				parent.questions.remove(instance);
				parent.treeElementReference.getChildren().remove(treeElementReference);
				CenterNode.getStyledRoot().getChildren().clear();
			}
		});

		this.instance.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();

				QuestionInfoNode.boundTreeElement = instance;
				QuestionInfoNode.updateNode();
				CenterNode.getScrollRoot().setContent(QuestionInfoNode.getQuestionInfoNode());
			}
		});
	}
}
