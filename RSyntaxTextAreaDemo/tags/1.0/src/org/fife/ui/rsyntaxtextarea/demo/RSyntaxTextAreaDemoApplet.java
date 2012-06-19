package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.event.*;
import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;


/**
 * Demo application for {@link RSyntaxTextArea}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaDemoApplet extends JApplet
									implements SyntaxConstants {

	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;

	private static final String DEFAULT_TEXT =
		"package com.mycompany.demo;\n\n" +
		"/**\n * An example class.\n *\n * @author Your Name\n */\n" +
		"public class ExampleCode {\n\n" +
		"   /**\n" +
		"    * Creates a new example.\n" +
		"    *\n" +
		"    * @param value A value to use in the example.\n" +
		"    */\n" +
		"   public ExampleCode(int value) {\n" +
		"      this.value = value;\n" +
		"   }\n\n" +
		"   public void doWork() {\n" +
		"      // Very important work gets done here.\n" +
		"      System.out.println(\"My value is: \" + value + \"\\n\");\n" +
		"   }\n\n" +
		"   public static void main(String[] args) {\n" +
		"      ExampleCode example = new ExampleCode(5);\n" +
		"      example.doWork();\n" +
		"   }\n\n}\n";


	public RSyntaxTextAreaDemoApplet() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace(); // Never happens
		}

		textArea = createTextArea();
		scrollPane = new RTextScrollPane(500,300, textArea, true);
		getContentPane().add(scrollPane);
		setJMenuBar(createMenuBar());

	}


	private void addItem(String name, int style, ButtonGroup bg, JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
								new ChangeSyntaxStyleAction(name, style));
		bg.add(item);
		menu.add(item);
	}


	private JMenuBar createMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu menu = new JMenu("Language");
		ButtonGroup bg = new ButtonGroup();
		addItem("C", C_SYNTAX_STYLE, bg, menu);
		addItem("C#", CSHARP_SYNTAX_STYLE, bg, menu);
		addItem("Java", JAVA_SYNTAX_STYLE, bg, menu);
		addItem("Lua", LUA_SYNTAX_STYLE, bg, menu);
		addItem("Perl", PERL_SYNTAX_STYLE, bg, menu);
		addItem("SQL", SQL_SYNTAX_STYLE, bg, menu);
		addItem("XML", XML_SYNTAX_STYLE, bg, menu);
		menu.getItem(2).setSelected(true);
		mb.add(menu);

		menu = new JMenu("View");
//		JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(new MonospacedFontAction());
//		cbItem.setSelected(true);
//		menu.add(cbItem);
		JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(new ViewLineHighlightAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineNumbersAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new WordWrapAction());
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ToggleAntiAliasingAction());
		menu.add(cbItem);
		mb.add(menu);

		menu = new JMenu("Help");
		JMenuItem item = new JMenuItem(new AboutAction());
		menu.add(item);
		mb.add(menu);

		return mb;

	}


	private RSyntaxTextArea createTextArea() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.restoreDefaultSyntaxHighlightingColorScheme();
		textArea.setSyntaxEditingStyle(RSyntaxTextArea.JAVA_SYNTAX_STYLE);
		textArea.setText(DEFAULT_TEXT);
		return textArea;
	}


	private class AboutAction extends AbstractAction {

		public AboutAction() {
			putValue(NAME, "About RSyntaxTextArea...");
		}

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(RSyntaxTextAreaDemoApplet.this,
					"<html><b>RSyntaxTextArea</b> - A Swing syntax highlighting text component" +
					"<br>Licensed under the LGPL",
					"About RSyntaxTextArea",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}


	private class ChangeSyntaxStyleAction extends AbstractAction {

		private int style;

		public ChangeSyntaxStyleAction(String name, int style) {
			putValue(NAME, name);
			this.style = style;
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setSyntaxEditingStyle(style);
		}

	}

/*
 * Currently not used as some token types (i.e. keywords & comments) use a
 * different font than the default (bold/italic), and I'm too lazy to change
 * all necessary fonts.
	private class MonospacedFontAction extends AbstractAction {

		private boolean selected;

		public MonospacedFontAction() {
			putValue(NAME, "Monospaced Font");
			selected = true;
		}

		public void actionPerformed(ActionEvent e) {
			selected = !selected;
			if (selected) {
				textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			}
			else {
				textArea.setFont(null);
			}
		}

	}
*/

	private class ViewLineHighlightAction extends AbstractAction {

		public ViewLineHighlightAction() {
			putValue(NAME, "Current Line Highlight");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setCurrentLineHighlightEnabled(
					!textArea.isCurrentLineHighlightEnabled());
		}

	}


	private class ToggleAntiAliasingAction extends AbstractAction {

		private boolean selected;

		public ToggleAntiAliasingAction() {
			putValue(NAME, "Anti-Aliasing");
		}

		public void actionPerformed(ActionEvent e) {
			selected = !selected;
			String hint = selected ? "VALUE_TEXT_ANTIALIAS_ON" : null;
			textArea.setTextAntiAliasHint(hint);
		}

	}


	private class ViewLineNumbersAction extends AbstractAction {

		public ViewLineNumbersAction() {
			putValue(NAME, "Line Numbers");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setLineNumbersEnabled(
					!scrollPane.areLineNumbersEnabled());
		}

	}


	private class WordWrapAction extends AbstractAction {

		public WordWrapAction() {
			putValue(NAME, "Word Wrap");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setLineWrap(!textArea.getLineWrap());
		}

	}


}