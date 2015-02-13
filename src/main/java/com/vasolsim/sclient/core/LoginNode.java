package com.vasolsim.sclient.core;

import com.vasolsim.common.node.DrawableParent;
import com.vasolsim.sclient.StudentClient;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public class LoginNode implements DrawableParent
{
	protected Parent loginNode;

	public LoginNode()
	{
		redrawParent(false);
	}

	public void redrawParent(boolean apply)
	{
		HBox horizontalRoot = new HBox();

		VBox left = new VBox();
		left.setMinWidth(480);
		left.setPrefWidth(StudentClient.screenSize.getWidth());


		Separator lrSeparator = new Separator();
		lrSeparator.setOrientation(Orientation.VERTICAL);
		lrSeparator.setPrefHeight(2000);
		lrSeparator.setStyle("verticalSeparator");


		VBox right = new VBox();


	}

	public Parent getParent()
	{
		return loginNode;
	}
}
