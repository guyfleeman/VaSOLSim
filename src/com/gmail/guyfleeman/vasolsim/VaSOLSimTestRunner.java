package com.gmail.guyfleeman.vasolsim;


import com.gmail.guyfleeman.vasolsim.common.file.VaSOLSimExamFile;

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
		System.out.println(VaSOLSimExamFile.convertHashToString(VaSOLSimExamFile.generateHash("m3chdud3m@n".getBytes(), (short) 160)));
		System.out.println(VaSOLSimExamFile.convertHashToString(VaSOLSimExamFile.generateHash("m3chdud3m@n".getBytes(), (short) 256)));
		System.out.println(VaSOLSimExamFile.convertHashToString(VaSOLSimExamFile.generateHash("m3chdud3m@n".getBytes(), (short) 384)));
		System.out.println(VaSOLSimExamFile.convertHashToString(VaSOLSimExamFile.generateHash("m3chdud3m@n".getBytes(), (short) 512)));

		byte[] passwordHash = VaSOLSimExamFile.generateHash("m3chdud3m@n".getBytes(), (short) 256);
		Cipher ec = VaSOLSimExamFile.getEncryptionCipher(passwordHash);
		Cipher dc = VaSOLSimExamFile.getDecryptionCipher(passwordHash);

		String message = "Hello Crypto World!";
		System.out.println(message);
		byte[] encMessage = ec.doFinal(message.getBytes());
		System.out.println(new String(encMessage));
		String decMessage = new String(dc.doFinal(encMessage));
		System.out.println(decMessage);
	}
}
