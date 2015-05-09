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

package main.java.vasolsim.teacherclient.core;

import main.java.vasolsim.teacherclient.TeacherClient;
import main.java.vasolsim.teacherclient.tree.ExamsTreeElement;
import main.java.vasolsim.common.node.tree.TreeElement;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static main.java.vasolsim.common.GenericUtils.createTreeItem;

/**
 * @author willstuckey
 * @date 11/8/14 <p></p>
 */
public class LeftNode
{
	protected static HBox leftNode;

	static
	{
		HBox leftHorizRoot = new HBox();
		leftHorizRoot.getStyleClass().add("borders");

		VBox leftVertRoot = new VBox();
		leftVertRoot.getStyleClass().add("leftvbox");
		leftVertRoot.setMinWidth(320);
		leftHorizRoot.getChildren().add(leftVertRoot);

		ExamsTreeElement exams = new ExamsTreeElement();

		TeacherClient.examsRoot = createTreeItem(TeacherClient.class, exams, TeacherClient.pathToExamsIcon, 24);
		TreeView<TreeElement> view = new TreeView<TreeElement>(TeacherClient.examsRoot);
		leftVertRoot.getChildren().add(view);
		leftNode = leftHorizRoot;
	}

	public static HBox getLeftNode()
	{
		return leftNode;
	}
}
