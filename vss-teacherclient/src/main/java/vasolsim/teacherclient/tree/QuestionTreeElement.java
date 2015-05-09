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

import main.java.vasolsim.common.file.Question;
import main.java.vasolsim.common.node.tree.TreeElement;
import main.java.vasolsim.teacherclient.TeacherClient;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.teacherclient.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class QuestionTreeElement extends TreeElement
{
	public QuestionSetTreeElement parent;
	public QuestionTreeElement    instance;
	public Question                  question = new Question();

	public static Logger logger = Logger.getLogger(QuestionTreeElement.class.getName());

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
				logger.info("question removal invoked -> " + question.getName());
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
				logger.info("display question info -> " + question.getName());
				CenterNode.addScrollRoot();

				TeacherClient.questionNode.boundTreeElement = instance;
				TeacherClient.questionNode.redrawNode(true);
			}
		});
	}
}
