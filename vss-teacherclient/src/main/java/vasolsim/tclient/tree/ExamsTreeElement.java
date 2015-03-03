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

import main.java.vasolsim.tclient.tree.TreeElement;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.tclient.TeacherClient;
import main.java.vasolsim.tclient.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class ExamsTreeElement extends TreeElement
{
	public static Vector<ExamTreeElement> exams = new Vector<ExamTreeElement>();
	public static Logger logger = Logger.getLogger(ExamsTreeElement.class.getName());

	public ExamsTreeElement()
	{
		super(new Label("Exams"),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToAddIcon,
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonCreateStyleClass,
				      12),
		      null);

		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("exam creation invoked");
				CenterNode.getStyledRoot().getChildren().clear();
				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(TeacherClient.examInitNode.getNode());
			}
		});
	}
}
