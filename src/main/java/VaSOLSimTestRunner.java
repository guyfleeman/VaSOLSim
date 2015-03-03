package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//import static main.java.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 6/25/14 <p></p>
 */
public class VaSOLSimTestRunner
{
	public static void main(String[] args) throws Exception
	{
		printProjectBuildSize();
	}

	/*
	public static void smtpTest()
	{
		System.out.println(isValidEmail("guyfleeman@gmail.com"));
		System.out.println(isValidAddress("smtp.gmail.com"));
		//System.out.println(canConnectToAddress("smtp.gmail.com"));
		System.out.println(isValidSMTPConfiguration("smtp.gmail.com",
		                                            587,
		                                            "guyfleeman@gmail.com",
		                                            "<mypassword>".getBytes(),
		                                            true));
	}
	*/

	/*
	 * Project scope info finder. Just for fun ;)
	 */
	private static long fileCt, lineCt, maxDepth;

	public static void printProjectBuildSize()
	{
		getBuildSizeInfo(new File("/home/willstuckey/Dropbox/PROGRAMMING/Java/VaSOLSim/project/src"), 0L);
		System.out.println("VASOLSIM SCOPE STATISTICS: numFiles=" + fileCt
				                   + ", numLines=" + lineCt);
	}

	private static void getBuildSizeInfo(File dir, long depth)
	{
		if (dir == null)
			return;

		File[] files = dir.listFiles();
		if (files == null)
			return;

		for (File f : files)
		{
			if (f.isDirectory())
			{
				if (depth + 1 > maxDepth)
					maxDepth = depth + 1;

				getBuildSizeInfo(f, depth + 1);
			}
			else if (f.isFile())
			{
				fileCt++;

				try
				{
					Scanner fileScanner = new Scanner(f);
					while (fileScanner.hasNextLine())
						lineCt++;

					fileScanner.close();
				}
				catch (FileNotFoundException e) {}
			}
		}
	}
}
