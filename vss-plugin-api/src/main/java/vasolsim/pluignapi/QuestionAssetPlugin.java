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

package main.java.vasolsim.pluignapi;

import main.java.vasolsim.pluignapi.core.AssetPlugin;
import main.java.vasolsim.common.node.DrawableParent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by willstuckey on 5/6/15.
 */
public interface QuestionAssetPlugin extends AssetPlugin
{
	/**
	 * Returns the names of the types of questions the plugin can create. For each given question name, the plugin
	 * must be able to create the associated UI nodes.
	 * @return
	 */
	@Nullable
	public String[] getQuestionNames();

	/**
	 * Returns the node that allows the user of the student client to view the question and log a response.
	 * @param questionName the name of the question, indicates to the plugin for what type of question a node needs to
	 *                     be generated. The question name is guaranteed to match at least one String from
	 *                     getQuestionNames() and to not be null.
	 * @return the generated node
	 */
	@Nonnull
	public DrawableParent getQuestionNode(@Nonnull String questionName);

	/**
	 * Returns the node that allows the user of the teacher client to create the question.
	 * @param questionName the name of the question, indicates to the plugin for what type of question a node needs to
	 *                     be generated.The question name is guaranteed to match at least one String from
	 *                     getQuestionNames() and to not be null.
	 * @return the generated node
	 */
	@Nonnull
	public DrawableParent getQuestionInitializerNode(@Nonnull String questionName);
}
