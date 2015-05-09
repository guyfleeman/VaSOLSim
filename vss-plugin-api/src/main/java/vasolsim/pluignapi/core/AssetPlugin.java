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

package main.java.vasolsim.pluignapi.core;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.Capabilities;
import javax.annotation.Nullable;


/**
 * Created by willstuckey on 5/6/15.
 */
public interface AssetPlugin extends Plugin
{
	/**
	 * The URL used for a remote update.
	 * @return
	 */
	@Nullable
	public String getRemoteURL();

	/**
	 * A user navigable URL from the plugin's maintainer.
	 * @return
	 */
	@Nullable
	public String getInfoURL();

	/**
	 * The capabilities of the plugin.
	 * @return
	 */
	@Nullable
	@Capabilities
	public String[] capabilities();
}
