package com.vasolsim.common.file;

import javax.annotation.Nullable;
import com.vasolsim.common.VaSolSimException;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

import static com.vasolsim.common.GenericUtils.convertBufferedImageToFXImage;

/**
 * @author willstuckey
 * @date 11/5/14 <p>This class represents a com.vasolsim.common.file.AnswerChoice but with an associated image. See the
 * AnswerChoice documentation for full information on constructor parameters.</p>
 */
public class ImageAnswerChoice extends AnswerChoice
{
	@Nullable
	private BufferedImage image   = null;
	@Nullable
	private Image         fxImage = null;

	/**
	 * Default constructor. Creates unlocked default com.vasolsim.common.file.AnswerChoice with a null image.
	 */
	public ImageAnswerChoice()
	{
		this(null);
	}

	/**
	 * Default constructor. Creates unlocked default com.vasolsim.common.file.AnswerChoice with a given image.
	 *
	 * @param image the image
	 */
	public ImageAnswerChoice(@Nullable BufferedImage image)
	{
		super();
		setImage(image);
	}

	/**
	 * Advanced constructor. Creates unlocked com.vasolsim.common.file.AnswerChoice with a given image.
	 * @param visibleChoiceID this is the string that will identify the answer choice. ID's will remain in order even if
	 *                        the answer choices are scrambled. (e.g. ABC will still be ABC)
	 * @param text, this is the text of the answer
	 * @param isActive denotes if the answer active. Only active answers will be written to the final exam file. For
	 *                 example, if a multiple choice question consists of 6 out of 8 answers, then there will be two
	 *                 inactive answers.
	 * @param isCorrect denotes if the answer is correct
	 * @param image the image
	 */
	public ImageAnswerChoice(@Nullable String visibleChoiceID,
	                         @Nullable String text,
	                         boolean isActive,
	                         boolean isCorrect,
	                         @Nullable BufferedImage image)
	{
		this(visibleChoiceID,
		     text,
		     isActive,
		     isCorrect,
		     false,
		     image);
	}

	/**
	 * Advanced constructor. Creates unlocked com.vasolsim.common.file.AnswerChoice with a given image.
	 * @param visibleChoiceID this is the string that will identify the answer choice. ID's will remain in order even if
	 *                        the answer choices are scrambled. (e.g. ABC will still be ABC)
	 * @param text, this is the text of the answer
	 * @param isActive denotes if the answer active. Only active answers will be written to the final exam file. For
	 *                 example, if a multiple choice question consists of 6 out of 8 answers, then there will be two
	 *                 inactive answers.
	 * @param isCorrect denotes if the answer is correct
	 * @param isLocked denotes if the answer is locked
	 * @param image the image
	 */
	ImageAnswerChoice(@Nullable String visibleChoiceID,
	                  @Nullable String text,
	                  boolean isActive,
	                  boolean isCorrect,
	                  boolean isLocked,
	                  @Nullable BufferedImage image)
	{
		super(visibleChoiceID, text, isActive, isCorrect, isLocked);
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
