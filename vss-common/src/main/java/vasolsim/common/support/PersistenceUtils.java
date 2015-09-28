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
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
	public static String DEFAULT_PERSIST_DIR = System.getProperty("user.home") + "/.vss/plugins/";
	public static String DEFAULT_SYS_DIR = "sys/tmp/";
	public static String DEFAULT_VSS_TEACHERCLIENT_DIR = DEFAULT_PERSIST_DIR + "vss-teacherClient/";
	public static String DEFAULT_VSS_STUDENTCLIENT_DIR = DEFAULT_PERSIST_DIR + "vss-studentClient/";

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

	/**
	 * creates the core file system for all vss programs
	 * @return success
	 */
	public static boolean initCore()
	{
		return initCore(DEFAULT_PERSIST_DIR);
	}

	public static boolean initCore(@Nonnull String dir)
	{
		return new File(dir).isDirectory() || new File(dir).mkdirs();
	}

	/**
	 *
	 * @param master
	 * @param existing
	 * @param dest
	 * @param tmp
	 * @param loader
	 * @throws IOException
	 */
	public static void initPersistentFS(@Nonnull File master,
	                                    @Nonnull File existing,
	                                    @Nonnull File dest,
	                                    @Nonnull File tmp,
	                                    @Nullable Class loader) throws IOException
	{
		initPersistentFS(master, existing, dest, tmp, loader, null);
	}

	/**
	 *
	 * @param master
	 * @param existing
	 * @param dest
	 * @param tmp
	 * @param loader
	 * @param logger
	 * @throws IOException
	 */
	public static void initPersistentFS(@Nonnull File master,
	                                    @Nonnull File existing,
	                                    @Nonnull File dest,
	                                    @Nonnull File tmp,
	                                    @Nullable Class loader,
	                                    @Nullable Logger logger) throws IOException
	{
		if (loader != null)
		{
			System.out.println("classloading master directory (" + loader.getClass().getResource(master.toString()).toExternalForm() + ") copy into tmp (" + tmp.toString() + ")");
			FileUtils.copyDirectoryToDirectory(new File(loader.getClass().getResource(master.toString())
			                                                  .toExternalForm()), tmp);
		}
		else
		{
			System.out.println("loading master directory (" + master.toString() + ") copy into tmp (" + tmp.toString() + ")");
			FileUtils.copyDirectoryToDirectory(master, tmp);
		}

		if (existing.isDirectory())
		{
			System.out.println("copying existing resource pool (" + existing.toString() + ") into tmp (" + tmp.toString() + ")");
			FileUtils.copyDirectory(existing, tmp);
		}
		else
		{
			System.out.println("existing resource pool not found. Will create directory and use as additional destination target");
			existing.mkdirs();
		}

		if (existing.isDirectory())
		{
			System.out.println("copying merged resource pool back into existing");
			FileUtils.copyDirectory(tmp, existing);
		}

		System.out.println("copying merged resource pool to destination");
		FileUtils.copyDirectory(tmp, dest);

		System.out.println("cleaning tmp");
		FileUtils.deleteDirectory(tmp);
	}
}
