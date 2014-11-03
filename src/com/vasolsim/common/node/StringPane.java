package com.vasolsim.common.node;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * @author willstuckey
 * @date 11/2/14 <p></p>
 */
public class StringPane extends StackPane
{
	protected BooleanProperty active = new SimpleBooleanProperty(false);

	/**
	 * creates a layered pane to allow internal and external border styles and sizes
	 * @param overlay the string in the box
	 */
	public StringPane(String overlay)
	{
		this(overlay,
		     "charPaneDefaultSuper",
		     "charPaneDefault",
		     "charPaneDefaultActive",
		     "charPaneDefaultText",
		     60,
		     60,
		     3);
	}

	/**
	 * creates a layered pane to allow internal and external border styles and sizes
	 * @param overlay the string in the box
	 * @param styleClass the style formatting the external border (transparent bg and some other style)
	 * @param subStyleClass the style formatting the internal pane
	 * @param subStyleActiveClass the style formatting the internal pane when it is active (does not layer with
	 *                               subStyleClass)
	 * @param textStyleClass the style formatting the text
	 * @param width the width of the box
	 * @param height the height of the box
	 * @param inset the inset between the external and internal panes
	 */
	public StringPane(final String overlay,
	                  final String styleClass,
	                  final String subStyleClass,
	                  final String subStyleActiveClass,
	                  final String textStyleClass,
	                  int width,
	                  int height,
	                  int inset)
	{
		this.setPrefWidth(width);
		this.setPrefHeight(height);
		this.setMaxWidth(width);
		this.setMaxHeight(height);
		this.setAlignment(Pos.CENTER);
		this.getStyleClass().add(styleClass);

		/*
		 * Java FX <=8.0 bug prevents style changes (removal) when that style is hovered over. This means the style
		 * changes on click bot doesn't on "de-click" or toggle. Therefore, I created two copies of everything! Scratch
		 * that, I created ALL THE COPIES!!! (also it it very late, and now that I have succeeded I will sleep through
		 * my CS lecture tomorrow).
		 */
		final StackPane subActive = new StackPane();
		subActive.setPrefWidth(width - inset * 2);
		subActive.setPrefHeight(height - inset * 2);
		subActive.setMaxWidth(width - inset * 2);
		subActive.setMaxHeight(height - inset * 2);
		subActive.getStyleClass().add(subStyleActiveClass);

		final StackPane subInactive = new StackPane();
		subInactive.setPrefWidth(width - inset * 2);
		subInactive.setPrefHeight(height - inset * 2);
		subInactive.setMaxWidth(width - inset * 2);
		subInactive.setMaxHeight(height - inset * 2);
		subInactive.getStyleClass().add(subStyleClass);

		final Label textActive = new Label(overlay);
		textActive.getStyleClass().add(textStyleClass);
		final Label textInactive = new Label(overlay);
		textInactive.getStyleClass().add(textStyleClass);

		subActive.getChildren().add(textActive);
		subActive.setAlignment(Pos.CENTER);
		subInactive.getChildren().add(textInactive);
		subInactive.setAlignment(Pos.CENTER);

		this.getChildren().add(subInactive);

		/*
		 * swap different styled copies
		 */
		super.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{

				active.setValue(!active.getValue());
//				getChildren().clear();
//
//				if (active.get())
//					getChildren().add(subActive);
//				else
//					getChildren().add(subInactive);
			}
		});

		active.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
			                    Boolean newValue)
			{
				getChildren().clear();

				if (newValue)
					getChildren().add(subActive);
				else
					getChildren().add(subInactive);
			}
		});
	}

	public BooleanProperty getActiveProperty()
	{
		return active;
	}
}
