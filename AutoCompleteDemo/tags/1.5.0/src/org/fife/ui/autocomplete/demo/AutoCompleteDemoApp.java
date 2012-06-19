/*
 * 12/21/2008
 *
 * AutoCompleteDemoApp.java - A demo program for the auto-completion library.
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

import java.awt.*;
import javax.swing.*;

import org.fife.ui.autocomplete.*;


/**
 * A program that demonstrates use of auto-completion.  It creates a simple
 * C source editor with context sensitive auto-completion.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class AutoCompleteDemoApp extends JFrame {


	/**
	 * Constructor.
	 */
	public AutoCompleteDemoApp() {
		this(null);
	}


	/**
	 * Constructor.
	 *
	 * @param provider The completion provider for the editor to use.
	 */
	public AutoCompleteDemoApp(CompletionProvider provider) {
		setRootPane(new DemoRootPane(provider));
		setTitle("AutoCompletion Demo");
		setSize(new Dimension(500,600));//pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/**
	 * Program entry point.
	 *
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String laf = UIManager.getSystemLookAndFeelClassName();
//laf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
				try {
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
					e.printStackTrace();
				}
				AutoCompleteDemoApp frame = new AutoCompleteDemoApp();
				frame.getToolkit().setDynamicLayout(true);
				frame.setVisible(true);
			}
		});

	}


}