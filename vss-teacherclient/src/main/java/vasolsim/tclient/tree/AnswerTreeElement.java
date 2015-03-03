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

package main.java.vasolsim.tclient.tree;


import main.java.vasolsim.common.file.AnswerChoice;
import main.java.vasolsim.tclient.TeacherClient;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.tclient.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
@Deprecated
public class AnswerTreeElement extends main.java.vasolsim.tclient.tree.TreeElement
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
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonDestroyStyleClass,
				      12)
		);

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
				//cd Vparent.answers.remove(instance);
				parent.treeElementReference.getChildren().remove(treeElementReference);
				CenterNode.getStyledRoot().getChildren().clear();
			}
		});
	}
}
