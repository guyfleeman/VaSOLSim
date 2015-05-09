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

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class CenterNode
{
	protected static HBox       centerRoot;
	protected static VBox       styledRoot;
	protected static ScrollPane scrollRoot;

	static
	{
		centerRoot = new HBox();
		centerRoot.setPrefWidth(Double.MAX_VALUE);
		centerRoot.getStyleClass().add("borders");

		styledRoot = new VBox(20);
		styledRoot.getStyleClass().add("centervbox");
		styledRoot.setPrefHeight(1200);
		styledRoot.setPrefWidth(2000);
		styledRoot.setMinWidth(300);
		centerRoot.getChildren().add(styledRoot);
	}

	public static HBox getCenterRoot()
	{
		return centerRoot;
	}

	public static VBox getStyledRoot()
	{
		return styledRoot;
	}

	public static ScrollPane getScrollRoot()
	{
		return scrollRoot;
	}

	public static void addScrollRoot()
	{
		styledRoot.getChildren().remove(scrollRoot);
		scrollRoot = new ScrollPane();
		scrollRoot.getStyleClass().add("scrollpanebg");
		scrollRoot.setPrefViewportHeight(1200);
		scrollRoot.setPrefViewportWidth(2000);
		scrollRoot.setFitToWidth(true);
		scrollRoot.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		styledRoot.getChildren().add(scrollRoot);
	}

	public static void removeScrollRoot()
	{
		styledRoot.getChildren().remove(scrollRoot);
	}
}
