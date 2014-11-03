package com.vasolsim.common.file;

/**
 * @author guyfleeman
 * @date 7/1/14 <p></p>
 */
public class AnswerChoice
{
	private final boolean initializedFromFile;
	private       boolean isActive;
	private       boolean isCorrect;
	private       int     idNumber;
	private       String  answerName;
	private       String  visibleChoiceID;
	private       String  text;

	public AnswerChoice()
	{
		this("New Answer", null, null, true, false, false);
	}

	public AnswerChoice(String answerName,
	                    String visibleChoiceID,
	                    String text,
	                    boolean isActive,
	                    boolean isCorrect,
	                    boolean initializedFromFile)
	{
		this.answerName = answerName;
		this.visibleChoiceID = visibleChoiceID;
		this.text = text;
		this.isActive = isActive;
		this.isCorrect = isCorrect;
		this.initializedFromFile = initializedFromFile;
	}

	public String getVisibleChoiceID()
	{
		return visibleChoiceID;
	}

	public String getAnswerText()
	{
		return text;
	}

	public int getIdNumber()
	{
		return idNumber;
	}

	public void setIdNumber(int idNumber)
	{
		this.idNumber = idNumber;
	}

	public boolean isInitializedFromFile()
	{
		return initializedFromFile;
	}

	public AnswerChoice setVisibleChoiceID(String visibleChoiceID)
	{
		if (!initializedFromFile)
			this.visibleChoiceID = visibleChoiceID;

		return this;
	}

	public AnswerChoice setText(String text)
	{
		if (!initializedFromFile)
			this.text = text;

		return this;
	}

	public String getAnswerName()
	{
		return answerName;
	}

	public void setAnswerName(String answerName)
	{
		this.answerName = answerName;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public boolean isCorrect()
	{
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect)
	{
		this.isCorrect = isCorrect;
	}
}
