package com.vasolsim.tclient.element.core;

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
