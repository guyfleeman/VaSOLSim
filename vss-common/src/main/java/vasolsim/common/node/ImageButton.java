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

package main.java.vasolsim.common.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p>This class wraps Button to provide functionality with images instead of text. Supports hover rsc.style. </p>
 */
public class ImageButton extends Button
{
	/**
	 * Default constructor.
	 * @param loader the class relative to which the image will be loaded
	 * @param imageInternalURL the location of the image relative to the loader
	 * @param defaultCSSClass the default css class
	 * @param hoverCSSClass the css class used when the image button is hovered over
	 * @param dim height of the image
	 */
	public ImageButton(@Nonnull Class loader,
	                   @Nonnull String imageInternalURL,
	                   @Nullable final String defaultCSSClass,
	                   @Nullable final String hoverCSSClass,
	                   int dim)
	{
		this(loader,
		     imageInternalURL,
		     defaultCSSClass,
		     hoverCSSClass,
		     dim,
		     dim);
	}

	/**
	 * Default constructor.
	 * @param loader the class relative to which the image will be loaded
	 * @param imageInternalURL the location of the image relative to the loader
	 * @param defaultCSSClass the default css class
	 * @param hoverCSSClass the css class used when the image button is hovered over
	 * @param height height of the image
	 * @param width width of the image
	 */
	public ImageButton(@Nonnull Class loader,
	                   @Nonnull String imageInternalURL,
	                   @Nullable final String defaultCSSClass,
	                   @Nullable final String hoverCSSClass,
	                   int height,
	                   int width)
	{
		ImageView img = new ImageView(new Image(loader.getClass().getResourceAsStream(imageInternalURL)));
		img.setFitHeight(height);
		img.setFitWidth(width);
		setGraphic(img);

		getStyleClass().add(defaultCSSClass);

		setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				getStyleClass().remove(defaultCSSClass);
				getStyleClass().add(hoverCSSClass);
			}
		});

		setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				getStyleClass().remove(hoverCSSClass);
				getStyleClass().add(defaultCSSClass);
			}
		});
	}
}
