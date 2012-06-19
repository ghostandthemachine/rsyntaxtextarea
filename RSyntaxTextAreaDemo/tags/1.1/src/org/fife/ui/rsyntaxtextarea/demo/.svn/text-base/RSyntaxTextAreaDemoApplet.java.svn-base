package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;


/**
 * Demo application for {@link RSyntaxTextArea}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaDemoApplet extends JApplet
							implements HyperlinkListener, SyntaxConstants {

	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;


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


	private void addItem(String name, String res, int style, ButtonGroup bg,
							JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
							new ChangeSyntaxStyleAction(name, res, style));
		bg.add(item);
		menu.add(item);
	}


	private JMenuBar createMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu menu = new JMenu("Language");
		ButtonGroup bg = new ButtonGroup();
		addItem("C", "CExample.txt", C_SYNTAX_STYLE, bg, menu);
		addItem("Java", "JavaExample.txt", JAVA_SYNTAX_STYLE, bg, menu);
		addItem("Perl", "PerlExample.txt", PERL_SYNTAX_STYLE, bg, menu);
		addItem("Ruby", "RubyExample.txt", RUBY_SYNTAX_STYLE, bg, menu);
		addItem("SQL", "SQLExample.txt", SQL_SYNTAX_STYLE, bg, menu);
		addItem("XML", "XMLExample.txt", XML_SYNTAX_STYLE, bg, menu);
		menu.getItem(1).setSelected(true);
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
		textArea.setText(getText("JavaExample.txt"));
		textArea.addHyperlinkListener(this);
		return textArea;
	}


	private String getText(String resource) {
		ClassLoader cl = getClass().getClassLoader();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(
					cl.getResourceAsStream(resource), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line=r.readLine())!=null) {
				sb.append(line).append('\n');
			}
			r.close();
			return sb.toString();
		} catch (Exception e) {
			return "Type here to see syntax highlighting";
		}
	}


	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
			URL url = e.getURL();
			if (url==null) {
				UIManager.getLookAndFeel().provideErrorFeedback(null);
			}
			else {
				JOptionPane.showMessageDialog(this,
									"URL clicked:\n" + url.toString());
			}
		}
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

		private String res;
		private int style;

		public ChangeSyntaxStyleAction(String name, String res, int style) {
			putValue(NAME, name);
			this.res = res;
			this.style = style;
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setText(getText(res));
			textArea.setCaretPosition(0);
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
			textArea.setHighlightCurrentLine(
					!textArea.getHighlightCurrentLine());
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