package main.java.vasolsim.common.node;

import javafx.scene.Parent;

/**
 * @author willstuckey
 * @date 2/7/15 <p></p>
 */
public interface DrawableParent
{
	public Parent getParent();

	public void redrawParent(boolean apply);
}
