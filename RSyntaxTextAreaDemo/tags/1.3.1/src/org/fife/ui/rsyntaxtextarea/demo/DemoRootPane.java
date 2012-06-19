package org.fife.ui.rsyntaxtextarea.demo;

//import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;


/**
 * The root pane used by the demos.  This allows both the applet and the
 * standalone application to share the same UI. 
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DemoRootPane extends JRootPane implements HyperlinkListener,
											SyntaxConstants {

	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;


	public DemoRootPane() {
		textArea = createTextArea();
		scrollPane = new RTextScrollPane(500,300, textArea, true);
		Gutter gutter = scrollPane.getGutter();
		gutter.setBookmarkingEnabled(true);
		URL url = getClass().getClassLoader().getResource("bookmark.png");
		gutter.setBookmarkIcon(new ImageIcon(url));
		getContentPane().add(scrollPane);
		setJMenuBar(createMenuBar());
	}


	private void addItem(String name, String res, String style, ButtonGroup bg,
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
		addItem("C", "CExample.txt", SYNTAX_STYLE_C, bg, menu);
		addItem("Java", "JavaExample.txt", SYNTAX_STYLE_JAVA, bg, menu);
		addItem("Perl", "PerlExample.txt", SYNTAX_STYLE_PERL, bg, menu);
		addItem("Ruby", "RubyExample.txt", SYNTAX_STYLE_RUBY, bg, menu);
		addItem("SQL", "SQLExample.txt", SYNTAX_STYLE_SQL, bg, menu);
		addItem("XML", "XMLExample.txt", SYNTAX_STYLE_PHP, bg, menu);
		menu.getItem(1).setSelected(true);
		mb.add(menu);

		menu = new JMenu("View");
		// JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(new
		// MonospacedFontAction());
		// cbItem.setSelected(true);
		// menu.add(cbItem);
		JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(
				new ViewLineHighlightAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineNumbersAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new BookmarksAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new WordWrapAction());
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ToggleAntiAliasingAction());
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new MarkOccurrencesAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
//		cbItem = new JCheckBoxMenuItem(new RtlAction());
//		menu.add(cbItem);
		mb.add(menu);

		menu = new JMenu("Help");
		JMenuItem item = new JMenuItem(new AboutAction());
		menu.add(item);
		mb.add(menu);

		return mb;

	}


	private RSyntaxTextArea createTextArea() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
		textArea.setText(getText("JavaExample.txt"));
		textArea.setCaretPosition(0);
		textArea.addHyperlinkListener(this);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		return textArea;
	}


	void focusTextArea() {
		textArea.requestFocusInWindow();
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
		} catch (RuntimeException re) {
			throw re; // FindBugs
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
			JOptionPane.showMessageDialog(DemoRootPane.this,
					"<html><b>RSyntaxTextArea</b> - A Swing syntax highlighting text component" +
					"<br>Version 1.3" +
					"<br>Licensed under the LGPL",
					"About RSyntaxTextArea",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}


	private class BookmarksAction extends AbstractAction {

		public BookmarksAction() {
			putValue(NAME, "Bookmarks");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setIconRowHeaderEnabled(
							!scrollPane.isIconRowHeaderEnabled());
		}

	}


	private class ChangeSyntaxStyleAction extends AbstractAction {

		private String res;
		private String style;

		public ChangeSyntaxStyleAction(String name, String res, String style) {
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


	private class MarkOccurrencesAction extends AbstractAction {

		public MarkOccurrencesAction() {
			putValue(NAME, "Mark Occurrences");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setMarkOccurrences(!textArea.getMarkOccurrences());
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

/* Removed to keep size of demo applet down
	private class RtlAction extends AbstractAction {

		public RtlAction() {
			putValue(NAME, "Right-to-Left Orientation");
		}

		public void actionPerformed(ActionEvent e) {
			if (scrollPane.getComponentOrientation().isLeftToRight()) {
				scrollPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
			else {
				scrollPane.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		}

	}
*/

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


	private class ViewLineHighlightAction extends AbstractAction {

		public ViewLineHighlightAction() {
			putValue(NAME, "Current Line Highlight");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setHighlightCurrentLine(
					!textArea.getHighlightCurrentLine());
		}

	}


	private class ViewLineNumbersAction extends AbstractAction {

		public ViewLineNumbersAction() {
			putValue(NAME, "Line Numbers");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setLineNumbersEnabled(
					!scrollPane.getLineNumbersEnabled());
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