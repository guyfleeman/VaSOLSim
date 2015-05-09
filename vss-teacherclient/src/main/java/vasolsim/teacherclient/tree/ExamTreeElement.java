/*
 * Copyright (c) 2015.
 *
 *     This file is part of VaSOLSim.
 *
 *     VaSOLSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     VaSOLSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with VaSOLSim.  If not, see <http://www.gnu.org/licenses/>.
 */

package main.java.vasolsim.teacherclient.tree;

import javafx.scene.control.TreeItem;
import main.java.vasolsim.common.GenericUtils;
import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.support.notification.PopupManager;
import main.java.vasolsim.teacherclient.TeacherClient;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.common.node.tree.TreeElement;
import main.java.vasolsim.teacherclient.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class ExamTreeElement extends TreeElement
{
	public Exam exam;
	public Vector<QuestionSetTreeElement> qSets = new Vector<QuestionSetTreeElement>();
	public ExamTreeElement thisInstance;

	public static Logger logger = Logger.getLogger(ExamTreeElement.class.getName());

	public ExamTreeElement()
	{
		super(new Label("New Exam"),
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

		thisInstance = this;
	}

	public void initListeners()
	{
		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("question set creation invoked");
				QuestionSetTreeElement newQuestionSet = new QuestionSetTreeElement();

				TreeItem<TreeElement> element = GenericUtils.createTreeItem(TeacherClient.class,
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
				logger.info("exam removal invoked -> " + exam.getTestName());
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
				logger.info("display exam info -> " + exam.getTestName());
				CenterNode.addScrollRoot();
				TeacherClient.examNode.setExam(exam);
				TeacherClient.examNode.boundTreeElement = thisInstance;
				CenterNode.getScrollRoot().setContent(TeacherClient.examNode.getNode());
			}
		});
	}
}
