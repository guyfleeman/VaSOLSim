package com.vasolsim.common;

import com.sun.istack.internal.NotNull;
import com.vasolsim.common.file.Exam;
import com.vasolsim.common.notification.PopupManager;
import com.vasolsim.common.file.AnswerChoice;
import com.vasolsim.common.file.Question;
import com.vasolsim.common.file.QuestionSet;
import com.vasolsim.tclient.TeacherClient;
import com.vasolsim.tclient.tree.TreeElement;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.NullCipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.mail.Session;
import javax.mail.Transport;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author guyfleeman
 * @date 7/14/14 <p></p>
 */
public class GenericUtils
{
	//////////////////////////////////
	//  BEGIN CONSTANT DECLARATION  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_CONSTANT_DECLARATION = false;
	//////////////////////////////////

	/////////////////////////////
	//  DEFAULT CRYPTO VALUES  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_CRYPTO_VALUES = false;
	/////////////////////////////

	/**
	 * The default cryptography service provider interface.
	 */
	public static final String DEFAULT_SERVICE_PROVIDER_INTERFACE = "AES/CBC/PKCS5Padding";

	/**
	 * The default cryptography service provider.
	 */
	public static final String DEFAULT_SERVICE_PROVIDER = "SunJCE";

	/**
	 * The default encryption algorithm.
	 */
	public static final String DEFAULT_ENCRYPTION_ALGORITHM = "AES";


	//////////////////////////////
	//  DEFAULT ERROR MESSAGES  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_ERROR_MESSAGES = false;
	//////////////////////////////

	public static final String ERROR_MESSAGE_FILE_ALREADY_EXISTS                          =
			"File already exists and overwrite not permitted.";
	public static final String ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK          =
			"File not found after internal check. Try running as admin. Is this a bug or a file permissions error?";
	public static final String ERROR_MESSAGE_COULD_NOT_CREATE_DIRS                        =
			"Could not create file directory. Do you have permission to create a directory on your machine? " +
					"(try running the jar as admin)";
	public static final String ERROR_MESSAGE_COULD_NOT_CREATE_FILE                        =
			"Could not create file. Do you have permission to create a file on your machine? " +
					"(try running the jar as admin)";
	public static final String ERROR_MESSAGE_CREATE_FILE_EXCEPTION                        =
			"File creation internal exception. Do you have permission to create a file on your machine? Could this " +
					"be a bug? (try running the jar as admin)";
	public static final String ERROR_MESSAGE_BAD_CIPHER_MODE                              = "The provided cipher " +
			"initialization mode is invalid. Use class defined constant Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE";
	public static final String ERROR_MESSAGE_GENERIC_CRYPTO                               =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager.";
	public static final String ERROR_MESSAGE_CIPHER_NOT_INITIALIZED_PROPERLY              =
			"One or more ciphers was not properly initialized.";
	public static final String ERROR_MESSAGE_INTERNAL_HASH_EXCEPTION                      =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager. INTERNAL HASH EXCEPTION";
	public static final String ERROR_MESSAGE_STATS_DESTINATION_ADDRESS_NOT_PROVIDED       =
			"Statistics reporting requested but no address was provided";
	public static final String ERROR_MESSAGE_STANDALONE_STATS_ADDRESS_NOT_PROVIDED        =
			"Statistics reporting requested with standalone paradigm and no address was provided.";
	public static final String ERROR_MESSAGE_STANDALONE_STATS_PASSWORD_NOT_PROVIDED       =
			"Statistics reporting requested with standalone paradigm and no password was provided.";
	public static final String ERROR_MESSAGE_STANDALONE_STATS_SMTP_ADDRESS_NOT_PROVIDED   =
			"Statistics reporting requested with standalone paradigm and an invalid SMTP address was given";
	public static final String ERROR_MESSAGE_STANDALONE_STATS_SMTP_PORT_INVALID           =
			"Statistics reporting assigned an invalid SMTP port";
	public static final String ERROR_MESSAGE_BAD_SMTP_CREDENTIALS                         =
			"The provided SMTP email credentials were bad. Please see the popup dialog for more information.";
	public static final String ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager. XML_PARSER_INIT_EXCEPTION";
	public static final String ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION           =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager. COULD NOT WRITE XML FILE. INTERNAL " +
					"CONFIGURATION ERROR.";
	public static final String ERROR_MESSAGE_INTERNAL_TRANSFORMER_EXCEPTION               =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager. COULD NOT WRITE XML FILE. INTERNAL " +
					"TRANSFORMER EXCEPTION";

