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

package main.java.vasolsim.common.node.tree;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;

/**
 * @author WillStuckey
 * @date 7/22/14 <p></p>
 */
public class TreeElement extends HBox
{
	public TreeItem treeElementReference;

	public Button addButton;
	public Button removeButton;
	public Label  label;

	public TreeElement(Label label, Button addButton, Button removeButton)
	{
		super();
		super.setAlignment(Pos.CENTER_LEFT);

		this.addButton = addButton;
		this.removeButton = removeButton;
		this.label = label;

		if (label != null)
		{
			label.getStyleClass().add("paddingtwo");
			getChildren().add(label);
		}

		if (addButton != null)
		{
			addButton.getStyleClass().add("paddingtwo");
			getChildren().add(addButton);
		}

		if (removeButton != null)
		{
			removeButton.getStyleClass().add("paddingtwo");
			getChildren().add(removeButton);
		}
	}
}
