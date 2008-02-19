/*********************************************************************
 *
 *      Copyright (C) 2002-2005 Nathan Fiedler
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * $Id: MainWindow.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.ui;

import com.bluemarsh.benoit.Main;
import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.action.ActionTable;
import com.bluemarsh.benoit.model.BenoitNumber;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

/**
 * This class implements the main window of the application.
 *
 * @author  Nathan Fiedler
 */
public class MainWindow extends JFrame {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Suffix added to commnd strings to find images. */
    private static final String MENU_IMAGE_SUFFIX = "MenuImage";
    /** Suffix added to commnd strings to find images. */
    private static final String TOOLBAR_IMAGE_SUFFIX = "ToolbarImage";
    /** Suffix added to commnd strings to find labels. */
    private static final String LABEL_SUFFIX = "Label";
    /** Suffix added to commnd strings to find tooltips. */
    private static final String TIP_SUFFIX = "Tooltip";
    /** Toolbar instance. */
    private JToolBar toolbar;

    /**
     * Creates a MainWindow object and puts up the main window.
     * Also creates a window listener to close the program when the
     * close button is activated (using the ExitAction class).
     */
    public MainWindow() {
        // Call superclass for default behavior.
        super(Bundle.getString("AppTitle"));
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new Closer());

        // Compute the default window position (centered).
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPt = ge.getCenterPoint();
        Rectangle maxBounds = ge.getMaximumWindowBounds();
        int width = Math.min(800, maxBounds.width);
        int height = Math.min(650, maxBounds.height);
        int xpos = ((int) centerPt.getX()) - width / 2;
        int ypos = ((int) centerPt.getY()) - height / 2;
        setSize(width, height);
        setLocation(xpos, ypos);

