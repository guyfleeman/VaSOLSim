package main.java.vasolsim.tclient.tree;

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
