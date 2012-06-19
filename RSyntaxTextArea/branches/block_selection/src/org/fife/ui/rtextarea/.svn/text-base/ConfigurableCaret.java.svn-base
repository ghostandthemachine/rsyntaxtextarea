/*
 * 12/21/2004
 *
 * ConfigurableCaret.java - The caret used by RTextArea.
 * Copyright (C) 2004 Robert Futrell
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
package org.fife.ui.rtextarea;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;


/**
 * The caret used by {@link RTextArea}.  This caret has all of the properties
 * that <code>javax.swing.text.DefaultCaret</code> does, as well as adding the
 * following niceties:
 *
 * <ul>
 *   <li>This caret can paint itself several different ways:
 *      <ol>
 *         <li>As a vertical line (like <code>DefaultCaret</code>)</li>
 *         <li>As a slightly thicker vertical line (like Eclipse)</li>
 *         <li>As an underline</li>
 *         <li>As a "block caret"</li>
 *         <li>As a rectangle around the current character</li>
 *      </ol></li>
 *   <li>On Microsoft Windows and other operating systems that do not
 *       support system selection (i.e., selecting text, then pasting
 *       via the middle mouse button), clicking the middle mouse button
 *       will cause a regular paste operation to occur.  On systems
 *       that support system selection (i.e., all UNIX variants),
 *       the middle mouse button will behave normally.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 0.6
 */
public class ConfigurableCaret extends DefaultCaret {

	/**
	 * The minimum value of a caret style.
	 */
	public static final int MIN_STYLE				= 0;

	/**
	 * The vertical line style.
	 */
	public static final int VERTICAL_LINE_STYLE		= 0;

	/**
	 * The horizontal line style.
	 */
	public static final int UNDERLINE_STYLE			= 1;

	/**
	 * The block style.
	 */
	public static final int BLOCK_STYLE			= 2;

	/**
	 * The block border style.
	 */
	public static final int BLOCK_BORDER_STYLE		= 3;

	/**
	 * A thicker vertical line (2 pixels instead of 1).
	 */
	public static final int THICK_VERTICAL_LINE_STYLE	= 4;

	/**
	 * The maximum value of a caret style.
	 */
	public static final int MAX_STYLE				= THICK_VERTICAL_LINE_STYLE;

	/**
	 * Action used to select a word on a double click.
	 */
	static private transient Action selectWord = null;

	/**
	 * Action used to select a line on a triple click.
	 */
	static private transient Action selectLine = null;

	/**
	 * holds last MouseEvent which caused the word selection
	 */
	private transient MouseEvent selectedWordEvent = null;

	/**
	 * Used for fastest-possible retrieval of the character at the
	 * caret's position in the document.
	 */
	private transient Segment seg;

	/**
	 * Whether the caret is a vertical line, a horizontal line, or a block.
	 */
	private int style;

	/**
	 * Used to help with block selections.
	 */
	private Point lastPoint;

	/**
	 * Whether a block selection is currently taking place.
	 */
	private boolean isBlockSelection;

	/**
	 * Whether a block selection exists.
	 */
	boolean blockSelectionExists;

	/**
	 * The dot of the block selection.  Only valid if
	 * {@link #blockSelectionExists} is <code>true</code>.
	 */
	private int blockSelectionDot;

	/**
	 * The mark of the block selection.  Only valid if
	 * {@link #blockSelectionExists} is <code>true</code> and
	 * {@link #isBlockSelection} is <code>false</code>.
	 */
	private int blockSelectionMark;

	/**
	 * The selection painter.  By default this paints selections with the
	 * text area's selection color.
	 */
	private ChangeableHighlightPainter selectionPainter;

	/**
	 * Whether this is Java 1.4.
	 */
	/* TODO: Remove me when 1.4 support is removed. */
	private static final boolean IS_JAVA_1_4 =
				"1.4".equals(System.getProperty("java.specification.version"));


