package com.gmail.guyfleeman.vasolsim;


import com.gmail.guyfleeman.vasolsim.common.file.VaSOLSimTestFile;

import javax.crypto.Cipher;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p></p>
 */
public class VaSOLSimTestRunner
{
	public static void main(String[] args) throws Exception
	{
		System.out.println(VaSOLSimTestFile.convertHashToString(VaSOLSimTestFile.generateHash("m3chdud3m@n".getBytes(), (short) 160)));
		System.out.println(VaSOLSimTestFile.convertHashToString(VaSOLSimTestFile.generateHash("m3chdud3m@n".getBytes(), (short) 256)));
		System.out.println(VaSOLSimTestFile.convertHashToString(VaSOLSimTestFile.generateHash("m3chdud3m@n".getBytes(), (short) 384)));
		System.out.println(VaSOLSimTestFile.convertHashToString(VaSOLSimTestFile.generateHash("m3chdud3m@n".getBytes(), (short) 512)));

		byte[] passwordHash = VaSOLSimTestFile.generateHash("m3chdud3m@n".getBytes(), (short) 256);
		Cipher ec = VaSOLSimTestFile.getEncryptionCipher(passwordHash);
		Cipher dc = VaSOLSimTestFile.getDecryptionCipher(passwordHash);

		String message = "Hello Crypto World!";
		System.out.println(message);
		byte[] encMessage = ec.doFinal(message.getBytes());
		System.out.println(new String(encMessage));
		String decMessage = new String(dc.doFinal(encMessage));
		System.out.println(decMessage);
	}
}
