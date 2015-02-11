package com.vasolsim.common.support;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author willstuckey
 * @date 11/12/14 <p></p>
 */
public class SystemInformationNode
{
	protected static Node systemInformationNode;

	static
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label info = new Label("This information is the technical description of the system.");
		info.getStyleClass().add("lbltext");

		HBox spacer = new HBox();
		spacer.setPrefHeight(1);
		spacer.setPrefWidth(2000);
		spacer.getStyleClass().add("lblspacer");

		Label osNameLabel = new Label("OS NAME: " + property("os.name"));
		osNameLabel.getStyleClass().add("lbltext");

		Label osVersionLabel = new Label("OS VERSION: " + property("os.version"));
		osVersionLabel.getStyleClass().add("lbltext");

		Label osArchLabel = new Label("OS ARCH: " + property("os.arch"));
		osArchLabel.getStyleClass().add("lbltext");

		Label osDesktopLabel = new Label("OS WINDOW MANAGER: " + property("sun.desktop"));
		osDesktopLabel.getStyleClass().add("lbltext");

		Label vmMemory = new Label("MEMORY USAGE (used/allocated): "
				                           + (long)((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory())
												/ Math.pow(1024, 2))
				                           + "/" + (long)(Runtime.getRuntime().maxMemory() / Math.pow(1024, 2)));
		vmMemory.getStyleClass().add("lbltext");

		HBox spacerTwo = new HBox();
		spacerTwo.setPrefHeight(1);
		spacerTwo.setPrefWidth(2000);
		spacerTwo.getStyleClass().add("lblspacer");

		Label javaRuntimeName = new Label("JAVA RT: " + property("java.runtime.name"));
		javaRuntimeName.getStyleClass().add("lbltext");

		Label javaRuntimeVersion = new Label("JAVA RT VERSION: " + property("java.runtime.version"));
		javaRuntimeVersion.getStyleClass().add("lbltext");

		Label javaVMVendor = new Label("JAVA VM VENDOR: " + property("java.vm.vendor"));
		javaVMVendor.getStyleClass().add("lbltext");

		Label javaVMVersion = new Label("JAVA VM VERSION: " + property("java.vm.version"));
		javaVMVersion.getStyleClass().add("lbltext");

		Label javaVMSpec = new Label("JAVA VM SPEC: " + property("java.vm.specification.version"));
		javaVMSpec.getStyleClass().add("lbltext");

		HBox spacerThree = new HBox();
		spacerThree.setPrefHeight(1);
		spacerThree.setPrefWidth(2000);
		spacerThree.getStyleClass().add("lblspacer");

		Label javaFXVersion = new Label("JAVA FX VERSION: " + property("javafx.version"));
		javaFXVersion.getStyleClass().add("lbltext");

		Label javaFXRuntime = new Label("JAVA FX RUNTIME: " + property("javafx.runtime.version"));
		javaFXRuntime.getStyleClass().add("lbltext");

		Label javaFXStylesheet = new Label("JAVA FX UA-STYLE: " + property("javafx.userAgentStylesheetUrl"));
		javaFXStylesheet.getStyleClass().add("lbltext");

		verticalRoot.getChildren().addAll(info,
		                                  spacer,
		                                  osNameLabel,
		                                  osVersionLabel,
		                                  osArchLabel,
		                                  osDesktopLabel,
		                                  vmMemory,
		                                  spacerTwo,
		                                  javaRuntimeName,
		                                  javaRuntimeVersion,
		                                  javaVMVendor,
		                                  javaVMVersion,
		                                  javaVMSpec,
		                                  spacerThree,
		                                  javaFXVersion,
		                                  javaFXRuntime,
		                                  javaFXStylesheet);

		SystemInformationNode.systemInformationNode = horizontalRoot;
	}

	private static String property(String key)
	{
		return System.getProperty(key) == null ? "UNK" : System.getProperty(key);
	}

	public static Node getSystemInformationNode()
	{
		return systemInformationNode;
	}
}
