package com.gmail.guyfleeman.vasolsim.common;

import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.common.struct.AnswerChoice;
import com.gmail.guyfleeman.vasolsim.common.struct.Question;
import com.gmail.guyfleeman.vasolsim.common.struct.QuestionSet;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import javax.mail.Session;
import javax.mail.Transport;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author guyfleeman
 * @date 7/14/14
 * <p></p>
 */
public class GenericUtils {
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


	//////////////////////////////
	//  XML STRUCTURE CONSTANTS //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_XML_STRUCTURE_CONSTANTS = false;
	//////////////////////////////

	//system keys
	public static final String INDENTATION_KEY = "{http://xml.apache.org/xslt}indent-amount";

	//root depth
	public static final String XML_ROOT_ELEMENT_NAME = "vssroot";

	//root+ depth
	public static final String XML_INFO_ELEMENT_NAME = "info";

	//root++ depth (information+)
	public static final String XML_TEST_NAME_ELEMENT_NAME   = "testName";
	public static final String XML_AUTHOR_NAME_ELEMENT_NAME = "author";
	public static final String XML_SCHOOL_NAME_ELEMENT_NAME = "school";
	public static final String XML_PERIOD_NAME_ELEMENT_NAME = "class";
	public static final String XML_DATE_ELEMENT_NAME        = "date";

	//root+ depth
	public static final String XML_SECURITY_ELEMENT_NAME = "sec";

	//root++ depth (sec+)
	public static final String XML_ENCRYPTED_VALIDATION_HASH_ELEMENT_NAME            = "encValHash";
	public static final String XML_PARAMETRIC_INITIALIZATION_VECTOR_ELEMENT_NAME     = "paramIV";
	public static final String XML_IS_REPORTING_STATISTICS_ELEMENT_NAME              = "statsReporting";
	public static final String XML_IS_REPORTING_STATISTICS_STANDALONE_ELEMENT_NAME   = "statsStandalone";
	public static final String XML_IS_ENCRYPTING_STATISTICS_ELEMENT_NAME             = "statsEnc";
	public static final String XML_STATISTICS_DESTINATION_EMAIL_ADDRESS_ELEMENT_NAME = "statsDestEmail";
	public static final String XML_STATISTICS_SENDER_EMAIL_ADDRESS_ELEMENT_NAME      = "statsSender";
	public static final String XML_STATISTICS_SENDER_EMAIL_PASSWORD_ELEMENT_NAME     = "statsSenderPw";
	public static final String XML_STATISTICS_SENDER_SMTP_ADDRESS_ELEMENT_NAME       = "statsSenderSMTPAddr";
	public static final String XML_STATISTICS_SENDER_SMTP_PORT_ELEMENT_NAME          = "statsSenderSMTPPort";

	//root+ depth
	public static final String XML_QUESTION_SET_ELEMENT_NAME = "questionSet";

	//root++ depth (questionSet+)
	public static final String XML_QUESTION_SET_ID_ELEMENT_NAME            = "setID";
	public static final String XML_QUESTION_SET_NAME_ELEMENT_NAME          = "setName";
	public static final String XML_QUESTION_SET_RESOURCE_TYPE_ELEMENT_NAME = "rscType";
	public static final String XML_QUESTION_SET_RESOURCE_DATA_ELEMENT_NAME = "rscData";
	public static final String XML_QUESTION_ELEMENT_NAME                   = "question";

	//root+++ depth (questionSet++, questionGrouping+)
	public static final String XML_QUESTION_ID_ELEMENT_NAME                   = "questionID";
	public static final String XML_QUESTION_NAME_ELEMENT_NAME                 = "questionName";
	public static final String XML_QUESTION_TEXT_ELEMENT_NAME                 = "question";
	public static final String XML_QUESTION_SCRAMBLE_ANSWERS_ELEMENT_NAME     = "scramAns";
	public static final String XML_QUESTION_REATIAN_ANSWER_ORDER_ELEMENT_NAME = "retOrder";
	public static final String XML_QUESTION_ENCRYPTED_ANSWER_HASH             = "encAnsHash";
	public static final String XML_ANSWER_CHOICE_ELEMENT_NAME                 = "answerChoice";

