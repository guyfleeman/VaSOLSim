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

package main.java.vasolsim.tclient.core;

import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.tclient.TeacherClient;
import main.java.vasolsim.tclient.form.ExamExportNode;
import main.java.vasolsim.common.support.SystemInformationNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

import java.io.File;

/**
 * @author willstuckey
 * @date 11/8/14
 * <p></p>
 */
public class MenuNode
{
	protected static MenuBar menuNode;

	static
	{
		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("File");
		MenuItem saveMenuItem = new MenuItem("Save");
		saveMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		saveMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				System.out.println("TODO: save");
			}
		});

		MenuItem saveAsMenuItem = new MenuItem("Save As");
		saveAsMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				System.out.println("TODO: save as");
			}
		});

		MenuItem importLocalMenuItem = new MenuItem("Import -> Local Exam");
		importLocalMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
		importLocalMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				System.out.println("TODO: handle import");
			}
		});

		MenuItem exportLocalMenuItem = new MenuItem("Export -> Local Exam");
		exportLocalMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
		exportLocalMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				CenterNode.addScrollRoot();
				ExamExportNode.exportRaw = true;
				TeacherClient.examExportNode.redrawNode(true);
				//CenterNode.getScrollRoot().setContent(TeacherClient.examExportNode.getNode());
			}
		});

		MenuItem exportProdMenuItem  = new MenuItem("Export -> Student Exam");
		exportProdMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+E"));
		exportProdMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				CenterNode.addScrollRoot();
				ExamExportNode.exportRaw = false;
				TeacherClient.examExportNode.redrawNode(true);
				//CenterNode.getScrollRoot().setContent(TeacherClient.examExportNode.getNode());
			}
		});

		MenuItem recoverProdMenuItem = new MenuItem("Recover -> Student Exam");

		fileMenu.getItems().addAll(saveMenuItem,
		                           saveAsMenuItem,
		                           new SeparatorMenuItem(),
		                           importLocalMenuItem,
								   exportLocalMenuItem,
		                           exportProdMenuItem,
		                           new SeparatorMenuItem(),
		                           recoverProdMenuItem);

		Menu systemMenu = new Menu("View");
		MenuItem fullscreenMenuItem = new MenuItem("Fullscreen");
		fullscreenMenuItem.setAccelerator(KeyCombination.valueOf("f11"));
		fullscreenMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				TeacherClient.stage.setFullScreen(!TeacherClient.stage.isFullScreen());
			}
		});

		MenuItem exitApplicationMenuItem = new MenuItem("Exit VaSOLSim");
		exitApplicationMenuItem.setAccelerator(KeyCombination.valueOf("Esc"));
		exitApplicationMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				TeacherClient.stage.close();
			}
		});

		MenuItem clearPersistence = new MenuItem("Clear Persistence");
		systemMenu.getItems().addAll(fullscreenMenuItem,
		                             exitApplicationMenuItem,
		                             new SeparatorMenuItem(),
		                             clearPersistence);
		clearPersistence.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (PopupManager.askYesNo("Are you sure you would like to delete persistent data? This will close " +
						                          "the program. (you should manually save exams before proceeding)"))
				{
					TeacherClient.clearPersistenceFlagged = true;
					TeacherClient.stage.close();
				}
			}
		});

		Menu statisticsMenu = new Menu("Statistics");
		MenuItem fetchAllStatisticsMenuItem = new MenuItem("Fetch All Statistics");
		fetchAllStatisticsMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
		fetchAllStatisticsMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				System.out.println("TODO: handle statistics");
			}
		});

		MenuItem fetchTestStatistics = new MenuItem("Fetch Statistics");
		fetchTestStatistics.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+F"));
		fetchTestStatistics.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				System.out.println("TODO: handle statistics");
			}
		});

		statisticsMenu.getItems().addAll(fetchAllStatisticsMenuItem,
		                                 fetchTestStatistics);

		Menu helpMenu = new Menu("Help");
		MenuItem onlineHelpMenuItem = new MenuItem("View Online Help");

		MenuItem onlineDocumentationMenuItem = new MenuItem("View Online Documentation\n(Technical)");

		MenuItem licenseMenuItem = new MenuItem("License");

		MenuItem sourcesMenuItem = new MenuItem("Sources");

		MenuItem systemInfoMenuItem = new MenuItem("System Information");
		systemInfoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
		systemInfoMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(SystemInformationNode.getSystemInformationNode());
			}
		});

		MenuItem debugConsoleMenuItem = new MenuItem("Toggle Debug Window");
		debugConsoleMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
		debugConsoleMenuItem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				TeacherClient.debugWindow.toggle();
			}
		});

		helpMenu.getItems().addAll(onlineHelpMenuItem,
		                           onlineDocumentationMenuItem,
		                           new SeparatorMenuItem(),
		                           licenseMenuItem,
		                           sourcesMenuItem,
		                           new SeparatorMenuItem(),
		                           systemInfoMenuItem,
		                           debugConsoleMenuItem);

		menuBar.getMenus().addAll(fileMenu, systemMenu, statisticsMenu, helpMenu);
		menuNode = menuBar;
	}

	public static MenuBar getMenuNode()
	{
		return menuNode;
	}
}
