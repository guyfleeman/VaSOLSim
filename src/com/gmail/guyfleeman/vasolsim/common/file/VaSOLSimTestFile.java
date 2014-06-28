package com.gmail.guyfleeman.vasolsim.common.file;

import com.gmail.guyfleeman.vasolsim.common.VaSolSimException;
import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p></p>
 */
public class VaSOLSimTestFile
{
	protected static byte[] IV = "V9JEIvOQqG9fOt59".getBytes();
	protected static int algorithmExpectedKeyLengthInBytes = 16;
	protected static String serviceProviderInterface = "AES/CBC/PKCS5Padding";
	protected static String serviceProvider = "SunJCE";
	protected static String algorithm = "AES";
	protected static String charsetEncoding = "UTF-8";

	private static final String ERROR_MESSAGE_FILE_ALREADY_EXISTS =
			"File already exists and overwrite not permitted.";
	private static final String ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK =
			"File not found after internal check. Try running as admin. Is this a bug or a file permissions error?";
	private static final String ERROR_MESSAGE_COULD_NOT_CREATE_DIRS =
			"Could not create file directory. Do you have permission to create a directory on your machine? " +
					"(try running the jar as admin)";
	private static final String ERROR_MESSAGE_COULD_NOT_CREATE_FILE =
			"Could not create file. Do you have permission to create a file on your machine? " +
					"(try running the jar as admin)";
	private static final String ERROR_MESSAGE_CREATE_FILE_EXCEPTION =
			"File creation internal exception. Do you have permission to create a file on your machine? Could this be " +
					"a bug? (try running the jar as admin)";
	private static final String ERROR_MESSAGE_GENERIC_CRYPTO =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager.";

	public void read() throws IOException
	{

	}

	/**
	 * Writes a digitized version of the VaSOLSim test, represented by an instance of this class, to a given file in
	 * XML format. Will not overwrite by default.
	 * @param simFile the file on the disk to be written
	 * @return if the write operation was successful
	 * @throws VaSolSimException thrown if insufficient information to write the file is contained in VaSolSimTest
	 * object. Please ensure a password is provided to protect answer data and potentially email data if test statistics
	 * and notifications are reported.
	 */
	public boolean write(File simFile)throws VaSolSimException
	{
		return write(simFile, false);
	}

	/**
	 * Writes a digitized version of the VaSOLSim test, represented by an instance of this class, to a given file in
	 * XML format.
	 * @param simFile the file on the disk to be written
	 * @param canOverwriteFile can this method call write over an existing file with content
	 * @return if the write operation was successful
	 * @throws VaSolSimException thrown if insufficient information to write the file is contained in VaSolSimTest
	 * object. Please ensure a password is provided to protect answer data and potentially email data if test statistics
	 * and notifications are reported.
	 */
	public boolean write(File simFile, boolean canOverwriteFile) throws VaSolSimException
	{
		if (simFile.isFile())
		{
			if (!canOverwriteFile)
			{
				PopupManager.showMessage(ERROR_MESSAGE_FILE_ALREADY_EXISTS);
				return false;
			}
			else
			{
				PrintWriter printWriter;
				try
				{
					printWriter = new PrintWriter(simFile);
				}
				catch (FileNotFoundException e)
				{
					PopupManager.showMessage(ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK);
					return false;
				}

				printWriter.print("");
				printWriter.close();
			}
		}
		else
		{
			if (simFile.getParentFile().isDirectory() || !simFile.getParentFile().mkdirs())
			{
				PopupManager.showMessage(ERROR_MESSAGE_COULD_NOT_CREATE_DIRS);
				return false;
			}

			try
			{
				if (!simFile.createNewFile())
				{
				 	PopupManager.showMessage(ERROR_MESSAGE_COULD_NOT_CREATE_FILE);
					return false;
				}
			}
			catch (IOException e)
			{
				PopupManager.showMessage(ERROR_MESSAGE_CREATE_FILE_EXCEPTION);
				return false;
			}
		}

		//TODO write xml

		return true;
	}

