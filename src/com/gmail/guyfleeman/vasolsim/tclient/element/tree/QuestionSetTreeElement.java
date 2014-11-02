package com.gmail.guyfleeman.vasolsim.tclient.element.tree;


import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.common.struct.QuestionSet;
import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.ImageButton;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA.QuestionInitNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.QuestionSetInfoNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.QandA.QuestionTypeNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
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
				QuestionInitNode.parent = instance;

				CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(QuestionTypeNode.getQuestionTypeNode());
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
				TeacherClient.questionSetInfoNode.boundTreeElement = instance;
				TeacherClient.questionSetInfoNode.qSet = instance.qSet;
				CenterNode.getScrollRoot().setContent(TeacherClient.questionSetInfoNode.getNode());
			}
		});
	}
}
