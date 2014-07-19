package com.gmail.guyfleeman.vasolsim.common.struct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/1/14
 * <p></p>
 */
public class QuestionSet
{
	private final boolean initializedFromFile;
	private byte[] resource = NO_RESOURCE_DATA.getBytes();
	private ResourceType resourceType = ResourceType.NONE;
	private String name;

	private ArrayList<Question> questions = new ArrayList<Question>();

	public QuestionSet(final String name, final ArrayList<Question> questions, boolean initializedFromFile)
	{
		this.name = name;
		this.questions = questions;
		this.initializedFromFile = initializedFromFile;
	}

	public boolean loadResource(ResourceType type, File resource)
	{
		if (!resource.isFile())
			return false;

		try
		{
			return loadResource(type, Files.readAllBytes(resource.toPath()));
		}
		catch (IOException e)
		{
			return false;
		}

	}

	public boolean loadResource(String resource)
	{
		return loadResource(ResourceType.TEXT, resource.getBytes());
	}

	public boolean loadResource(ResourceType type, byte[] raw)
	{
		if (initializedFromFile)
			return false;

	 	this.resourceType = type;
		this.resource = raw;

		return true;
	}

	public byte[] getResource() {
		return resource;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name)
	{
		if (!initializedFromFile)
			this.name = name;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions)
	{
		if (!initializedFromFile)
			this.questions = questions;
	}
}