	/**
	 * Creates the caret using {@link #VERTICAL_LINE_STYLE}.
	 */
	public ConfigurableCaret() {
		this(VERTICAL_LINE_STYLE);
	}


	/**
	 * Constructs a new <code>ConfigurableCaret</code>.
	 *
	 * @param style The style to use when painting the caret.  If this is
	 *        invalid, then {@link #VERTICAL_LINE_STYLE} is used.
	 */
	public ConfigurableCaret(int style) {
		seg = new Segment();
		setStyle(style);
		selectionPainter = new ChangeableHighlightPainter();
		lastPoint = new Point();
	}


	/**
	 * Adjusts the caret location based on the MouseEvent.
	 */
	private void adjustCaret(MouseEvent e) {
		if ((e.getModifiers()&ActionEvent.SHIFT_MASK)!=0 && getDot()!=-1)
			moveCaret(e);
		else
			positionCaret(e);
	}


	/**
	 * Adjusts the focus, if necessary.
	 *
	 * @param inWindow if true indicates requestFocusInWindow should be used
	 */
	private void adjustFocus(boolean inWindow) {
		RTextArea textArea = getTextArea();
		if ((textArea != null) && textArea.isEnabled() &&
				textArea.isRequestFocusEnabled()) {
			if (inWindow)
				textArea.requestFocusInWindow();
			else 
				textArea.requestFocus();
		}
	}


