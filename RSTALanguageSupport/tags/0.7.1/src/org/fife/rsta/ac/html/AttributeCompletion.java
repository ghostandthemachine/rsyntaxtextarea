/*
 * 04/29/2010
 *
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This code is licensed under the LGPL.  See the "license.txt" file included
 * with this project.
 */
package org.fife.rsta.ac.html;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.ParameterizedCompletion.Parameter;


/**
 * A completion for an HTML attribute.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class AttributeCompletion extends AbstractCompletion {

	private Parameter param;


	public AttributeCompletion(CompletionProvider provider, Parameter param) {
		super(provider);
		this.param = param;
	}


	public String getSummary() {
		return param.getDescription();
	}


	public String getReplacementText() {
		return param.getName();
	}


}