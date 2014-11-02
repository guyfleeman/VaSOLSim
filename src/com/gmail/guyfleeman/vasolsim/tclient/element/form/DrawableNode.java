package com.gmail.guyfleeman.vasolsim.tclient.element.form;

import javafx.scene.Node;

/**
 * @author willstuckey
 * @date 11/1/14 <p></p>
 */
public interface DrawableNode
{
	public Node getNode();

	public void redrawNode(boolean apply);
}
