package com.vasolsim.common.file;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author willstuckey
 * @date 7/1/14
 * <p>This class represents a question set and its attached resources.</p>
 */
public class QuestionSet
{
	private final boolean isLocked;
	@NotNull
	private String name;
	@NotNull
	private ArrayList<Question> questions = new ArrayList<Question>();
	@NotNull
	private ResourceType    resourceType = ResourceType.NONE;
	@Nullable
	private BufferedImage[] resources    = null;
	@Nullable
	private Image[]         fxResources  = null;

	/**
	 * Default constructor. Initializes a question set. Questions sets must be locked before they will be accepted by
	 * the viewer.
	 */
	public QuestionSet()
	{
		this("New Question Set", new ArrayList<Question>());
	}

	/**
	 * Advanced constructor. Initializes a question set. Questions sets must be locked before they will be accepted by
	 * the viewer.
	 * @param name the name of the question set, will appear in the viewer as a reference
	 * @param questions the questions attached to the set
	 */
	public QuestionSet(@NotNull String name, @NotNull ArrayList<Question> questions)
	{
		this.name = name;
		this.questions = questions;
		this.isLocked = false;
	}

	/**
	 * Advanced constructor. Initializes a question set. Questions sets must be locked before they will be accepted by
	 * the viewer.
	 * @param name the name of the question set, will appear in the viewer as a reference
	 * @param questions the questions attached to the set
	 * @param isLocked if the set is locked
	 */
	QuestionSet(@NotNull String name, @NotNull ArrayList<Question> questions, boolean isLocked)
	{
		this.name = name;
		this.questions = questions;
		this.isLocked = isLocked;
	}

	/**
	 * loads a pdf resource from a file. NOTE: rendering takes time, handle this in a thread or task if it's being made
	 * by a locking UI call
	 * @param file the pdf file
	 * @return if the addition was successful
	 */
	public boolean loadPDFResource(String file)
	{
		return loadPDFResource(new File(file));
	}

	/**
	 * loads a pdf resource from a file. NOTE: rendering takes time, handle this in a thread or task if it's being made
	 * by a locking UI call
	 * @param file the pdf file
	 * @return if the addition was successful
	 */
	public boolean loadPDFResource(File file)
	{
		try
		{
			resources = renderPDF(file);
			resourceType = ResourceType.PDF;
		}
		catch (IOException e)
		{
			resourceType = ResourceType.NONE;
			resources = null;
			return false;
		}

		return true;
	}

	/**
	 * removes the current resource, only if the set is unlocked
	 */
	public final void removeResource()
	{
		if (!isLocked)
		{
			this.resourceType = ResourceType.NONE;
			this.resources = null;
		}
	}

	/**
	 * gets the resources as images
	 * @return
	 */
	@Nullable
	public BufferedImage[] getResources()
	{
		return resources;
	}

	/**
	 * directly sets the resources as images
	 * @param images
	 */
	public final void setResources(@Nullable BufferedImage[] images)
	{
		if (!isLocked)
			this.resources = images;
	}

	/**
	 * gets the resources as JavaFX images
	 * @return
	 */
	@Nullable
	public Image[] getFxResources()
	{
		return fxResources;
	}

	/**
	 * sets the JavaFX resources
	 * @param fxResources
	 */
	public void setFxResources(@Nullable Image[] fxResources)
	{
		this.fxResources = fxResources;
	}

	/**
	 * gets the resource type
	 * @return resource type
	 */
	@NotNull
	public ResourceType getResourceType()
	{
		return resourceType;
	}

	/**
	 * gets the name
	 * @return name
	 */
	@NotNull
	public String getName()
	{
		return name;
	}

	/**
	 * sets the name, only if the set is unlocked
	 * @param name
	 */
	public final void setName(@NotNull String name)
	{
		if (!isLocked)
			this.name = name;
	}

	/**
	 * gets the attached questions
	 * @return questions
	 */
	@NotNull
	public ArrayList<Question> getQuestions()
	{
		return questions;
	}

	/**
	 * sets the attached question, only if the set is unlocked
	 * @param questions questions
	 */
	public final void setQuestions(@NotNull ArrayList<Question> questions)
	{
		if (!isLocked)
			this.questions = questions;
	}
}
