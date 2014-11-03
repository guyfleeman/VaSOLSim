package com.vasolsim.tclient.element.tree;

import com.vasolsim.common.file.Exam;
import com.vasolsim.common.notification.PopupManager;
import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.common.node.ImageButton;
import com.vasolsim.tclient.element.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class ExamTreeElement extends TreeElement
{
	public Exam exam;
	public Vector<QuestionSetTreeElement> qSets = new Vector<QuestionSetTreeElement>();
	public ExamTreeElement thisInstance;

	public ExamTreeElement()
	{
		super(new Label("New Exam"),
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

		thisInstance = this;
	}

	public void initListeners()
	{
		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				QuestionSetTreeElement newQuestionSet = new QuestionSetTreeElement();
				//newQuestionSet.exam = thisInstance.exam;

				TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
				                                               newQuestionSet,
				                                               TeacherClient.pathToQuestionSetIcon,
				                                               24);
				newQuestionSet.treeElementReference = element;
				newQuestionSet.parent = thisInstance;
				thisInstance.treeElementReference.getChildren().add(element);
				newQuestionSet.initListeners();
				qSets.add(newQuestionSet);

				CenterNode.addScrollRoot();
				TeacherClient.questionSetNode.boundTreeElement = newQuestionSet;
				CenterNode.getScrollRoot().setContent(TeacherClient.questionSetNode.getNode());
			}
		});

		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (PopupManager.askYesNo("You are about to delete this exam. All data will be lost. Continue?"))
				{
					ExamsTreeElement.exams.remove(thisInstance);
					TeacherClient.examsRoot.getChildren().remove(treeElementReference);
					CenterNode.getStyledRoot().getChildren().clear();
				}
			}
		});

		this.thisInstance.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				CenterNode.removeScrollRoot();
				CenterNode.addScrollRoot();
				TeacherClient.examNode.setExam(exam);
				TeacherClient.examNode.boundTreeElement = thisInstance;
				CenterNode.getScrollRoot().setContent(TeacherClient.examNode.getNode());
			}
		});
	}
}