	//root++++ depth (questionSet+++, questionGrouping++, answer+)
	public static final String XML_ANSWER_CHOICE_ID_ELEMENT_NAME         = "answerID";
	public static final String XML_ANSWER_CHOICE_VISIBLE_ID_ELEMENT_NAME = "answerVisibleID";
	public static final String XML_ANSWER_TEXT_ELEMENT_NAME              = "answer";

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

	public static final String NO_STATS = "!@!NO-STATS!@!";
	public static final String NO_SMTP  = "!@!NO-SMTP!@!";
	public static final String NO_RESOURCE_DATA = "!@!NO-RSC-DATA!@!";

	public static final String NO_TEST_NAME_GIVEN = "No test name given.";
	public static final String NO_AUTHOR_NAME_GIVEN = "No author given.";
	public static final String NO_SCHOOL_NAME_GIVEN = "No school name given.";
	public static final String NO_PERIOD_ID_GIVEN = "No period given";
	public static final String NO_DATE_GIVEN = "No date given.";

	public static enum ResourceType {
		NONE,
		TEXT,
		PDF,
		PNG,
		JPG,
		GIF
	}

	public enum QuestionType {
		MULTIPLE_CHOICE,
		D_AND_D_MULTIPLE_CHOICE,
		D_AND_D_MULTIPLE_RESPONSE,
		D_AND_D_GRAMMAR_MULTIPLE_RESPONSE,
		D_AND_D_VENN_DIAGRAM
	}

	////////////////////////////////
	//  END CONSTANT DECLARATION  //
	////////////////////////////////


	//////////////////////////////////
	//  BEGIN FUNCTION DECLARATION  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_FUNCTIONS = false;
	//////////////////////////////////

	/**
	 * Securely shrinks a 512bit hash to a 128bit hash
	 *
	 * @param hash 512 bit (64 byte) hash to shrink
	 * @return 128 bit (16 byte) hash, -1 if 64 byte array not provided
	 */
	public static byte[] validate512HashTo128Hash(byte[] hash) {
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
		for (int index = 0; index < 16; index++) {
			lowerHalf[index] = (byte) (0xFF & ((int) lowerQuarterOne[index] ^ (int) lowerQuarterTwo[index]));
			higherHalf[index] = (byte) (0xFF & ((int) higherQuarterOne[index] ^ (int) higherQuarterTwo[index]));
			xorHash[index] = (byte) (0xFF & ((int) lowerHalf[index] ^ (int) higherHalf[index]));
		}

		return xorHash;
	}

	/**
	 * Converts a byte array hash to a character representation for storage and comparison
	 *
	 * @param hash the hash to convert to plain text
	 * @return plain text representation of the hash value
	 */
	public static String convertBytesToHexString(byte[] hash) {
		StringBuilder hashString = new StringBuilder();
		for (byte b : hash)
			hashString.append(Integer.toString((b & 0xFF) + 0x0100, 0x10).substring(1));

		return hashString.toString();
	}

	/**
	 * Applies a cipher to bytes.
	 *
	 * @param message the bytes to be ciphered
	 * @param cipher  the *initialized* cipher
	 * @return the new bytes, or -1 if ya done fucked up
	 */
	public static byte[] applyCryptographicCipher(byte[] message, Cipher cipher) {
		try {
			return forceCryptographicCipher(message, cipher);
		} catch (Exception e) {
			return new byte[]{-1};
		}
	}

	/**
	 * Runs basic tests to see if a cipher is initialized properly.
	 *
	 * @param cipher cipher
	 * @return if properly initialized
	 */
	public static boolean isCipherProperlyInitialized(Cipher cipher) {
		return !(cipher instanceof NullCipher);

		/*
		try {
			cipher.doFinal("Picard>>ThanKirk".getBytes());
			return true;
		} catch (Exception e)
		{
			System.out.println(e.toString());
			return false;
		}
		*/
	}