        try {
            // Create a menubar and add all the menus to it.
            JMenuBar mb = new JMenuBar();
            String[] menuKeys = tokenize(Bundle.getString("menubar"));
            for (int i = 0; i < menuKeys.length; i++) {
                JMenu m = createMenu(menuKeys[i]);
                if (m != null) {
                    mb.add(m);
                }
            }
            setJMenuBar(mb);

            // Create a toolbar and add it to the window.
            createToolbar();
            getContentPane().add(toolbar, BorderLayout.NORTH);
        } catch (Exception e) {
            // Catch any and all exceptions. This allows our
            // caller to still invoke "show" on us. That way,
            // even if we failed to build out the UI the user
            // can close the program via the window close button.
            e.printStackTrace();
        }
    } // MainWindow

    /**
     * Create a menu for the app. By default this pulls the
     * definition of the menu from the associated resource file.
     *
     * @param  key  menu key in resource bundle
     * @return  menu built out
     */
    protected JMenu createMenu(String key) {
        // Create the menu.
        JMenu menu = new JMenu(Bundle.getString(key + LABEL_SUFFIX));
        URL url = Bundle.getResource(key + MENU_IMAGE_SUFFIX);
        if (url != null) {
            menu.setHorizontalTextPosition(JButton.RIGHT);
            menu.setIcon(new ImageIcon(url));
        }

        // Scan for menu items for this menu.
        int i = 0;
        String[] itemKeys = tokenize(Bundle.getString(key));
        while (i < itemKeys.length) {
            if (itemKeys[i].equals("-")) {
                // A "-" means insert a separator.
                menu.addSeparator();
            } else if (itemKeys[i].equals(">")) {
                // A ">" signals that the next key is a submenu name.
                menu.add(createMenu(itemKeys[++i]));
            } else if (itemKeys[i].startsWith("@")) {
                // Item is a special menu.
                itemKeys[i] = itemKeys[i].substring(1);
                JMenu smenu = null;
                if (itemKeys[i].equals("precision")) {
                    // Designer wants the precision menu.
                    smenu = new PrecisionMenu(
                        Bundle.getString("precisionMenuLabel"));
                    menu.add(smenu);
                } else {
                    throw new IllegalArgumentException(
                        "invalid special menu " + itemKeys[i]);
                }
                if (smenu != null) {
                    url = Bundle.getResource("clearImage");
                    if (url != null) {
                        smenu.setHorizontalTextPosition(JButton.RIGHT);
                        smenu.setIcon(new ImageIcon(url));
                    }
                }
            } else {
                // A normal menu item...
                menu.add(createMenuItem(itemKeys[i]));
            }
            i++;
        }
        return menu;
    } // createMenu

    /**
     * This is the hook through which all menu items are
     * created. Using the <code>cmd</code> string it finds
     * the menu item label in the resource bundle.
     *
     * @param  cmd  action command string for this menu item;
     *              used to get the label.
     * @return  new menu item
     * @see #createMenu
     */
    protected JMenuItem createMenuItem(String cmd) {
        // Create menu item and set text.
        JMenuItem mi = new JMenuItem(Bundle.getString(cmd + LABEL_SUFFIX));
        URL url = Bundle.getResource(cmd + MENU_IMAGE_SUFFIX);
        if (url != null) {
            mi.setHorizontalTextPosition(JButton.RIGHT);
            mi.setIcon(new ImageIcon(url));
        }
        // Set menu action command.
        mi.setActionCommand(cmd);
        Action a = ActionTable.getAction(cmd);
        if (a != null) {
            // Set up the action to listen for events.
            mi.addActionListener(a);
            a.addPropertyChangeListener(new ActionChangedListener(mi));
            mi.setEnabled(a.isEnabled());
        } else {
            // No action! Disable menu item.
            mi.setEnabled(false);
        }
        return mi;
    } // createMenuItem

    /**
     * Create the toolbar. By default this reads the resource file for
     * the definition of the toolbar.
     */
    protected void createToolbar() {
        toolbar = new JToolBar();
        // We don't want the toolbar moving around our carefully
        // crafted window, which has components in the other regions.
        toolbar.setFloatable(false);
        String[] toolKeys = tokenize(Bundle.getString("toolbar"));
        for (int i = 0; i < toolKeys.length; i++) {
            if (toolKeys[i].equals("-")) {
                toolbar.add(Box.createHorizontalStrut(5));
            } else {
                createToolbarButton(toolKeys[i]);
            }
        }
        toolbar.add(Box.createHorizontalStrut(10));
    } // createToolbar

    /**
     * Create a button and add it to the toolbar. This will find the
     * associated action and add it as a listener to the button.
     * Also the tooltip and toolbar image are added to the button.
     *
     * @param  key  key in resource bundle for tool.
     * @return  new button.
     */
    public JButton createToolbarButton(String key) {
        // create the button
        URL url = Bundle.getResource(key + TOOLBAR_IMAGE_SUFFIX);
        JButton b = url != null
            ? new JButton(new ImageIcon(url)) : new JButton(key);
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1, 1, 1, 1));

        // get action and attach it to button as a listener
        b.setActionCommand(key);
        Action a = ActionTable.getAction(key);
        if (a != null) {
            b.addActionListener(a);
            a.addPropertyChangeListener(new ActionChangedListener(b));
            b.setEnabled(a.isEnabled());
        } else {
            b.setEnabled(false);
        }

        // attach tooltip to button
        String tip = Bundle.getString(key + TIP_SUFFIX);
        if (tip != null) {
            // Use HTML for multi-line tooltips.
            // The font seems awfully big, so let's shrink it.
            tip = "<html><font size=\"-1\">" + tip + "</font></html>";
            b.setToolTipText(tip);
        }
        toolbar.add(b);
        return b;
    } // createToolbarButton

    /**
     * Take the given string and chop it up into a series of strings
     * on whitespace boundries. This is useful for trying to get an
     * array of strings out of the resource file. If input is null,
     * returns a zero-length array of String.
     *
     * @param  input  string to be split apart.
     * @return  array of strings from input.
     */
    protected static String[] tokenize(String input) {
        if (input == null) {
            return new String[0];
        }
        StringTokenizer t = new StringTokenizer(input);
        int size = t.countTokens();
        String strings[] = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = t.nextToken();
        }
        return strings;
    } // tokenize

    /**
     * Watches for changes in the actions and deals with them by
     * changing the corresponding menu items or toolbar buttons.
     */
    protected class ActionChangedListener implements PropertyChangeListener {
        /** Component we are associated with. */
        private JComponent component;

        /**
         * Constructor for our action change listener.
         *
         * @param  c  component we are to associate with.
         */
        public ActionChangedListener(JComponent c) {
            super();
            component = c;
        } // ActionChangedListener

        /**
         * Handles changes in the action. If the action name
         * changed we change our menu name. If the action changed
         * it's enabled state, we change our component's state.
         *
         * @param  e  property change event
         */
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                if (component instanceof JMenuItem) {
                    String text = (String) e.getNewValue();
                    ((JMenuItem) component).setText(text);
                }
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                component.setEnabled(enabledState.booleanValue());
            }
        } // propertyChange
    } // ActionChangedListener

    /**
     * Handles the window close operation.
     */
    protected class Closer extends WindowAdapter {

        /**
         * Window is closing.
         *
         * @param  e  window event.
         */
        public void windowClosing(WindowEvent e) {
            Main.closeSet(MainWindow.this);
        } // windowClosing
    } // Closer

    /**
     * Specialized menu class that implements the numeric precision menu
     * for this program. It automatically builds out the menu for selecting
     * the available numeric precisions.
     *
     * <p>This is one of the available special menus. It is requested in
     * the resources file using the "@precision" special menu tag.</p>
     *
     * @author  Nathan Fiedler
     */
    protected class PrecisionMenu extends JMenu implements ItemListener {
        /** silence the compiler warnings */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a PrecisionMenu with the given name.
         *
         * @param  name  title for this menu.
         */
        public PrecisionMenu(String name) {
            super(name, true);

            // Create menu items for changing precision.
            ButtonGroup group = new ButtonGroup();

            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(
                Bundle.getString("fastDoublesLabel"));
            menuItem.setSelected(true);
            menuItem.setActionCommand("fastDoubles");
            add(menuItem);
            group.add(menuItem);
            menuItem.addItemListener(this);

            menuItem = new JRadioButtonMenuItem(
                Bundle.getString("bigDecimalLabel"));
            menuItem.setActionCommand("bigDecimal");
            add(menuItem);
            group.add(menuItem);
            menuItem.addItemListener(this);
        } // PrecisionMenu

        /**
         * One of the look & feels was selected. See which one it was
         * and switch the entire user interface to that look & feel.
         *
         * @param  e  Indicates which item was selected.
         */
        public void itemStateChanged(ItemEvent e) {
            JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
            if (rb.isSelected()) {
                Set set = SetFrameMapper.getSetForEvent(e);
                String name = rb.getActionCommand();
                if (name.equals("fastDoubles")) {
                    set.setNumberType(BenoitNumber.DOUBLE_TYPE);
                } else if (name.equals("bigDecimal")) {
                    set.setNumberType(BenoitNumber.BIG_TYPE);
                } else {
                    throw new IllegalArgumentException(
                        "invalid menu item " + name);
                }
            }
        } // itemStateChanged
    } // PrecisionMenu
} // MainWindow
