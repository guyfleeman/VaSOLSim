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

package main.java.vasolsim.common.support;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by willstuckey on 5/6/15.
 */
public class PersistenceUtils
{
	private PersistenceUtils() {}

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
		return exportResource(PersistenceUtils.class.getResource(internalResource), new File(externalResource));
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

	public static boolean initCore()
	{
		return new File(System.getProperty("user.home") + "/.vss/plugins").isDirectory() ||
				new File(System.getProperty("user.home") + "/.vss/plugins").mkdirs();
	}

	public static void initPersistentFS(File master, File dest, boolean overwrite, boolean classloadMaster) throws IOException
	{
		initPersistentFS(master, dest, overwrite, classloadMaster, false);
	}

	private static File masterCopy;
	private static void initPersistentFS(File master, File dest, boolean overwrite, boolean classloadMaster, boolean recurse) throws IOException
	{
		if (overwrite)
		{
			FileUtils.deleteDirectory(dest);
			FileUtils.copyDirectory(master, dest);
			return;
		}

		if (!master.isDirectory() && classloadMaster && PersistenceUtils.class.getResource(master.getPath()) == null)
			throw new IOException("Provided master is not a directory. " + master.getPath());

		if (!dest.isDirectory())
			if (!dest.isFile() && !dest.mkdirs())
				throw new IOException("Provided destination is not a directory and could not be created. " + dest.getPath());

		if (!recurse)
			PersistenceUtils.masterCopy = master;

		if (classloadMaster)
		{
			try
			{
				master = new File(PersistenceUtils.class.getResource(master.getPath()).toURI());
			}
			catch (URISyntaxException e)
			{
				throw new IOException("Unable to classload master.", e);
			}
		}

		File[] files = master.listFiles();
		if (files != null)
		{
			System.out.println("Master root: " + master.getAbsolutePath());

			for (File f : files)
			{
				System.out.println("RESOURCE: " + f.getPath());

				//String rel = masterCopy.toURI().relativize(f.toURI()).getPath();
				String rel = getRelativePath(masterCopy.getPath(), f.getPath(), "/");

				System.out.println("REL RESC:" + rel);
				File destFile = new File(rel); //new File(dest.getAbsolutePath() + "/" + rel);
				if (f.isFile())
				{
					if (destFile.isFile())
					{
						BufferedReader bufferedReader = new BufferedReader(new FileReader(destFile));
						if (bufferedReader.readLine() == null)
						{
							if (classloadMaster)
								FileUtils.copyURLToFile(PersistenceUtils.class.getResource(f.getPath()), destFile);
							else
								FileUtils.copyFile(f, destFile);
						}
						bufferedReader.close();
					}
					else
					{
						if (destFile.createNewFile())
						{
							if (classloadMaster)
								FileUtils.copyURLToFile(PersistenceUtils.class.getResource(f.getPath()), destFile);
							else
								FileUtils.copyFile(f, destFile);
						}
						else
							throw new IOException("Could not create file for persistence. " + destFile.getPath());
					}

				}
				else if (f.isDirectory())
				{
					if (destFile.isDirectory() || destFile.mkdirs())
						initPersistentFS(master, dest, false, classloadMaster, true);
					else
						throw new IOException("Could not create directory for persistence. " + destFile.getPath());
				}
			}
		}
	}

	public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {

		// Normalize the paths
		String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
		String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

		// Undo the changes to the separators made by normalization
		if (pathSeparator.equals("/")) {
			normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
			normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

		} else if (pathSeparator.equals("\\")) {
			normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
			normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

		} else {
			throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
		}

		String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
		String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

		// First get all the common elements. Store them as a string,
		// and also count how many of them there are.
		StringBuffer common = new StringBuffer();

		int commonIndex = 0;
		while (commonIndex < target.length && commonIndex < base.length
				&& target[commonIndex].equals(base[commonIndex])) {
			common.append(target[commonIndex] + pathSeparator);
			commonIndex++;
		}

		if (commonIndex == 0) {
			// No single common path element. This most
			// likely indicates differing drive letters, like C: and D:.
			// These paths cannot be relativized.
			throw new PathResolutionException("No common path element found for '" + normalizedTargetPath + "' and '" + normalizedBasePath
					                                  + "'");
		}

		// The number of directories we have to backtrack depends on whether the base is a file or a dir
		// For example, the relative path from
		//
		// /foo/bar/baz/gg/ff to /foo/bar/baz
		//
		// ".." if ff is a file
		// "../.." if ff is a directory
		//
		// The following is a heuristic to figure out if the base refers to a file or dir. It's not perfect, because
		// the resource referred to by this path may not actually exist, but it's the best I can do
		boolean baseIsFile = true;

		File baseResource = new File(normalizedBasePath);

		if (baseResource.exists()) {
			baseIsFile = baseResource.isFile();

		} else if (basePath.endsWith(pathSeparator)) {
			baseIsFile = false;
		}

		StringBuffer relative = new StringBuffer();

		if (base.length != commonIndex) {
			int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

			for (int i = 0; i < numDirsUp; i++) {
				relative.append(".." + pathSeparator);
			}
		}
		relative.append(normalizedTargetPath.substring(common.length()));
		return relative.toString();
	}


	static class PathResolutionException extends RuntimeException {
		PathResolutionException(String msg) {
			super(msg);
		}
	}

}
