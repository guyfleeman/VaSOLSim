package com.gmail.guyfleeman.vasolsim;

import com.gmail.guyfleeman.vasolsim.common.file.VaSOLSimExam;

import javax.crypto.Cipher;
import java.io.File;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p></p>
 */
public class VaSOLSimTestRunner
{
	public static void main(String[] args) throws Exception
	{
		docGen();

		//System.out.println(VaSOLSimExam.convertHashToString(VaSOLSimExam.generateHash("m3chdud3m@n".getBytes(), (short) 512)));
		//System.out.println(VaSOLSimExam.convertHashToString(VaSOLSimExam.generateHash("m3chdud3m@n".getBytes(), (short) 512)));

		/*
		System.out.println(VaSOLSimExam.convertHashToString(VaSOLSimExam.generateHash("m3chdud3m@n".getBytes(), VaSOLSimExam.HashType.SHA256)));
		System.out.println(VaSOLSimExam.convertHashToString(VaSOLSimExam.generateHash("m3chdud3m@n".getBytes(), VaSOLSimExam.HashType.SHA512)));

		byte[] passwordHash = VaSOLSimExam.generateHash("m3chdud3m@n".getBytes(), VaSOLSimExam.HashType.SHA256);
		Cipher ec = VaSOLSimExam.getEncryptionCipher(passwordHash);
		Cipher dc = VaSOLSimExam.getDecryptionCipher(passwordHash);

		String message = "Hello Crypto World!";
		System.out.println(message);
		byte[] encMessage = ec.doFinal(message.getBytes());
		System.out.println(new String(encMessage));
		String decMessage = new String(dc.doFinal(encMessage));
		System.out.println(decMessage);
		*/
	}

	public static void docGen() throws Exception
	{
		VaSOLSimExam exam = new VaSOLSimExam();
		exam.setReportingStatistics(true);
		exam.setReportingStatisticsEncrypted(true);
		exam.setReportingStatisticsUsingStandaloneEmailParadigm(true);
		exam.setDecryptedStatisticsEmail("guyfleeman@gmail.com");
		exam.setDecryptedStatisticsEmailPassword("guyfleeman".getBytes());

		exam.setNotifyingCompletion(true);
		exam.setDecryptedNotificationEmail("guyfleeman@gmail.com");
		exam.setDecryptedNotificationEmailPassword("guyfleeman".getBytes());

		exam.setValidationKey("m3chdud3m@n");

		exam.setTestPublisher("Will S.");

		exam.write(new File("/home/williamstuckey/Desktop/exam.xml"), true);
	}
}
