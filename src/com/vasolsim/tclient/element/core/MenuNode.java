package com.vasolsim.tclient.element.core;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/**
 * @author willstuckey
 * @date 11/8/14 <p></p>
 */
public class MenuNode
{
	protected static MenuBar menuNode;

	static
	{
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		Menu helpMenu = new Menu("Help");
		menuBar.getMenus().addAll(fileMenu, helpMenu);
		menuNode = menuBar;
	}

	public static MenuBar getMenuNode()
	{
		return menuNode;
	}
}