	///////////////////////////
	//  EMAIL PATTERN STUFF  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_EMAIL_PATTERNS = false;
	///////////////////////////

	public static String  validEmailRegex     = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
			"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static String  validAddressRegex   = "^[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static Pattern validEmailPattern   = Pattern.compile(validEmailRegex);
	public static Pattern validAddressPattern = Pattern.compile(validAddressRegex);

	///////////////////////
	//  OTHER CONSTANTS  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_OTHER_CONSTANTS = false;
	///////////////////////

	public static final int MIN_SETS      = 1;
	public static final int MAX_SETS      = 1024;
	public static final int MIN_QUESTIONS = 1;
	public static final int MAX_QUESTIONS = 16384;
	public static final int MIN_ANSWERS   = 1;
	public static final int MAX_ANSWERS   = 8;
	public static final int MIN_HASHES    = 1;
	public static final int MAX_HASHES    = 8;

	public static final String NO_DATA          = "!@!NONE!@!";
	public static final String NO_EMAIL         = "!@!NO-EMAIL!@!";
	public static final String NO_STATS         = "!@!NO-STATS!@!";
	public static final String NO_SMTP          = "!@!NO-SMTP!@!";
	public static final String NO_RESOURCE_DATA = "!@!NO-RSC-DATA!@!";

	public static final String NO_TEST_NAME_GIVEN   = "No test name given.";
	public static final String NO_AUTHOR_NAME_GIVEN = "No author given.";
	public static final String NO_SCHOOL_NAME_GIVEN = "No school name given.";
	public static final String NO_PERIOD_ID_GIVEN   = "No period given";
	public static final String NO_DATE_GIVEN        = "No date given.";

	public static enum ResourceType
	{
		NONE,
		PNG
	}

	public enum QuestionType
	{
		MULTIPLE_CHOICE,
		MULTIPLE_RESPONSE,
		TE_MULTIPLE_CHOICE,
		TE_MULTIPLE_RESPONSE,
		TE_D_AND_D_MULTIPLE_CHOICE,
		TE_D_AND_D_MULTIPLE_RESPONSE,
		TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE,
		TE_D_AND_D_VENN_DIAGRAM
	}

	///////////////////////////////////

	////////////////////////////////
	//  END CONSTANT DECLARATION  //
	////////////////////////////////


	//////////////////////////////////
	//  BEGIN FUNCTION DECLARATION  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_FUNCTIONS = false;
	//////////////////////////////////

	/**
	 * converts question type to a user friendly string
	 *
	 * @param questionType type
	 *
	 * @return string
	 */
	public static String questionTypeToString(QuestionType questionType)
	{
		switch (questionType)
		{
			case MULTIPLE_CHOICE:
				return "Multiple Choice";
			case TE_MULTIPLE_CHOICE:
				return "Multiple Choice (TE)";
			case TE_D_AND_D_MULTIPLE_CHOICE:
				return "Multiple Choice (TE/DD)";
			case MULTIPLE_RESPONSE:
				return "Multiple Response";
			case TE_MULTIPLE_RESPONSE:
				return "Multiple Response (TE)";
			case TE_D_AND_D_MULTIPLE_RESPONSE:
				return "Multiple Response (TE/DD)";
			case TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE:
				return "Grammar (TE/DD)";
			case TE_D_AND_D_VENN_DIAGRAM:
				return "Venn Diagram (TE/DD)";
			default:
				return "UNK";
		}
	}

	/**
	 * Securely shrinks a 512bit hash to a 128bit hash
	 *
	 * @param hash 512 bit (64 byte) hash to shrink
	 *
	 * @return 128 bit (16 byte) hash, -1 if 64 byte array not provided
	 */
	public static byte[] validate512HashTo128Hash(byte[] hash)
	{
		if (hash.length != 64)
			return new byte[]{-1};

		/*
		 * Create 16byte quarters from the 64 byte hash
		 */
		byte[] lowerQuarterOne = new byte[16];
		byte[] lowerQuarterTwo = new byte[16];
		byte[] higherQuarterOne = new byte[16];
		byte[] higherQuarterTwo = new byte[16];
		byte[] lowerHalf = new byte[16];
		byte[] higherHalf = new byte[16];
		byte[] xorHash = new byte[16];

		/*
		 * Copy key parts
		 */
		System.arraycopy(hash, 0x00, lowerQuarterOne, 0x00, 0x10);
		System.arraycopy(hash, 0x10, lowerQuarterTwo, 0x00, 0x10);
		System.arraycopy(hash, 0x20, higherQuarterOne, 0x00, 0x10);
		System.arraycopy(hash, 0x30, higherQuarterTwo, 0x00, 0x10);

		/*
		 * XOR the lower and higher quarters, then xor the lower and high halves
		 */
		for (int index = 0; index < 16; index++)
		{
			lowerHalf[index] = (byte) (0xFF & ((int) lowerQuarterOne[index] ^ (int) lowerQuarterTwo[index]));
			higherHalf[index] = (byte) (0xFF & ((int) higherQuarterOne[index] ^ (int) higherQuarterTwo[index]));
			xorHash[index] = (byte) (0xFF & ((int) lowerHalf[index] ^ (int) higherHalf[index]));
		}

		return xorHash;
	}

	/**
	 * Converts a byte array hash to a character representation
	 *
	 * @param hash the hash to convert to plain text
	 *
	 * @return plain text representation of the hash value
	 */
	public static String convertBytesToHexString(byte[] hash)
	{
		StringBuilder hashString = new StringBuilder();
		for (byte b : hash)
			hashString.append(Integer.toString((b & 0xFF) + 0x0100, 0x10).substring(1));

		return hashString.toString();
	}

	/**
	 * Converts a hex string to a byte array
	 *
	 * @param hex the hex string
	 *
	 * @return byte array
	 */
	public static byte[] convertHexStringToBytes(String hex)
	{
		int length = hex.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2)
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));

		return data;
	}

	/**
	 * initializes a cipher
	 *
	 * @param key  the key
	 * @param mode the mode (Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE)
	 *
	 * @return an initialized cipher
	 *
	 * @throws VaSolSimException for all the usual crypto stuff
	 */
	public static Cipher initCrypto(byte[] key, int mode) throws VaSolSimException
	{
		if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
			throw new VaSolSimException(ERROR_MESSAGE_BAD_CIPHER_MODE);

		byte[] parametricIV = new byte[16];
		Cipher cipher;
		try
		{
			//create an IV
			SecureRandom random = new SecureRandom();
			random.nextBytes(parametricIV);

			//initialize the crypto
			cipher = Cipher.getInstance(DEFAULT_SERVICE_PROVIDER_INTERFACE, DEFAULT_SERVICE_PROVIDER);
			cipher.init(
					mode,
					new SecretKeySpec(key, DEFAULT_ENCRYPTION_ALGORITHM),
					new IvParameterSpec(parametricIV));
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM\n" +
					                            e.toString() + "\n" +
					                            e.getCause() + "\n" +
					                            ExceptionUtils.getStackTrace(e),
			                            e);
		}
		catch (NoSuchProviderException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PROVIDER\n" +
					                            e.toString() + "\n" +
					                            e.getCause() + "\n" +
					                            ExceptionUtils.getStackTrace(e),
			                            e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nNO SUCH PADDING\n" +
					                            e.toString() + "\n" +
					                            e.getCause() + "\n" +
					                            ExceptionUtils.getStackTrace(e),
			                            e);
		}
		catch (InvalidKeyException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD KEY\n" +
					                            e.toString() + "\n" +
					                            e.getCause() + "\n" +
					                            ExceptionUtils.getStackTrace(e),
			                            e);
		}
		catch (InvalidAlgorithmParameterException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM PARAMS\n" +
					                            e.toString() + "\n" +
					                            e.getCause() + "\n" +
					                            ExceptionUtils.getStackTrace(e),
			                            e);
		}

		return cipher;
	}

	/**
	 * Applies a cipher to bytes.
	 *
	 * @param message the bytes to be ciphered
	 * @param cipher  the initialized cipher
	 *
	 * @return the new bytes, or -1 if ya done fucked up
	 */
	public static byte[] applyCryptographicCipher(byte[] message, Cipher cipher)
	{
		try
		{
			return forceCryptographicCipher(message, cipher);
		}
		catch (Exception e)
		{
			return new byte[]{-1};
		}
	}

	/**
	 * Runs basic tests to see if a cipher is initialized properly.
	 *
	 * @param cipher cipher
	 *
	 * @return if properly initialized
	 */
	public static boolean isCipherProperlyInitialized(Cipher cipher)
	{
		return !(cipher instanceof NullCipher);
	}

	/**
	 * Applies a cipher to bytes.
	 *
	 * @param message the bytes to be ciphered
	 * @param cipher  the *initialized* cipher
	 *
	 * @return new bytes
	 *
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] forceCryptographicCipher(byte[] message, Cipher cipher)
			throws IllegalBlockSizeException, BadPaddingException
	{
		return cipher.doFinal(message);
	}

	/**
	 * Returns if a given string is a valid email regex
	 *
	 * @param email email address
	 *
	 * @return if the email address is a valid email
	 */
	public static boolean isValidEmail(String email)
	{
		return !(email == null || email.equals("")) && validEmailPattern.matcher(email).matches();
	}

	/**
	 * Returns if a given string is a valid address
	 *
	 * @param address net address
	 *
	 * @return if the address is a valid address
	 */
	public static boolean isValidAddress(String address)
	{
		return !(address == null || address.equals("")) && validAddressPattern.matcher(address).matches();
	}

	/**
	 * Returns if a given http based address can be connected to
	 *
	 * @param address the address
	 *
	 * @return if teh connection can be made
	 */
	public static boolean canConnectToAddress(String address)
	{
		if (!isValidAddress(address))
			return false;

		try
		{
			HttpURLConnection connection = (HttpURLConnection) (new URL(address).openConnection());
			connection.setRequestMethod("HEAD");
			connection.connect();
			boolean success = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
			connection.disconnect();
			return success;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Tests if a given SMTP configuration is valid. It will validate addresses and the port. Then it will test
	 * connectivity of the SMTP address. Lastly, it will AUTH to SMTP server and ensure the information is good.
	 *
	 * @param address  the SMTP address
	 * @param port     the SMTP port
	 * @param email    the email address
	 * @param password the email address password
	 *
	 * @return if the AUTH was successful
	 */
	public static boolean isValidSMTPConfiguration(String address,
	                                               int port,
	                                               String email,
	                                               byte[] password)
	{
		return isValidSMTPConfiguration(address, port, email, password, false);
	}

	/**
	 * Tests if a given SMTP configuration is valid. It will validate addresses and the port. Then it will test
	 * connectivity of the smtp address. Lastly, it will AUTH to smtp server and ensure the information is good.
	 *
	 * @param address  the SMTP address
	 * @param port     the SMTP port
	 * @param email    the email address
	 * @param password the email address password
	 * @param notify   if popup dialogs will appear carrying the servers unsuccessful response message
	 *
	 * @return if the AUTH was successful
	 */
	public static boolean isValidSMTPConfiguration(String address,
	                                               int port,
	                                               String email,
	                                               byte[] password,
	                                               boolean notify)
	{
		if (!isValidAddress(address) || port <= 0 || !isValidEmail(email) || password.length == 0)
			return false;

		try
		{
			Properties smtpProperties = new Properties();
			smtpProperties.put("mail.smtp.starttls.enable", "true");
			smtpProperties.put("mail.smtp.auth", "true");
			Session session = Session.getInstance(smtpProperties, null);
			Transport transport = session.getTransport("smtp");
			transport.connect(address, port, email, new String(password));
			transport.close();
			return true;
		}
		catch (Exception e)
		{
			if (notify)
			{
				PopupManager.showMessage("Cause:\n" + e.getCause() + "\n\nMessage:\n" + e.getMessage(), TeacherClient.SMTP_BAD_TITLE);

				System.out.println(e.getCause());
				System.out.println(e.getMessage());
			}

			return false;
		}
	}

	/**
	 * Returns if a port is valid.
	 *
	 * @param port port
	 *
	 * @return if its valid
	 */
	public static boolean isValidPort(int port)
	{
		return port > 0 && port < 65536;
	}

	/**
	 * Creates a text node within a created element and then attaches the date to a parent.
	 *
	 * @param elementName   the name for the new element
	 * @param nodeData      the data for the new text node
	 * @param parentElement the parent element
	 * @param doc           the document (root)
	 */
	public static void appendSubNode(String elementName, String nodeData, Element parentElement, Document doc)
	{
		Element subElement = doc.createElement(elementName);
		subElement.appendChild(doc.createTextNode(nodeData));
		parentElement.appendChild(subElement);
	}

	/**
	 * Checks questions qSets to ensure no null elements are present and
	 *
	 * @param questionSets
	 *
	 * @return
	 */
	@Deprecated
	public static boolean verifyQuestionSetsIntegrity(ArrayList<QuestionSet> questionSets)
	{
		if (questionSets == null
				|| questionSets.size() < MIN_SETS
				|| questionSets.size() > MAX_SETS)
			return false;

		for (QuestionSet set : questionSets)
		{
			if (set == null
					|| set.getQuestions() == null
					|| set.getQuestions().size() < MIN_QUESTIONS
					|| set.getQuestions().size() > MAX_QUESTIONS)
				return false;

			for (Question question : set.getQuestions())
			{
				if (question == null
						|| question.getAnswerChoices() == null
						|| question.getAnswerChoices().size() < MIN_QUESTIONS
						|| question.getAnswerChoices().size() > MAX_QUESTIONS)
					return false;

				for (AnswerChoice ac : question.getAnswerChoices())
					if (ac == null
							|| ac.getAnswerText() == null
							|| ac.getAnswerText().equals(""))
						return false;
			}
		}

		return true;
	}

	/**
	 *
	 * @param exam
	 * @return
	 */
	public static ArrayList<String> checkExamIntegrity(Exam exam)
	{
		ArrayList<String> errors = new ArrayList<String>();

		if (exam.getTestName() == null)
			errors.add("INTERNAL: Test name is null and is not Nullable.");

		if (exam.getAuthorName() == null)
			errors.add("INTERNAL: Author name is null and is not Nullable.");

		if (exam.getSchoolName() == null)
			errors.add("INTERNAL: School name is null and is not Nullable.");

		if (exam.getPeriodName() == null)
			errors.add("INTERNAL: Period is null and is not Nullable.");

		if (exam.isReportingStats())
		{
			if (exam.getStatsDestinationEmail() == null || !isValidEmail(exam.getStatsDestinationEmail()))
				errors.add("Statistics reporting is selected but is not accompanied by a valid email address.");

			if (exam.isReportingStatsStandalone())
			{
				if (exam.getStatsSenderEmail() == null || !isValidEmail(exam.getStatsSenderEmail()))
					errors.add("Standalone statistics is selected but is not accompanied by a valid sender email " +
							           "address.");

				if (exam.getStatsSenderPassword() == null)
					errors.add("Standalone statistics is selected but the sender address does not have a valid " +
							           "password.");

				if (exam.getStatsSenderSMTPAddress() == null || !isValidAddress(exam.getStatsSenderSMTPAddress()))
					errors.add("Standalone statistics is selected but the SMTP address is not valid.");

				if (!isValidPort(exam.getStatsSenderSMTPPort()))
					errors.add("Standalone statistics is selected but the SMTP port is not valid.");

				//TODO check smtp configuration
			}
		}

		for (QuestionSet questionSet : exam.getQuestionSets())
			for (Question question : questionSet.getQuestions())
			{
				boolean hasFoundCorrect = false;
				for (AnswerChoice answerChoice : question.getAnswerChoices())
					if (answerChoice.isCorrect())
					{
						hasFoundCorrect = true;
						break;
					}

				if (!hasFoundCorrect)
					errors.add("Question " + question.getName() +
							           " in " + questionSet.getName() +
							           " does not have a correct answer selected.");
			}

		return errors;
	}

	/**
	 *
	 * @param errors
	 * @return
	 */
	public static String errorsToOutput(@NotNull ArrayList<String> errors)
	{
		StringBuilder errorBuilder = new StringBuilder();
		errorBuilder.append("Found ").append(errors.size()).append(" errors that require attention.\n\n");
		for (String error : errors)
			errorBuilder.append(error).append("\n");

		return errorBuilder.toString();
	}

	public static String exceptionToString(@NotNull Throwable t)
	{
		StringBuilder out = new StringBuilder();
		out.append("Ex: ").append(t.toString()).append("\n");
		out.append("Cause: ").append(t.getCause()).append("\n");
		out.append("Message: ").append(t.getMessage()).append("\n\n");
		out.append("StackTrace:\n").append(ExceptionUtils.getStackTrace(t));
		out.append("---------- END ----------");

		return out.toString();
	}

	/**
	 * gets the number of pages in a pdf
	 *
	 * @param file
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	public static int getPDFPages(File file) throws IOException
	{
		PDDocument doc = PDDocument.load(file);
		int pages = doc.getNumberOfPages();
		doc.close();
		return pages;
	}

	/**
	 * renders a pdf to images
	 *
	 * @param file pdf file
	 *
	 * @return images
	 *
	 * @throws IOException
	 */
	public static BufferedImage[] renderPDF(File file) throws IOException
	{
		PDDocument doc = PDDocument.load(file);
		@SuppressWarnings("unchecked")
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		Iterator<PDPage> iterator = pages.iterator();
		BufferedImage[] images = new BufferedImage[pages.size()];
		for (int i = 0; iterator.hasNext(); i++)
			images[i] = iterator.next().convertToImage();

		doc.close();

		return images;
	}

	/**
	 * converts an awt image to a javafx image
	 *
	 * @param image
	 *
	 * @return
	 *
	 * @throws VaSolSimException
	 */
	public static Image convertBufferedImageToFXImage(BufferedImage image) throws VaSolSimException
	{
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image, "png", out);
			out.flush();

			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			return new Image(in);
		}
		catch (IOException e)
		{
			//TODO field message out
			throw new VaSolSimException("image error");
		}
	}

	/**
	 * converts awt images to fx images
	 *
	 * @param images
	 *
	 * @return
	 *
	 * @throws VaSolSimException
	 */
	public static Image[] convertBufferedImagesToFXImages(BufferedImage[] images) throws VaSolSimException
	{
		Image[] fxImages = new Image[images.length];
		for (int i = 0; i < images.length; i++)
			fxImages[i] = convertBufferedImageToFXImage(images[i]);

		return fxImages;
	}


	////////////////////////
	//  YAY JAVAFX UTILS  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_JAVAFX_UTILS = false;
	////////////////////////

	/**
	 * Creates a tree item
	 *
	 * @param resourceLoaderClass
	 * @param title
	 * @param iconLocation
	 * @param imgSize
	 *
	 * @return
	 */
	public static TreeItem<String> createTreeItem(Class resourceLoaderClass,
	                                              String title,
	                                              String iconLocation,
	                                              int imgSize)
	{
		if (resourceLoaderClass == null || title == null || iconLocation == null || imgSize <= 0)
			return null;

		ImageView examsIcon = new ImageView(
				new Image(resourceLoaderClass.getResource(iconLocation).toExternalForm()));
		examsIcon.setFitHeight(imgSize);
		examsIcon.setFitWidth(imgSize);
		return new TreeItem<String>(title, examsIcon);
	}

	public static TreeItem<TreeElement> createTreeItem(Class resourceLoaderClass,
	                                                   TreeElement box,
	                                                   String iconLocation,
	                                                   int imgSize)
	{
		if (resourceLoaderClass == null || box == null || iconLocation == null || imgSize <= 0)
			return null;

		ImageView examsIcon = new ImageView(
				new Image(resourceLoaderClass.getResource(iconLocation).toExternalForm()));
		examsIcon.setFitHeight(imgSize);
		examsIcon.setFitWidth(imgSize);
		return new TreeItem<TreeElement>(box, examsIcon);
	}

	/**
	 * pauses a thread
	 */
	public static void pause()
	{
		pause(300);
	}

	/**
	 * pauses a thread
	 *
	 * @param time time
	 */
	public static void pause(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * exports an internal resource to an external file
	 *
	 * @param internalResource internal resource
	 * @param externalResource external target file
	 *
	 * @return if the export was successful
	 */
	public static boolean exportResource(String internalResource, String externalResource)
	{
		return exportResource(GenericUtils.class.getResource(internalResource), new File(externalResource));
	}

	/**
	 * exports an internal resource to an external file
	 *
	 * @param internalResource internal resource
	 * @param externalResource external target file
	 *
	 * @return if the export was successful
	 */
	public static boolean exportResource(URL internalResource, File externalResource)
	{
		try
		{
			FileUtils.copyURLToFile(internalResource, externalResource);
		}
		catch (IOException e)
		{
			return false;
		}

		return true;
	}

	/*
	 * considering porting text formatting to html but 2.2 implementation is a pain
	public static String getHtmlLead(String content)
	{

	}

	public static String getHtmlClose(String content)
	{

	}

	public static String getHtmlContent(String content)
	{

	}
	*/

	/**
	 * Everything should be static so don't allow initialization of the class.
	 */
	private GenericUtils()
	{
	}
}
