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

package main.java.vasolsim.common;

import main.java.vasolsim.common.node.DraggablePane;
import main.java.vasolsim.common.node.StringPane;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * @author willstuckey
 * @date 11/20/14 <p></p>
 */
public class EventHandlers
{
	/**
	 *
	 * @param localSpace
	 * @param parent
	 * @param source
	 * @param draggablePane
	 */
	private void initDragRoutine(final Node localSpace,
	                             final Pane parent,
	                             final Node source,
	                             final DraggablePane draggablePane)
	{
		initDragRoutine(localSpace,
		                parent,
		                source,
		                draggablePane,
		                false,
		                null,
		                null);
	}

	/**
	 *
	 * @param localSpace the root of the scene
	 * @param parent the Node to which the draggable pane will be a child
	 * @param source the Node out of which the draggable pane will be created
	 * @param draggablePane the draggable pane that will follow the cursor during the drag routine
	 * @param replaceSource if the source will be replaced once it had been dragged away. e.g. the draggable pane
	 *                      assumes the identity of the source, once this has taken place shall the source be replaced?
	 *                      If it is not, is becomes a "spawner" allowing many draggable panes to assume its identity,
	 *                      ig it is replaced, it's entire lifespan in the drag cycle will be carried by the draggable
	 *                      pane
	 * @param replaceWith the node replacing the source node
	 * @param replaceFrom the node in which the source resides
	 */
	private void initDragRoutine(final Node localSpace,
	                             final Pane parent,
	                             final Node source,
	                             final DraggablePane draggablePane,
	                             final boolean replaceSource,
	                             final Node replaceWith,
	                             final Pane replaceFrom)
	{
		/*
		 * add the draggable pane and create its text
		 */
		source.setOnDragDetected(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{

				//update content
				if (mouseEvent.getSource() instanceof DraggablePane)
					draggablePane.updateContentFailStop(((DraggablePane) mouseEvent.getSource()).getContentFailStop());

				//add to scene and start drag
				parent.getChildren().add(draggablePane);
				draggablePane.startFullDrag();

				/*
				 * hide and de-manage source, replace later (replacing immediately dereferences the event handler in
				 * JavaFX 8.0+ causing a freezing behavior, the reference will be removed in the "mouse released"
				 * handler)
				 */
				if (replaceSource && replaceFrom != null)
					for (int index = 0; index < replaceFrom.getChildren().size(); index++)
						if (replaceFrom.getChildren().get(index) == source)
						{
							replaceFrom.getChildren().get(index).setManaged(false);
							replaceFrom.getChildren().get(index).setVisible(false);
							replaceFrom.getChildren().add(index + 1, replaceWith);
						}

				mouseEvent.consume();
			}
		});

		/*
		 * update the position
		 */
		source.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{

				//use translation rather than location, relocate is buggy in JavaFX 8.0+
				Point2D localPoint = localSpace.sceneToLocal(
						new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
				draggablePane.setTranslateX(localPoint.getX() -
						                            draggablePane.getLayoutX() -
						                            draggablePane.getBoundsInLocal().getWidth() / 2);
				draggablePane.setTranslateY(localPoint.getY() -
						                            draggablePane.getLayoutY() -
						                            draggablePane.getBoundsInLocal().getHeight() / 2);

				source.setCursor(Cursor.NONE);
				mouseEvent.consume();
			}
		});

		/*
		 * adjust cursor on entry
		 */
		source.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				source.setCursor(Cursor.HAND);
				mouseEvent.consume();
			}
		});

		/*
		 * adjust cursor on pressed
		 */
		source.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				//prevent mouse drag references to the the source and pane
				source.setMouseTransparent(true);
				draggablePane.setMouseTransparent(true);

				//adjust cursor
				source.setCursor(Cursor.CLOSED_HAND);

				mouseEvent.consume();
			}
		});

		/*
		 * adjust cursor on released
		 */
		source.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent mouseEvent)
			{
				//restore mouse drag references to the source and pane
				source.setMouseTransparent(false);
				draggablePane.setMouseTransparent(false);

				//update cursor
				source.setCursor(Cursor.DEFAULT);

				//remove pane
				parent.getChildren().remove(draggablePane);

				/*
				 * if source replacement was selected, it has already been hidden and de-managed by the onDragStarted
				 * handler. Now that all event handlers are unreachable (source to be destroyed) we can finally do it.
				 * I am still unsure as to the fate of teh mouseEvent consumption call after this block, regardless, its
				 * execution seems to do nothing, but I will leave it for convention.
				 */
				if (replaceSource && replaceFrom != null)
					for (int index = 0; index < replaceFrom.getChildren().size(); index++)
						if (replaceFrom.getChildren().get(index) == source)
							replaceFrom.getChildren().remove(index);

				mouseEvent.consume();
			}
		});
	}

	private void initDragTargetRoutine(final Node localSpace,
	                                   final Pane parent,
	                                   final Node target,
	                                   final DraggablePane draggablePane)
	{
		target.setOnMouseDragEntered(new EventHandler<MouseDragEvent>()
		{
			public void handle(MouseDragEvent e)
			{

				target.getStyleClass().clear();
				target.getStyleClass().add("grammarSpaceActive");
				e.consume();
			}
		});

		target.setOnMouseDragExited(new EventHandler<MouseDragEvent>()
		{
			public void handle(MouseDragEvent e)
			{

				target.getStyleClass().clear();

				if (target instanceof StringPane)
				{
					target.getStyleClass().add("charPaneDefaultSuper");
					target.getStyleClass().remove("grammarSpaceActive");
				}
				else
					target.getStyleClass().add("grammarSpace");

				e.consume();
			}
		});

		target.setOnMouseDragReleased(new EventHandler<MouseDragEvent>()
		{
			public void handle(MouseDragEvent e)
			{

				System.out.println("target drag released");
				if (e.getGestureSource() instanceof StringPane)
					for (int index = 0; index < parent.getChildren().size(); index++)
						if (parent.getChildren().get(index) == e.getTarget())
							if (e.getTarget() instanceof StringPane)
								((StringPane) e.getSource()).setOverlay(
										((StringPane) e.getGestureSource()).getOverlay());
							else
							{
								StringPane sp = new StringPane(((StringPane) e.getGestureSource()).getOverlay(),
								                               "charPaneDefaultSuper",
								                               "charPaneDefault",
								                               null,
								                               "charPaneSmallText",
								                               40,
								                               40,
								                               3);

								parent.getChildren().set(index, sp);

								StackPane rect = new StackPane();
								rect.setMinSize(40, 40);
								rect.setMaxSize(40, 40);
								rect.getStyleClass().add("grammarSpace");
								initDragTargetRoutine(localSpace,
								                      parent,
								                      rect,
								                      draggablePane);
								initDragRoutine(localSpace,
								                parent,
								                sp,
								                draggablePane,
								                true,
								                rect,
								                parent);

								initDragTargetRoutine(localSpace,
								                      parent,
								                      sp,
								                      draggablePane);
							}

				e.consume();
			}
		});
	}
}
