package com.vasolsim.common.file;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/1/14 <p></p>
 */
public class QuestionSet
{
	private final boolean initializedFromFile;
	private BufferedImage[] resources    = null;
	private Image[]         fxResources  = null;
	private ResourceType    resourceType = ResourceType.NONE;
	private String name;

	private ArrayList<Question> questions = new ArrayList<Question>();

	public QuestionSet()
	{
		this("New Question Set", new ArrayList<Question>(), false);
	}

	public QuestionSet(final String name, final ArrayList<Question> questions, boolean initializedFromFile)
	{
		this.name = name;
		this.questions = questions;
		this.initializedFromFile = initializedFromFile;
	}

	public boolean loadPDFResource(String file)
	{
		return loadPDFResource(new File(file));
	}

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

	public void removeResource()
	{
		this.resourceType = ResourceType.NONE;
		this.resources = null;
	}

	public BufferedImage[] getResources()
	{
		return resources;
	}

	public void setResources(BufferedImage[] images)
	{
		this.resources = images;
	}

	public ResourceType getResourceType()
	{
		return resourceType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		if (!initializedFromFile)
			this.name = name;
	}

	public ArrayList<Question> getQuestions()
	{
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions)
	{
		if (!initializedFromFile)
			this.questions = questions;
	}

	public Image[] getFxResources()
	{
		return fxResources;
	}

	public void setFxResources(Image[] fxResources)
	{
		this.fxResources = fxResources;
	}
}
