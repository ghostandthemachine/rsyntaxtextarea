/*
 * 01/13/2009
 *
 * AutoCompleteDemoApplet.java - An applet that demos the autocompletion
 * library.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.autocomplete.demo;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * An applet version of the autocompletion demo.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class AutoCompleteDemoApplet extends JApplet {


	/**
	 * Initializes this applet.
	 */
	public void init() {
		super.init();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String laf = UIManager.getSystemLookAndFeelClassName();
				try {
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
					e.printStackTrace();
				}
				setRootPane(new DemoRootPane());
			}
		});
	}


	/**
	 * Called when this applet is made visible.  Here we request that the
	 * {@link RSyntaxTextArea} is given focus.  I tried putting this code in
	 * {@link #start()} (wrapped in SwingUtilities.invokeLater()), but it
	 * didn't seem to work there.
	 *
	 * @param visible Whether this applet should be visible.
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			((DemoRootPane)getRootPane()).focusEditor();
		}
	}


}