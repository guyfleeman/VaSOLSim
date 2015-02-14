package main.java.vasolsim.common.node;

import main.java.vasolsim.common.VaSolSimException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author willstuckey
 * @date 11/20/14
 * <p>This class is a generic, updatable, wrapper for draggable nodes. It is designed to be compliant with hover-over
 * rsc.style switching for both JavaFX <=2.2 and >=8.0. It also provides an interface for updating internal Nodes during
 * drag routines while supporting user defined type migrations if needed on update.</p>
 */
public class DraggablePane<E extends Node> extends StackPane
{
	public static int defaultHeight = 60;
	public static int defaultWidth  = 60;
	public static int defaultInset  = 3;

	protected BooleanProperty active = new SimpleBooleanProperty(false);
	protected Class<E> classDef;
	protected Method   updateMethod;
	protected Method   contentMethod;
	protected E        overlay;
	protected E        activeOverlay;
	protected E        inactiveOverlay;

	/**
	 * @param activeOverlay the active overlay
	 * @param inactiveOverlay the inactive overlay
	 * @param classDef the template class type
	 * @param updateCallName the name of the method used by the update call
	 * @throws VaSolSimException if updateCallName is not a declared method of classDef
	 */
	public DraggablePane(E activeOverlay,
	                     E inactiveOverlay,
	                     Class<E> classDef,
	                     String updateCallName,
	                     String contentCallName) throws VaSolSimException
	{
		this(activeOverlay,
		     inactiveOverlay,
		     classDef,
		     updateCallName,
		     contentCallName,
		     defaultWidth,
		     defaultHeight,
		     defaultInset);
	}

	/**
	 * @param activeOverlay the active overlay
	 * @param inactiveOverlay the inactive overlay
	 * @param classDef the template class type
	 * @param updateCallName the name of the method used by the update call
	 * @param width the width of the pane
	 * @param height the height of the pane
	 * @param inset the padding between the parent pane and its children
	 * @throws VaSolSimException if updateCallName is not a declared method of classDef
	 */
	public DraggablePane(E activeOverlay,
	                     E inactiveOverlay,
	                     Class<E> classDef,
	                     String updateCallName,
	                     String contentCallName,
	                     int width,
	                     int height,
	                     int inset) throws VaSolSimException
	{
		this(activeOverlay,
		     inactiveOverlay,
		     classDef,
		     updateCallName,
		     contentCallName,
		     "charPaneDefaultSuper",
		     "charPaneDefault",
		     "charPaneDefaultActive",
		     width,
		     height,
		     inset);
	}

