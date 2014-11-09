package com.vasolsim.tclient.element.tree;

import com.vasolsim.common.file.Question;
import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.common.node.ImageButton;
import com.vasolsim.tclient.element.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
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
		      null,
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToRemoveIcon,
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonDestroyStyleClass,
				      12)
		);

		instance = this;


	}

	public void initListeners()
	{
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

				TeacherClient.questionNode.boundTreeElement = instance;
				TeacherClient.questionNode.redrawNode(true);
			}
		});
	}
}