	/**
	 * Applies a cipher to bytes.
	 *
	 * @param message the bytes to be ciphered
	 * @param cipher  the *initialized* cipher
	 * @return new bytes
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] forceCryptographicCipher(byte[] message, Cipher cipher)
			throws IllegalBlockSizeException, BadPaddingException {
		return cipher.doFinal(message);
	}

	/**
	 * Returns if a given string is a valid email regex
	 *
	 * @param email email address
	 * @return if the email address is a valid email
	 */
	public static boolean isValidEmail(String email) {
		return !(email == null || email.equals("")) && validEmailPattern.matcher(email).matches();
	}

	/**
	 * Returns if a given string is a valid address
	 *
	 * @param address net address
	 * @return if the address is a valid address
	 */
	public static boolean isValidAddress(String address) {
		return !(address == null || address.equals("")) && validAddressPattern.matcher(address).matches();
	}

	/**
	 * Returns if a given http based address can be connected to
	 *
	 * @param address the address
	 * @return if teh connection can be made
	 */
	public static boolean canConnectToAddress(String address) {
		if (!isValidAddress(address))
			return false;

		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(address).openConnection());
			connection.setRequestMethod("HEAD");
			connection.connect();
			boolean success = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
			connection.disconnect();
			return success;
		} catch (Exception e) {
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
	 * @return if the AUTH was successful
	 */
	public static boolean isValidSMTPConfiguration(String address,
	                                               int port,
	                                               String email,
	                                               byte[] password) {
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
	 * @return if the AUTH was successful
	 */
	public static boolean isValidSMTPConfiguration(String address,
	                                               int port,
	                                               String email,
	                                               byte[] password,
	                                               boolean notify) {
		if (!isValidAddress(address) || port <= 0 || !isValidEmail(email) || password.length == 0)
			return false;

		/*
		if (!canConnectToAddress(address))
			return false;
			*/

		try {
			Properties smtpProperties = new Properties();
			smtpProperties.put("mail.smtp.starttls.enable", "true");
			smtpProperties.put("mail.smtp.auth", "true");
			Session session = Session.getInstance(smtpProperties, null);
			Transport transport = session.getTransport("smtp");
			transport.connect(address, port, email, new String(password));
			transport.close();
			return true;
		} catch (Exception e) {
			if (notify) {
				PopupManager.showMessage("Cause:\n" + e.getCause() + "\n\nMessage:\n" + e.getMessage(),
						"SMTP Failed!");
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
	 * @return if its valid
	 */
	public static boolean isValidPort(int port) {
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
	public static void appendSubNode(String elementName, String nodeData, Element parentElement, Document doc) {
		Element subElement = doc.createElement(elementName);
		subElement.appendChild(doc.createTextNode(nodeData));
		parentElement.appendChild(subElement);
	}

	/**
	 * Checks questions sets to ensure no null elements are present and
	 *
	 * @param questionSets
	 * @return
	 */
	public static boolean verifyQuestionSetsIntegrity(ArrayList<QuestionSet> questionSets) {
		if (questionSets == null
				|| questionSets.size() < MIN_SETS
				|| questionSets.size() > MAX_SETS)
			return false;

		for (QuestionSet set : questionSets) {
			if (set == null
					|| set.getQuestions() == null
					|| set.getQuestions().size() < MIN_QUESTIONS
					|| set.getQuestions().size() > MAX_QUESTIONS)
				return false;

			for (Question question : set.getQuestions()) {
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

	////////////////////////
	//  YAY JAVAFX UTILS  //
	private static final boolean __BEGIN_JAVAFX_UTILS = false;
	////////////////////////

	/**
	 * Creates a tree item
	 *
	 * @param resourceLoaderClass
	 * @param title
	 * @param iconLocation
	 * @param imgSize
	 * @return
	 */
	public static TreeItem<String> createTreeItem(Class resourceLoaderClass,
	                                              String title,
	                                              String iconLocation,
	                                              int imgSize) {
		if (resourceLoaderClass == null || title == null || iconLocation == null || imgSize <= 0)
			return null;

		ImageView examsIcon = new ImageView(
				new Image(resourceLoaderClass.getResource(iconLocation).toExternalForm()));
		examsIcon.setFitHeight(imgSize);
		examsIcon.setFitWidth(imgSize);
		return new TreeItem<String>(title, examsIcon);
	}

	/**
	 * Everything should be static so don't allow initialization of the class.
	 */
	private GenericUtils() {
	}
}