package com.gmail.guyfleeman.vasolsim.tclient.element.core;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
 */
public class CenterNode
{
    protected static HBox centerRoot;
    protected static VBox styledRoot;
    protected static ScrollPane scrollRoot;

    static
    {
        centerRoot = new HBox();
        centerRoot.setPrefWidth(Double.MAX_VALUE);
        centerRoot.getStyleClass().add("borders");

        styledRoot = new VBox();
        styledRoot.getStyleClass().add("centervbox");
        styledRoot.setPrefHeight(1200);
        styledRoot.setPrefWidth(2000);
        styledRoot.setMinWidth(500);
        centerRoot.getChildren().add(styledRoot);
    }

    public static HBox getCenterRoot()
    {
        return centerRoot;
    }

    public static VBox getStyledRoot()
    {
        return styledRoot;
    }

    public static ScrollPane getScrollRoot()
    {
        return scrollRoot;
    }

    public static void addScrollRoot()
    {
        styledRoot.getChildren().remove(scrollRoot);
        scrollRoot = new ScrollPane();
        scrollRoot.getStyleClass().add("scrollpanebg");
        scrollRoot.setPrefViewportHeight(1200);
        scrollRoot.setPrefViewportWidth(2000);
        scrollRoot.setFitToWidth(true);
        styledRoot.getChildren().add(getScrollRoot());
    }

    public static void removeScrollRoot()
    {
        styledRoot.getChildren().remove(scrollRoot);
    }
}
