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

package main.java.vasolsim.common;

import javafx.scene.Scene;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author willstuckey
 * @date 8/28/15
 * <p>This class contains file system utils used to maintain the persistent data stored on file system between
 * application instances.</p>
 */
public class PersistenceUtil
{
	@Nonnull
	public static final String fsRoot = ".vss/";
	@Nonnull
	public static final String scRoot = "studentClient/";
	@Nonnull
	public static final String tcRoot = "teacherClient/";


	@Nonnull
	private static ErrorCode errorCode    = ErrorCode.NONE;
	@Nonnull
	private static String[]  topLevelDirs = {"rsc", "tmp"};
	@Nonnull
	private static Logger    logger       = Logger.getLogger(PersistenceUtil.class.getName());

	/**
	 * collection of error codes for persistence deployment stages
	 */
	public static enum ErrorCode
	{
		INTERNAL_ROOT_NOT_ABSOLUTE,
		FAILED_TO_CREATE_ROOTFS,
		FAILED_TO_CREATE_TOP_LEVEL_DIRS,
		EXTERNAL_ROOT_MALFORMED,
		RECURSIVE_EXPORT_FAILED,
		RECURSIVE_MERGE_IN_TMP_FAILED,
		RECURSIVE_COPY_TO_ROOT_FAILED,
		NONE
	}

	static
	{
		logger.setLevel(Level.TRACE);
	}

	public static boolean loadPersistentStyle(@Nonnull String root,
	                                          @Nonnull Scene scene)
	{
		return loadPersistentStyle(new File(root), scene);
	}

