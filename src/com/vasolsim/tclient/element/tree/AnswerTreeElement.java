package com.vasolsim.tclient.element.tree;

import com.vasolsim.common.file.AnswerChoice;
import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.common.node.ImageButton;
import com.vasolsim.tclient.element.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class AnswerTreeElement extends TreeElement
{
	public QuestionTreeElement parent;
	public AnswerTreeElement   instance;
	public AnswerChoice        answerChoice;

	public AnswerTreeElement()
	{
		this(false);
	}

	public AnswerTreeElement(boolean correct)
	{
		super(new Label("New Answer"),
		      null,
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToRemoveIcon,
				      "btndenyhover",
				      "btnnormal",
				      12)
		);

		//setCorrectIcon(correct);

		instance = this;
	}

	public void setCorrectIcon(boolean correct)
	{
		ImageView icon = new ImageView(TeacherClient.class.getResource(correct
		                                                               ? TeacherClient.pathToCorrectAnswerIcon
		                                                               : TeacherClient.pathToIncorrectAnswerIcon)
		                                                  .toExternalForm());
		icon.setFitWidth(24);
		icon.setFitHeight(24);

		treeElementReference.setGraphic(icon);
	}

	public void initListeners()
	{
		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				parent.answers.remove(instance);
				parent.treeElementReference.getChildren().remove(treeElementReference);
				CenterNode.getStyledRoot().getChildren().clear();
			}
		});
	}
}
