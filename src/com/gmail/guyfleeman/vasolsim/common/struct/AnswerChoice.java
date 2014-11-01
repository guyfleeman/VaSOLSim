package com.gmail.guyfleeman.vasolsim.common.struct;

/**
 * @author guyfleeman
 * @date 7/1/14
 * <p></p>
 */
public class AnswerChoice
{
    private final boolean initializedFromFile;
    private int idNumber;
	private String answerName;
    private String visibleChoiceID;
    private String text;

	public AnswerChoice()
	{
		this("New Answer", "none", "none", false);
	}

    public AnswerChoice(final String answerName,
		                final String visibleChoiceID,
                        final String text,
                        boolean initializedFromFile)
    {
	    this.answerName = answerName;
        this.visibleChoiceID = visibleChoiceID;
        this.text = text;
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

    public void setVisibleChoiceID(String visibleChoiceID)
    {
        if (!initializedFromFile)
            this.visibleChoiceID = visibleChoiceID;
    }

    public void setText(String text)
    {
        if (!initializedFromFile)
            this.text = text;
    }

	public String getAnswerName()
	{
		return answerName;
	}

	public void setAnswerName(String answerName)
	{
		this.answerName = answerName;
	}
}
