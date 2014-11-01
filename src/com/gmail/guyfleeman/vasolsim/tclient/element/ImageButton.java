package com.gmail.guyfleeman.vasolsim.tclient.element;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
 */
public class ImageButton extends Button
{
    public ImageButton(Class loader,
                       String imageInternalURL,
                       final String hoverCSSClass,
                       final String noHoverCSSClass,
                       int dim)
    {
        ImageView img = new ImageView(new Image(loader.getClass().getResourceAsStream(imageInternalURL)));
        img.setFitHeight(dim);
        img.setFitWidth(dim);
        setGraphic(img);

        getStyleClass().add(noHoverCSSClass);

        setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                getStyleClass().remove(noHoverCSSClass);
                getStyleClass().add(hoverCSSClass);
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                getStyleClass().remove(hoverCSSClass);
                getStyleClass().add(noHoverCSSClass);
            }
        });
    }
}