	public static boolean loadPersistentStyle(@Nonnull File root,
	                                          @Nonnull Scene scene)
	{
		try
		{
			File[] files = root.listFiles();
			if (files != null)
				for (File f : files)
					if (f.isDirectory())
					{
						logger.trace("searching " + f.getPath());
						loadPersistentStyle(f, scene);
					}
					else if (FilenameUtils.getExtension(f.getPath()).equals("css"))
					{
						logger.trace("loading " + f.getPath());
						scene.getStylesheets().add("file:///" + f.getPath());
					}
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	/**
	 * initializes a persistent files system for the application using a merge model based off of an internal master
	 * directory.
	 * @param externalRoot
	 * @param internalRoot
	 * @return
	 */
	public static boolean initPersistence(@Nonnull String externalRoot,
	                                      @Nonnull String internalRoot)
	{
		return initPersistence(new File(externalRoot), new File(internalRoot));
	}

	/**
	 * initializes a persistent files system for the application using a merge model based off of an internal master
	 * directory.
	 * @param externalRoot
	 * @param internalRoot
	 * @return
	 */
	public static boolean initPersistence(@Nonnull File externalRoot,
	                                      @Nonnull File internalRoot)
	{
		errorCode = ErrorCode.NONE;

		if (externalRoot.getPath().charAt(externalRoot.getPath().length() - 1) != File.separatorChar)
			externalRoot = new File(externalRoot.getPath() + File.separatorChar);

		if (internalRoot.getPath().charAt(0) != File.separatorChar)
		{
			errorCode = ErrorCode.INTERNAL_ROOT_NOT_ABSOLUTE;
			return false;
		}

		if (!externalRoot.isDirectory())
		{
			logger.debug("rootfs does not exist. creating...");
			if (externalRoot.mkdirs())
			{
				logger.info("created rootfs.");
			}
			else
			{
				errorCode = ErrorCode.FAILED_TO_CREATE_ROOTFS;
				logger.warn("failed to create rootfs. Are the permissions sufficient?");
				return false;
			}
		}

		for (String tlPath : topLevelDirs)
		{
			File tlFile = new File(externalRoot.getPath() + File.separator + tlPath);
			if (tlFile.isDirectory())
			{
				logger.trace("found top-level directory: " + tlFile.getPath());
			}
			else
			{
				if (tlFile.mkdirs())
				{
					logger.trace("created top-level directory: " + tlFile.getPath());
				}
				else
				{
					errorCode = ErrorCode.FAILED_TO_CREATE_TOP_LEVEL_DIRS;
					logger.warn("failed to create top-level directory" + tlFile.getPath());
					return false;
				}
			}
		}

		try
		{
			exportResources(internalRoot.getPath(), Paths.get(new URI("file:///" + externalRoot.getPath() +
					                                                          File.separator + "tmp")));
		}
		catch (URISyntaxException e)
		{
			errorCode = ErrorCode.EXTERNAL_ROOT_MALFORMED;
			logger.warn("external root is not a valid URI: " + externalRoot.getPath());
			return false;
		}
		catch (IOException e)
		{
			errorCode = ErrorCode.RECURSIVE_EXPORT_FAILED;
			logger.warn("recursive copy operation failed.");
			return false;
		}

		try
		{
			FileUtils.copyDirectory(new File(externalRoot.getPath() + File.separator + "rsc"),
			                                   new File(externalRoot.getPath() + File.separator + "tmp"));
		}
		catch (IOException e)
		{
			errorCode = ErrorCode.RECURSIVE_MERGE_IN_TMP_FAILED;
			logger.warn("failed to merge existing persistence into exported baseline.");
			return false;
		}

		try
		{
			FileUtils.copyDirectory(new File(externalRoot.getPath() + File.separator + "tmp"),
			                                   new File(externalRoot.getPath() + File.separator + "rsc"));
		}
		catch (IOException e)
		{
			errorCode = ErrorCode.RECURSIVE_COPY_TO_ROOT_FAILED;
			logger.warn("failed to copy merged persistence back to active root.");
			return false;
		}

		try
		{
			FileUtils.forceDeleteOnExit(new File(externalRoot.getPath() + File.separator + "tmp"));
		}
		catch (IOException e)
		{
			//we don't want to fail for this, just log it.
			logger.warn("failed to mark tmp files for deletion on exit");
		}

		return true;
	}

	/**
	 * recursively copies an internal (class-loadable) resource to a specified location on the platform file system
	 * @param source a resource internal to the jar, using absolute notation from the jar root. No bangs.
	 * @param target a location on the platform file system
	 * @throws URISyntaxException occurs when source or target path is malformed
	 * @throws IOException you name it
	 */
	protected static void exportResources(@Nonnull String source,
	                                      @Nonnull final Path target)
			throws URISyntaxException, IOException
	{
		final Path jarPath = FileSystems.newFileSystem(PersistenceUtil.class.getResource("").toURI(),
		                                               Collections.<String, String>emptyMap()).getPath(source);
		Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>()
		{
			private Path currentTarget;

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
			{
				try
				{
					currentTarget = target.resolve(jarPath.relativize(dir).toString());
					Files.createDirectories(currentTarget);
					logger.trace("visit-dir@(" + dir + "): replicated directory");
				}
				catch (IOException e)
				{
					logger.warn("visit-dir@(" + dir + "): failed to copy directory", e);
					throw e;
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				try
				{
					String ext = FilenameUtils.getExtension(file.toString());
					if (ext != null && !ext.equals("bss"))
					{
						Files.copy(file, target.resolve(jarPath.relativize(file).toString()), REPLACE_EXISTING);
						logger.trace("visit-copy@(" + file + "): copied file");
					}
					else
					{
						logger.trace("visit-skip(" + file + ")");
					}
				}
				catch (IOException e)
				{
					logger.warn("visit@(" + file + "): failed to copy file");
					throw e;
				}

				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * sets the top level directory structure required for a fs copy
	 * @param topLevelDirs
	 */
	public static void setTopLevelDirs(@Nonnull String[] topLevelDirs)
	{
		PersistenceUtil.topLevelDirs = topLevelDirs;
	}

	/**
	 * returns the error code from the last unsuccessful copy operation
	 * @return error code enum
	 */
	@Nonnull
	public static ErrorCode getErrorCode()
	{
		return errorCode;
	}

	private PersistenceUtil() {}
}
