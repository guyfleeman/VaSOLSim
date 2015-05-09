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


import main.java.vasolsim.common.node.tree.TreeElement;
import main.java.vasolsim.common.support.notification.PopupManager;
import main.java.vasolsim.common.file.QuestionSet;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.teacherclient.core.CenterNode;
import main.java.vasolsim.teacherclient.TeacherClient;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

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

	public static Logger logger = Logger.getLogger(QuestionSetTreeElement.class.getName());

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
				logger.info("question creation invoked");
				TeacherClient.questionInitNode.parent = instance;

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
					logger.info("question set removal invoked -> " + qSet.getName());
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
				logger.info("display question set info -> " + qSet.getName());
				CenterNode.addScrollRoot();
				TeacherClient.questionSetNode.boundTreeElement = instance;
				TeacherClient.questionSetNode.qSet = instance.qSet;
				CenterNode.getScrollRoot().setContent(TeacherClient.questionSetNode.getNode());
			}
		});
	}
}
