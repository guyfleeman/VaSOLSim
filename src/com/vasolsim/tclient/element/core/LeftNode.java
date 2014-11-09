package com.vasolsim.tclient.element.core;

import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.tclient.element.tree.ExamsTreeElement;
import com.vasolsim.tclient.element.tree.TreeElement;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.vasolsim.common.GenericUtils.createTreeItem;

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