	/**
	 *
	 * @param activeOverlay the active overlay
	 * @param inactiveOverlay the inactive overlay
	 * @param classDef the template class type
	 * @param updateCallName the name of the method used by the update call
	 * @param styleClass the rsc.style class of the parent
	 * @param subStyleActiveClass the rsc.style class of the active pane's child
	 * @param subStyleInactiveClass the rsc.style class of the inactive pane's child
	 * @param width the width of the pane
	 * @param height the height of the pane
	 * @param inset the padding between the parent pane and its children
	 * @throws VaSolSimException if updateCallName is not a declared method of classDef
	 */
	public DraggablePane(E activeOverlay,
	                     E inactiveOverlay,
	                     Class<E> classDef,
	                     String updateCallName,
	                     String contentCallName,
	                     String styleClass,
	                     String subStyleActiveClass,
	                     String subStyleInactiveClass,
	                     int width,
	                     int height,
	                     int inset) throws VaSolSimException
	{
		/*
		 * initialize the parent
		 */
		this.setPrefWidth(width);
		this.setPrefHeight(height);
		this.setMaxWidth(width);
		this.setMaxHeight(height);
		this.setAlignment(Pos.CENTER);
		this.getStyleClass().add(styleClass);

		/*
		 * set fields
		 */
		this.activeOverlay = activeOverlay;
		this.inactiveOverlay = inactiveOverlay;
		this.classDef = classDef;

		/*
		 * check for and assign the reflective calls
		 */
		for (Method m : classDef.getMethods())
		{
			if (m.getName().equals(updateCallName))
			{
				updateMethod = m;
				updateMethod.setAccessible(true);
			}

			if (m.getName().equals(contentCallName))
			{
				contentMethod = m;
				contentMethod.setAccessible(true);
			}
		}

		if (this.updateMethod == null)
			throw new VaSolSimException(
					"given update call does not exist as a declared member of the instantiated type");

		if (this.contentMethod == null)
			throw new VaSolSimException(
					"given content call does not exist as a declared member of the instantiated type");


		/*
		 * Java FX <=8.0 bug prevents rsc.style changes (removal) when that rsc.style is hovered over. This means the rsc.style
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
		subActive.setPickOnBounds(false);
		subActive.setMouseTransparent(true);

		final StackPane subInactive = new StackPane();
		subInactive.setPrefWidth(width - inset * 2);
		subInactive.setPrefHeight(height - inset * 2);
		subInactive.setMaxWidth(width - inset * 2);
		subInactive.setMaxHeight(height - inset * 2);
		subInactive.getStyleClass().add(subStyleInactiveClass);
		subInactive.setPickOnBounds(false);
		subInactive.setMouseTransparent(true);

		subActive.getChildren().add(activeOverlay);
		subActive.setAlignment(Pos.CENTER);
		subInactive.getChildren().add(inactiveOverlay);
		subInactive.setAlignment(Pos.CENTER);

		this.getChildren().add(subInactive);

		/*
		 * toggle state
		 */
		super.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				active.setValue(!active.getValue());
			}
		});

		/*
		 * on state toggle, swap panes. Separated from mouse event so external listeners can function as well
		 */
		active.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue,
			                    Boolean oldValue,
			                    Boolean newValue)
			{
				getChildren().clear();

				if (newValue)
				{
					getChildren().add(subActive);
				}
				else
				{
					getChildren().add(subInactive);
				}
			}
		});
	}

	/**
	 * the active state is bound by BooleanProperty and is accessible to attach listeners
	 *
	 * @return active property
	 */
	public BooleanProperty getActiveProperty()
	{
		return active;
	}

	/**
	 * gets the class of the DraggablePane instance
	 * @return the class of the DraggablePane instance
	 */
	public Class getType()
	{
		return classDef;
	}

	/**
	 * gets the expected arg count
	 * @return arg count
	 */
	public int getUpdateArgCount()
	{
		return updateMethod.getGenericParameterTypes().length;
	}

	/**
	 * gets the arg types
	 * @return arg types
	 */
	public Type[] getUpdateArgs()
	{
		return updateMethod.getGenericParameterTypes();
	}

	/**
	 * gets the method used in the update call
	 * @return the method target
	 */
	public Method getUpdateInvocationTarget()
	{
		return updateMethod;
	}

	/**
	 * gets the content of the active pane (content across panes should be the same)
	 * @return content
	 * @throws VaSolSimException
	 */
	public Object getContent() throws VaSolSimException
	{
		try
		{
			return updateMethod.invoke(activeOverlay);
		}
		catch (IllegalAccessException e)
		{
			throw new VaSolSimException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new VaSolSimException(e);
		}
	}

	/**
	 * provides the same functionality as getContent() but is guaranteed to not error out, it will stop on error
	 * @return content
	 */
	public Object getContentFailStop()
	{
		try
		{
			return getContent();
		}
		catch (VaSolSimException e)
		{
			return new VoidReturn();
		}
	}

	/**
	 * updates the active and inactive pane content
	 * @param updateCallArgs the arguments for the update calls
	 * @return the return of the update call, VoidReturn if the call errors out (distinguish errors from null returns
	 * in fail-stop calls)
	 * @throws VaSolSimException if the call fails
	 */
	public Object updateContent(Object... updateCallArgs) throws VaSolSimException
	{
		try
		{
			Object[] returns = new Object[2];
			returns[0] = updateMethod.invoke(activeOverlay, updateTypeMigrationWrapper(updateCallArgs));
			returns[1] = updateMethod.invoke(inactiveOverlay, updateTypeMigrationWrapper(updateCallArgs));
			return returns;
		}
		catch (IllegalAccessException e)
		{
			throw new VaSolSimException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new VaSolSimException(e);
		}
	}

	/**
	 * provides the same functionality as updateContent() but is guaranteed to not error out, it will stop on error
	 * @param updateCallArgs the arguments for the update calls
	 * @return the return of the update call, VoidReturn if the call errors out (distinguish errors from null returns
	 * in fail-stop calls)
	 */
	public Object updateContentFailStop(Object... updateCallArgs)
	{
		try
		{
			return updateContent(updateCallArgs);
		}
		catch (VaSolSimException e)
		{
			return new VoidReturn();
		}
	}

	/**
	 * This function will migrate the given update arguments before passing them to the method call. By default, this
	 * method does nothing, but if arguments of incompatible updates are passed, a migration routine should be defined
	 * here by overriding the method.
	 * @param args args
	 * @return migrated args
	 */
	public Object[] updateTypeMigrationWrapper(Object... args)
	{
		return args;
	}

	/**
	 * This class distinguishes null returns from errors in fail-stop update calls.
	 */
	public static final class VoidReturn
	{
		public VoidReturn() {}

		@Override
		public String toString()
		{
			return "DraggablePane void return";
		}
	}
}