	protected void customHighlight(Point start, Point end) {

		blockSelectionExists = true;

		Highlighter h = getComponent().getHighlighter();
		TextUI ui = getComponent().getUI();
		h.removeAllHighlights();
		int y = start.y;
		int firstX = start.x;
		int lastX = end.x;

		int pos1 = ui.viewToModel(getComponent(), new Point(firstX,y));
		int pos2 = ui.viewToModel(getComponent(), new Point(lastX,y));
		try {
			h.addHighlight(pos1, pos2, selectionPainter);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		y++;

		while (y<end.y) {

			int pos1new = ui.viewToModel(getComponent(), new Point(firstX, y));
			int pos2new = ui.viewToModel(getComponent(), new Point(lastX, y));
			if (pos1!=pos1new) {
				pos1 = pos1new;
				pos2 = pos2new;
				try {
					h.addHighlight(pos1,pos2, selectionPainter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			y++;

		}

	}


	/**
	 * Overridden to damage the correct width of the caret, since this caret
	 * can be different sizes.
	 *
	 * @param r The current location of the caret.
	 */
	protected synchronized void damage(Rectangle r) {
		if (r != null) {
			validateWidth(r); // Check for "0" or "1" caret width
			x = r.x - 1;
			y = r.y;
			width = r.width + 4;
			height = r.height;
			repaint();
		}
	}


	/**
	 * Called when the UI is being removed from the
	 * interface of a JTextComponent.  This is used to
	 * unregister any listeners that were attached.
	 *
	 * @param c The text component.  If this is not an
	 *        <code>RTextArea</code>, an <code>Exception</code>
	 *        will be thrown.
	 * @see Caret#deinstall
	 */
	public void deinstall(JTextComponent c) {
		if (!(c instanceof RTextArea))
			throw new IllegalArgumentException(
					"c must be instance of RTextArea");
		super.deinstall(c);
	}


	/**
	 * Called when the caret changes position, to notify listeners.  We must
	 * unfortunately override this method so that we can set
	 * <code>blockSelectionExists</code> to <code>false</code>, if necessary,
	 * before any listeners are notified, to keep offset calculations from
	 * {@link #getDot()} and {@link #getMark()} correct.
	 */
	protected void fireStateChanged() {
		if (blockSelectionExists && !isBlockSelection) {
			System.out.println("DEBUG: fireStateChanged: Removing block selection highlights");
			blockSelectionExists = false;
			getComponent().getHighlighter().removeAllHighlights();
		}
		else {
			System.out.println("Oops - " + blockSelectionExists + ", " + isBlockSelection);//Thread.dumpStack();
		}
		super.fireStateChanged();
	}


	public int getDot() {
		//return super.getDot();
		return blockSelectionExists ? blockSelectionDot : super.getDot();
	}


	public int getMark() {
		//return super.getMark();
		return blockSelectionExists ? blockSelectionMark : super.getMark();
	}


	/**
	 * Returns whether this caret's selection uses rounded edges.
	 *
	 * @return Whether this caret's edges are rounded.
	 * @see #setRoundedSelectionEdges
	 */
	public boolean getRoundedSelectionEdges() {
		return ((ChangeableHighlightPainter)getSelectionPainter()).
								getRoundedEdges();
	}


	/**
	 * Gets the text editor component that this caret is bound to.
	 *
	 * @return The <code>RTextArea</code>.
	 */
	protected RTextArea getTextArea() {
		return (RTextArea)getComponent();
	}


	/**
	 * Gets the painter for the Highlighter.  This is overridden to return
	 * our custom selection painter.
	 *
	 * @return The painter.
	 */
	protected Highlighter.HighlightPainter getSelectionPainter() {
		return selectionPainter;
	}


	/**
	 * Gets the current style of this caret.
	 *
	 * @return The caret's style.
	 * @see #setStyle(int)
	 */
	public int getStyle() {
		return style;
	}


	/**
	 * Installs this caret on a text component.
	 *
	 * @param c The text component.  If this is not an {@link RTextArea},
	 *        an <code>Exception</code> will be thrown.
	 * @see Caret#install
	 */
	public void install(JTextComponent c) {
		if (!(c instanceof RTextArea))
			throw new IllegalArgumentException(
					"c must be instance of RTextArea");
		super.install(c);
	}


	/**
	 * Called when the mouse is clicked.  If the click was generated from
	 * button1, a double click selects a word, and a triple click the
	 * current line.
	 *
	 * @param e the mouse event
	 * @see MouseListener#mouseClicked
	 */
	public void mouseClicked(MouseEvent e) {

		if (! e.isConsumed()) {

			RTextArea textArea = getTextArea();
			int nclicks = e.getClickCount();

			if (SwingUtilities.isLeftMouseButton(e)) {
				if (nclicks<=2) {
					// Only handle these clicks for 1.4.  In 1.5 the word
					// selection is (also?) handled in mousePressed, and if we
					// handle it here, our word selection gets doubled-up.
					if (IS_JAVA_1_4) {
						if (nclicks==1) {
							selectedWordEvent = null;
						}
						else { // 2
							selectWord(e);
							selectedWordEvent = null;
						}
					}
				}
				else {
					nclicks %= 2; // Alternate selecting word/line.
					switch (nclicks) {
						case 0:
							selectWord(e);
							selectedWordEvent = null;
							break;
						case 1:
							Action a = null;
							ActionMap map = textArea.getActionMap();
							if (map != null)
								a = map.get(RTextAreaEditorKit.selectLineAction);
							if (a == null) {
								if (selectLine == null) {
									selectLine = new RTextAreaEditorKit.SelectLineAction();
								}
								a = selectLine;
							}
							a.actionPerformed(new ActionEvent(textArea,
										ActionEvent.ACTION_PERFORMED,
										null, e.getWhen(), e.getModifiers()));
					}
				}
			}

			else if (SwingUtilities.isMiddleMouseButton(e)) {
				if (nclicks == 1 && textArea.isEditable() && textArea.isEnabled()) {
					// Paste the system selection, if it exists (e.g., on UNIX
					// platforms, the user can select text, the middle-mouse click
					// to paste it; this doesn't work on Windows).  If the system
					// doesn't support system selection, just do a normal paste.
					JTextComponent c = (JTextComponent) e.getSource();
					if (c != null) {
						try {
							Toolkit tk = c.getToolkit();
							Clipboard buffer = tk.getSystemSelection();
							// If the system supports system selections, (e.g. UNIX),
							// try to do it.
							if (buffer != null) {
								adjustCaret(e);
								TransferHandler th = c.getTransferHandler();
								if (th != null) {
									Transferable trans = buffer.getContents(null);
									if (trans != null)
										th.importData(c, trans);
								}
								adjustFocus(true);
							}
							// If the system doesn't support system selections
							// (e.g. Windows), just do a normal paste.
							else {
								textArea.paste();
							}
						} catch (HeadlessException he) {
							// do nothing... there is no system clipboard
						}
					} // if (c!=null)
				} // if (nclicks == 1 && component.isEditable() && component.isEnabled())
			} // else if (SwingUtilities.isMiddleMouseButton(e))

		} // if (!e.isConsumed())

	}


	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		lastPoint = e.getPoint();
	}


	public void mousePressed(MouseEvent e) {

		if (blockSelectionExists) {
			isBlockSelection = blockSelectionExists = false;
			getComponent().getHighlighter().removeAllHighlights();
		}

		super.mousePressed(e);

		boolean blockSelStart = e.getClickCount()==1 &&
				e.getButton()==MouseEvent.BUTTON1 &&
				e.isShiftDown();
		blockSelectionExists = isBlockSelection = blockSelStart;
		if (isBlockSelection) {
			blockSelectionDot = 
				blockSelectionMark = getComponent().viewToModel(e.getPoint());
		}
	}


	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		// Must do our stuff after super.mouseReleased()
		if (isBlockSelection) {
			blockSelectionDot = getComponent().viewToModel(e.getPoint());
			isBlockSelection = false;
		}
		lastPoint.setLocation(0, 0);
	}


	protected void moveCaret(MouseEvent e) {
		if (isBlockSelection) {
			Point p = e.getPoint();
			blockSelectionDot = getComponent().viewToModel(p);
			int pos = getComponent().getUI().viewToModel(getComponent(), p);
			if (pos >= 0) {
				setDot(pos);
				Point start=new Point(Math.min(lastPoint.x,p.x),Math.min(lastPoint.y,p.y));
				Point end=new Point(Math.max(lastPoint.x,p.x),Math.max(lastPoint.y,p.y));
				customHighlight(start,end);
			}
		}
		else {
			if (blockSelectionExists) {
				System.out.println("DEBUG: moveCaret: Removing block selection highlights");
				//blockSelectionExists = false;
				getComponent().getHighlighter().removeAllHighlights();
			}
			super.moveCaret(e);
		}
	}


	/**
	 * Paints the cursor.
	 *
	 * @param g The graphics context in which to paint.
	 */
	public void paint(Graphics g) {

		// If the cursor is currently visible...
		if (isVisible()) {

			try {

				RTextArea textArea = getTextArea();
				g.setColor(textArea.getCaretColor());
				TextUI mapper = textArea.getUI();
				Rectangle r = mapper.modelToView(textArea, getDot());

				// "Correct" the value of rect.width (takes into
				// account caret being at EOL (and thus rect.width==1),
				// etc.
				// We do this even for LINE_STYLE because
				// if they change from that caret to block/underline,
				// the first time they do so width==1, so it will take
				// one caret flash to paint correctly (wider).  If we
				// do this every time, then it's painted correctly the
				// first blink.
				validateWidth(r);

				// Need to subtract 2 from height, otherwise
				// the caret will expand too far vertically.
				r.height -= 2;

				switch (style) {

					// Draw a big rectangle, and xor the foreground color.
					case BLOCK_STYLE:
						g.setXORMode(Color.WHITE);
						// fills x==r.x to x==(r.x+(r.width)-1), inclusive.
						g.fillRect(r.x,r.y, r.width,r.height);
						break;

					// Draw a rectangular border.
					case BLOCK_BORDER_STYLE:
						// fills x==r.x to x==(r.x+(r.width-1)), inclusive.
						g.drawRect(r.x,r.y, r.width-1,r.height);
						break;

					// Draw an "underline" below the current position.
					case UNDERLINE_STYLE:
						g.setXORMode(Color.WHITE);
						int y = r.y + r.height;
						g.drawLine(r.x,y, r.x+r.width-1,y);
						break;

					// Draw a vertical line.
					default:
					case VERTICAL_LINE_STYLE:
						g.drawLine(r.x,r.y, r.x,r.y+r.height);
						break;

					// A thicker vertical line.
					case THICK_VERTICAL_LINE_STYLE:
						g.drawLine(r.x,r.y, r.x,r.y+r.height);
						r.x++;
						g.drawLine(r.x,r.y, r.x,r.y+r.height);
						break;

				} // End of switch (style).

			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}

		} // End of if (isVisible()).

	}


	/**
	 * Tries to set the position of the caret from the coordinates of a mouse
	 * event, using viewToModel().<p>
	 *
	 * This is overridden to correctly handle our block selection, if any.
	 * This method is called when the user has a block selection, then clicks
	 * elsewhere with the mouse.  It's our hook allowing us to remove the
	 * block selection from the view.
	 *
	 * @param e The event.
	 */
	protected void positionCaret(MouseEvent e) {
		// We must special-case this, as super.positionCaret() calls
		// setDot(int, Position.Bias) directly.  We remove block selections in
		// setDot(int), since we cannot override the former due to our 1.4 and
		// 1.5 support, thus if a block selection exists, we must force a
		// call to setDot(int).
		// AARGH!  isBlockSelection is cleared at this point!
		//if (isBlockSelection) {
		if (blockSelectionExists) {//e.isShiftDown()) {
System.out.println("Howza");
			Point p = e.getPoint();
			int offs = getComponent().getUI().viewToModel(getComponent(), p);
			setDot(offs);
		}
		else {
			super.positionCaret(e);
		}
	}


	/**
	 * Deserializes a caret.  This is overridden to read the caret's style.
	 *
	 * @param s The stream to read from.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void readObject(ObjectInputStream s)
						throws ClassNotFoundException, IOException {
		s.defaultReadObject();
		setStyle(s.readInt());
		seg = new Segment();
	}


	/**
	 * Selects word based on the MouseEvent
	 */
	private void selectWord(MouseEvent e) {
		if (selectedWordEvent != null
				&& selectedWordEvent.getX() == e.getX()
				&& selectedWordEvent.getY() == e.getY()) {
			// We've already the done selection for this.
			return;
		}
		Action a = null;
		RTextArea textArea = getTextArea();
		ActionMap map = textArea.getActionMap();
		if (map != null) {
			a = map.get(RTextAreaEditorKit.selectWordAction);
		}
		if (a == null) {
			if (selectWord == null) {
				selectWord = new RTextAreaEditorKit.SelectWordAction();
			}
			a = selectWord;
		}
		a.actionPerformed(new ActionEvent(textArea,
							ActionEvent.ACTION_PERFORMED,
							null, e.getWhen(), e.getModifiers()));
		selectedWordEvent = e;
	}


	/**
	 * Overridden so we repaint our highlights if we're in a block selection.
	 *
	 * @param offs The offset at which to place the caret.
	 */
	/*
	 * NOTE: If we only supported Java 6+ we could just override
	 * setDot(int, Position.Bias) and not override positionCaret(int), but
	 * because of our 1.4 and 1.5 support, we must override both.
	 */
	public void setDot(int offs) {
		super.setDot(offs);

		// Must do our stuff after super.setDot()
		if (blockSelectionExists) {
			System.out.println("DEBUG: setDot: Removing block selection highlights");
						getComponent().getHighlighter().removeAllHighlights();
			//blockSelectionExists = false; // customHighlight() will reset
		}
	}


	/**
	 * Sets whether this caret's selection should have rounded edges.
	 *
	 * @param rounded Whether it should have rounded edges.
	 * @see #getRoundedSelectionEdges()
	 */
	public void setRoundedSelectionEdges(boolean rounded) {
		((ChangeableHighlightPainter)getSelectionPainter()).
								setRoundedEdges(rounded);
	}


	/**
	 * Overridden to always render the selection, even when the text component
	 * loses focus.
	 *
	 * @param visible Whether the selection should be visible.  This parameter
	 *        is ignored.
	 */
	public void setSelectionVisible(boolean visible) {
		super.setSelectionVisible(true);
	}


	/**
	 * Sets the style used when painting the caret.
	 *
	 * @param style The style to use.  If this isn't one of
	 *        <code>VERTICAL_LINE_STYLE</code>, <code>UNDERLINE_STYLE</code>,
	 *        or <code>BLOCK_STYLE</code>, then
	 *        <code>VERTICAL_LINE_STYLE</code> is used.
	 * @see #getStyle()
	 */
	public void setStyle(int style) {
		if (style<MIN_STYLE || style>MAX_STYLE)
			style = VERTICAL_LINE_STYLE;
		this.style = style;
		repaint();
	}


	/**
	 * Helper function used by the block and underline carets to ensure the
	 * width of the painted caret is valid.  This is done for the following
	 * reasons:
	 *
	 * <ul>
	 *   <li>The <code>View</code> classes in the javax.swing.text package
	 *       always return a width of "1" when <code>modelToView</code> is
	 *       called.  We'll be needing the actual width.</li>
	 *   <li>Even in smart views, such as <code>RSyntaxTextArea</code>'s
	 *       <code>SyntaxView</code> and <code>WrappedSyntaxView</code> that
	 *       return the width of the current character, if the caret is at the
	 *       end of a line for example, the width returned from
	 *       <code>modelToView</code> will be 0 (as the width of unprintable
	 *       characters such as '\n' is calculated as 0).  In this case, we'll
	 *       use a default width value.</li>
	 * </ul>
	 *
	 * @param rect The rectangle returned by the current
	 *        <code>View</code>'s <code>modelToView</code>
	 *        method for the caret position.
	 */
	private void validateWidth(Rectangle rect) {

		// If the width value > 1, we assume the View is
		// a "smart" view that returned the proper width.
		// So only worry about this stuff if width <= 1.
		if (rect!=null && rect.width<=1) {

			// The width is either 1 (most likely, we're using a "dumb" view
			// like those in javax.swing.text) or 0 (most likely, we're using
			// a "smart" view like org.fife.ui.rsyntaxtextarea.SyntaxView,
			// we're at the end of a line, and the width of '\n' is being
			// computed as 0).

			try {

				// Try to get a width for the character at the caret
				// position.  We use the text area's font instead of g's
				// because g's may vary in an RSyntaxTextArea.
				RTextArea textArea = getTextArea();
				textArea.getDocument().getText(getDot(),1, seg);
				Font font = textArea.getFont();
				FontMetrics fm = textArea.getFontMetrics(font);
				rect.width = fm.charWidth(seg.array[seg.offset]);

				// This width being returned 0 likely means that it is an
				// unprintable character (which is almost 100% to be a
				// newline char, i.e., we're at the end of a line).  So,
				// just use the width of a space.
				if (rect.width==0) {
					rect.width = fm.charWidth(' ');
				}

			} catch (BadLocationException ble) {
				// This shouldn't ever happen.
				ble.printStackTrace();
				rect.width = 8;
			}

		} // End of if (rect!=null && rect.width<=1).

	}


	/**
	 * Serializes this caret.  This is overridden to write the style of the
	 * caret.
	 *
	 * @param s The stream to write to.
	 * @throws IOException If an IO error occurs.
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeInt(getStyle());
	}


}