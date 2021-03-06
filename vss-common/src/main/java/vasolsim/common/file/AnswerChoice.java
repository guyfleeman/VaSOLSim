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

package main.java.vasolsim.common.file;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * @author willstuckey
 * @date 7/1/14
 * <p>This class represents an answer choice to a question, and its associated data.</p>
 */
public class AnswerChoice
{
	private final boolean isLocked;
	private       boolean isActive;
	private       boolean isCorrect;
	@Nullable
	private       String  visibleChoiceID;
	@Nullable
	private       String  text;
	@Nullable
	private ObservableList<? extends Node> visualPersistence = null;

	/**
	 * Default constructor. Initializes an unlocked answer choice. Answer choice must be locked at time of creation to
	 * be opened by the test viewer.
	 */
	public AnswerChoice()
	{
		this(null, null, true, false);
	}

	/**
	 * Advanced constructor. Initializes an unlocked answer choice. Answer choice must be locked at time of creation to
	 * be opened by the test viewer.
	 * @param visibleChoiceID this is the string that will identify the answer choice. ID's will remain in order even if
	 *                        the answer choices are scrambled. (e.g. ABC will still be ABC)
	 * @param text, this is the text of the answer
	 * @param isActive denotes if the answer active. Only active answers will be written to the final exam file. For
	 *                 example, if a multiple choice question consists of 6 out of 8 answers, then there will be two
	 *                 inactive answers.
	 * @param isCorrect denotes if the answer is correct
	 */
	public AnswerChoice(@Nullable String visibleChoiceID,
	                    @Nullable String text,
	                    boolean isActive,
	                    boolean isCorrect)
	{
		this.visibleChoiceID = visibleChoiceID;
		this.text = text;
		this.isActive = isActive;
		this.isCorrect = isCorrect;
		this.isLocked = false;
	}

	/**
	 * Protected advanced constructor. Initializes an answer choice. Only the package local constructor may initialize a
	 * locked AnswerChoice. This deters unauthorized modifications to exam data.
	 * @param visibleChoiceID this is the string that will identify the answer choice. ID's will remain in order even if
	 *                        the answer choices are scrambled. (e.g. ABC will still be ABC)
	 * @param text, this is the text of the answer
	 * @param isActive denotes if the answer active. Only active answers will be written to the final exam file. For
	 *                 example, if a multiple choice question consists of 6 out of 8 answers, then there will be two
	 *                 inactive answers.
	 * @param isCorrect denotes if the answer is correct
	 * @param isLocked denotes if the answer is locked
	 */
	AnswerChoice(@Nullable String visibleChoiceID,
	                       @Nullable String text,
	                       boolean isActive,
	                       boolean isCorrect,
	                       boolean isLocked)
	{
		this.visibleChoiceID = visibleChoiceID;
		this.text = text;
		this.isActive = isActive;
		this.isCorrect = isCorrect;
		this.isLocked = isLocked;
	}

	/**
	 * returns if a locked answer choice is correct.
	 * @return is correct
	 */
	final boolean isLockedCorrect()
	{
		return isCorrect;
	}

	/**
	 * returns if the answer choice is locked
	 * @return if the answer choice is locked
	 */
	public final boolean isLocked()
	{
		return isLocked;
	}

	/**
	 * returns the visible choice id
	 * @return
	 */
	@Nullable
	public String getVisibleChoiceID()
	{
		return visibleChoiceID;
	}

	/**
	 * sets the visible choice id, only if the answer choice is unlocked
	 * @param visibleChoiceID the new ID
	 * @return if the set was successful
	 */
	public final boolean setVisibleChoiceID(@Nullable String visibleChoiceID)
	{
		if (!isLocked)
		{
			this.visibleChoiceID = visibleChoiceID;
			return true;
		}

		return false;
	}

	/**
	 * gets the answer text
	 * @return the answer text
	 */
	@Nullable
	public String getAnswerText()
	{
		return text;
	}

	/**
	 * sets the answer text, only if the answer choice is unlocked
	 * @param text the answer text
	 * @return if the set was successful
	 */
	public final boolean setText(@Nullable String text)
	{
		if (!isLocked)
		{
			this.text = text;
			return true;
		}

		return false;
	}

	/**
	 * gets if the answer choice is active
	 * @return is active
	 */
	public boolean isActive()
	{
		return isActive;
	}

	/**
	 * sets if the answer choice is active
	 * @param isActive if the answer choice is active
	 * @return if the set was successful
	 */
	public final boolean setActive(boolean isActive)
	{
		if (!isLocked)
		{
			this.isActive = isActive;
			return true;
		}

		return false;
	}

	/**
	 * get if an unlocked answer choice is correct. Locked answer choices will always return false. The only access to
	 * locked answers correct value is through a protected method. This deters unauthorized access to exam data.
	 * @return
	 */
	public final boolean isCorrect()
	{
		if (isLocked)
			return false;

		return isCorrect;
	}

	/**
	 * sets if the answer choice is correct, only if is unlocked
	 * @param isCorrect if the choice is correct
	 */
	public final boolean setCorrect(boolean isCorrect)
	{
		if (!isLocked)
		{
			this.isCorrect = isCorrect;
			return true;
		}

		return false;
	}

	/**
	 * Returns an unlocked answer choice from a locked answer choice after stripping protected data.
	 * @return an unlocked answer
	 */
	@Nonnull
	public final AnswerChoice getUnlockedAnswer()
	{
		return new AnswerChoice(visibleChoiceID,
		                        text,
		                        isActive,
		                        false,
		                        false);
	}

	/**
	 * gets the answer choice's persistent data
	 * @return data
	 */
	public @Nullable ObservableList<? extends Node> getVisualPersistence()
	{
		return visualPersistence;
	}

	/**
	 * sets the answer choice's persistent data
	 * @param visualPersistence data
	 */
	public void setVisualPersistence(@Nullable ObservableList<? extends Node> visualPersistence)
	{
		this.visualPersistence = visualPersistence;
	}

	/**
	 * clears the visualPersistence
	 */
	public void clearVisualPersistence()
	{
		this.visualPersistence = null;
	}

	public String toString()
	{
		return "AC: " + visibleChoiceID + ", " + text + ", " + isCorrect;
	}
}
