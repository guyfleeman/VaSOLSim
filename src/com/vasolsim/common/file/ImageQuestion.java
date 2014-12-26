package com.vasolsim.common.file;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.vasolsim.common.GenericUtils;
import com.vasolsim.common.VaSolSimException;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.vasolsim.common.GenericUtils.convertBufferedImageToFXImage;

/**
 * @author willstuckey
 * @date 11/5/14
 * <p>This class represents a com.vasolsim.common.file.Question but with an associated image. See the
 * Question documentation for full information on constructor parameters.</p>
 */
public class ImageQuestion extends Question
{
	@Nullable
	private BufferedImage image   = null;
	@Nullable
	private Image         fxImage = null;

	/**
	 * Default constructor. Creates unlocked default com.vasolsim.common.file.Question with a null image.
	 */
	public ImageQuestion()
	{
		this(null);
	}

	/**
	 * Default constructor. Creates unlocked default com.vasolsim.common.file.Question with a given image.
	 *
	 * @param image the image
	 */
	public ImageQuestion(@Nullable BufferedImage image)
	{
		super();
		setImage(image);
	}

	/**
	 * Advanced constructor. Creates unlocked com.vasolsim.common.file.Question with a given image.
	 *
	 * @param name                 the name of the question
	 * @param question             the text of the question
	 * @param questionType         the type of question (how the question will be rendered)
	 * @param answerChoices        the answer choices
	 * @param correctAnswerChoices the correct answer choices
	 * @param scrambleAnswers      if the order of the answers will be scrambled when the test viewer renders the
	 *                             question
	 * @param answerOrderMatters   if the order of the answers is needed for a correct answer. Only applies to certain
	 *                             question types.
	 * @param image                the image
	 */
	public ImageQuestion(@NotNull String name,
	                     @Nullable String question,
	                     @NotNull GenericUtils.QuestionType questionType,
	                     @NotNull ArrayList<AnswerChoice> answerChoices,
	                     @NotNull ArrayList<AnswerChoice> correctAnswerChoices,
	                     boolean scrambleAnswers,
	                     boolean answerOrderMatters,
	                     @Nullable BufferedImage image)
	{
		this(name,
		     question,
		     questionType,
		     answerChoices,
		     correctAnswerChoices,
		     scrambleAnswers,
		     answerOrderMatters,
		     false,
		     image);
	}

	/**
	 * Advanced constructor. Creates unlocked/locked com.vasolsim.common.file.Question with a given image.
	 *
	 * @param name                 the name of the question
	 * @param question             the text of the question
	 * @param questionType         the type of question (how the question will be rendered)
	 * @param answerChoices        the answer choices
	 * @param correctAnswerChoices the correct answer choices
	 * @param scrambleAnswers      if the order of the answers will be scrambled when the test viewer renders the
	 *                             question
	 * @param answerOrderMatters   if the order of the answers is needed for a correct answer. Only applies to certain
	 *                             question types.
	 * @param isLocked             denotes if the question is locked
	 * @param image                the image
	 */
	ImageQuestion(@NotNull String name,
	              @Nullable String question,
	              @NotNull GenericUtils.QuestionType questionType,
	              @NotNull ArrayList<AnswerChoice> answerChoices,
	              @NotNull ArrayList<AnswerChoice> correctAnswerChoices,
	              boolean scrambleAnswers,
	              boolean answerOrderMatters,
	              boolean isLocked,
	              @Nullable BufferedImage image)
	{
		super(name,
		      question,
		      questionType,
		      answerChoices,
		      correctAnswerChoices,
		      scrambleAnswers,
		      answerOrderMatters,
		      isLocked);
		setImage(image);
	}

	/**
	 * gets the image attached to the question
	 *
	 * @return the image
	 */
	@Nullable
	public BufferedImage getImage()
	{
		return image;
	}

	/**
	 * sets the image attached to the question, only if the question is unlocked. Will re-render the buffered image to
	 * JavaFX image as well; if the image is large, you may need to handle this operation in a thread.
	 *
	 * @param image the image
	 */
	public final boolean setImage(@Nullable BufferedImage image)
	{
		if (!isLocked())
		{
			try
			{
				this.image = image;
				this.fxImage = convertBufferedImageToFXImage(image);
				return true;
			}
			catch (VaSolSimException e)
			{
				return false;
			}
		}

		return false;
	}

	/**
	 * gets the JavaFX image version of the image resource
	 *
	 * @return the JavaFX image
	 */
	@Nullable
	public Image getFxImage()
	{
		return fxImage;
	}

	/**
	 * sets the JavaFX image version of the image resource
	 *
	 * @param fxImage the JavaFX image
	 */
	public void setFxImage(@Nullable Image fxImage)
	{
		this.fxImage = fxImage;
	}
}
