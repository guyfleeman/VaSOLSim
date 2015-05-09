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

import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * @author willstuckey
 * @date 11/8/14 <p></p>
 */
public class BottomNode
{
	protected static Node bottomNode;

	static
	{
		bottomNode = new HBox();
	}

	public static Node getBottomNode()
	{
		return bottomNode;
	}
}