	/**
	 * Generates a hash from a password with a given key length using a Secure Hash Algorithm (SHA)
	 * @param password the password form which the hash will be generated
	 * @param keyLen the length in bytes of the hash, defaults to SHA-256 if an invalid value is given. Valid values are
	 *               160 (SHA-1), 256 (SHA-256), 384 (SHA-384), 512 (SHA-512).
	 * @return hash
	 */
	//TODO revert to protected cope
	public static byte[] generateHash(byte[] password, short keyLen)
	{
		String hashAlgorithm;
		switch (keyLen)
		{
			case 160: hashAlgorithm = "SHA-1"; break;
			case 256: hashAlgorithm = "SHA-256"; break;
			case 384: hashAlgorithm = "SHA-384"; break;
			case 512: hashAlgorithm = "SHA-512"; break;
			default: hashAlgorithm = "SHA-256";
		}

		try
		{
			MessageDigest hash = MessageDigest.getInstance(hashAlgorithm);
			hash.update(password);
			return hash.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			return new byte[]{-1};
		}
	}

	/**
	 * Converts a byte array hash to a character representation for storage and comparison
	 * @param hash the hash to convert to plain text
	 * @return plain text representation of the hash value
	 */
	//TODO revert to protected scope
	public static String convertHashToString(byte[] hash)
	{
	 	StringBuilder hashString = new StringBuilder();
		for (int index = 0; index < hash.length; index++)
			hashString.append(Integer.toString((hash[index] & 0xff) + 0x0100, 16).substring(1));

		return hashString.toString();
	}

	/**
	 * Creates a cipher around a 128bit AES encryption initialized to decryption mode.
	 * @param key the key for the cipher
	 * @return initialized cipher, null if there is an error. The popup manager is used to notify errors.
	 */
	//TODO revert to protected scope
	public static Cipher getDecryptionCipher(byte[] key)
	{
		byte[] validatedKey = new byte[algorithmExpectedKeyLengthInBytes];
		if (key.length < algorithmExpectedKeyLengthInBytes)
		{
			int invalidatedKeyIndex = 0;
			for (int index = 0; index < algorithmExpectedKeyLengthInBytes; index++)
			{
				validatedKey[index] = key[invalidatedKeyIndex++];

				if (index >= key.length)
					invalidatedKeyIndex = 0;
			}
		}
		else if (key.length > algorithmExpectedKeyLengthInBytes)
			for (int index = 0; index < algorithmExpectedKeyLengthInBytes; index++)
				validatedKey[index] = key[index];
		else if (key.length == algorithmExpectedKeyLengthInBytes)
			validatedKey = key;

		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance(serviceProviderInterface, serviceProvider);
			cipher.init(
					Cipher.DECRYPT_MODE,
					new SecretKeySpec(validatedKey, algorithm),
					new IvParameterSpec(IV));
		}
		catch (NoSuchAlgorithmException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (NoSuchProviderException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PROVIDER\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (NoSuchPaddingException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PADDING\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (InvalidKeyException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD KEY\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (InvalidAlgorithmParameterException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM PARAMS\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}

	 	return cipher;
	}

	/**
	 * Creates a cipher around a 128bit AES encryption initialized to encryption mode.
	 * @param key the key for the cipher
	 * @return initialized cipher
	 */
	//TODO revert to protected scope
	public static Cipher getEncryptionCipher(byte[] key)
	{
		byte[] validatedKey = new byte[algorithmExpectedKeyLengthInBytes];
		if (key.length < algorithmExpectedKeyLengthInBytes)
		{
			int invalidatedKeyIndex = 0;
			for (int index = 0; index < algorithmExpectedKeyLengthInBytes; index++)
			{
				validatedKey[index] = key[invalidatedKeyIndex++];

				if (index >= key.length)
					invalidatedKeyIndex = 0;
			}
		}
		else if (key.length > algorithmExpectedKeyLengthInBytes)
			for (int index = 0; index < algorithmExpectedKeyLengthInBytes; index++)
				validatedKey[index] = key[index];
		else if (key.length == algorithmExpectedKeyLengthInBytes)
			validatedKey = key;

		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance(serviceProviderInterface, serviceProvider);
			cipher.init(
					Cipher.ENCRYPT_MODE,
					new SecretKeySpec(validatedKey, algorithm),
					new IvParameterSpec(IV));
		}
		catch (NoSuchAlgorithmException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (NoSuchProviderException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PROVIDER\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (NoSuchPaddingException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PADDING\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (InvalidKeyException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD KEY\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}
		catch (InvalidAlgorithmParameterException e)
		{
			PopupManager.showMessage(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM PARAMS\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e));
		}

		return cipher;
	}
}
