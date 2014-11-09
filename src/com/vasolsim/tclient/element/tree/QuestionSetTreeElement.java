package com.vasolsim.tclient.element.tree;


import com.vasolsim.common.notification.PopupManager;
import com.vasolsim.common.file.QuestionSet;
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
public class QuestionSetTreeElement extends TreeElement
{
	public ExamTreeElement        parent;
	public QuestionSetTreeElement instance;
	public QuestionSet                 qSet      = new QuestionSet();
	public Vector<QuestionTreeElement> questions = new Vector<QuestionTreeElement>();

	public QuestionSetTreeElement()
	{
		super(new Label("New QSet"),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToAddIcon,
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonCreateStyleClass,
				      12),
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
		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				TeacherClient.questionInitNode.parent = instance;

				//CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(TeacherClient.questionTypeNode.getNode());
			}
		});

		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (PopupManager.askYesNo("You are about to delete this QandA set. All data will be lost. Continue?"))
				{
					parent.qSets.remove(instance);
					parent.treeElementReference.getChildren().remove(treeElementReference);
					CenterNode.getStyledRoot().getChildren().clear();
				}
			}
		});

		this.instance.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();
				TeacherClient.questionSetNode.boundTreeElement = instance;
				TeacherClient.questionSetNode.qSet = instance.qSet;
				CenterNode.getScrollRoot().setContent(TeacherClient.questionSetNode.getNode());
			}
		});
	}
}
