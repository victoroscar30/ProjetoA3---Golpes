package ui;

/**
 * @(#)MenuScroller.java	1.5.0 04/02/12
 */
//package darrylbu.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 * A class that provides scrolling capabilities to a long menu dropdown or
 * popup menu.  A number of items can optionally be frozen at the top and/or
 * bottom of the menu.
 * <P>
 * <B>Implementation note:</B>  The default number of items to display
 * at a time is 15, and the default scrolling interval is 125 milliseconds.
 * <P>
 *
 * @version 1.5.0 04/05/12
 * @author Darryl
 */
public class MenuScroller {

    //private JMenu menu;
    private JPopupMenu menu;
    private Component[] menuItems;
    private MenuScrollItem upItem;
    private MenuScrollItem downItem;
    private final MenuScrollListener menuListener = new MenuScrollListener();
    private int scrollCount;
    private int interval;
    private int topFixedCount;
    private int bottomFixedCount;
    private int firstIndex = 0;
    private int keepVisibleIndex = -1;

    /**
     * Registers a menu to be scrolled with the default number of items to
     * display at a time and the default scrolling interval.
     *
     * @param menu the menu
     * @return the MenuScroller
     */
    public static MenuScroller setScrollerFor(JMenu menu) {
        return new MenuScroller(menu);
    }

    /**
     * Registers a popup menu to be scrolled with the default number of items to
     * display at a time and the default scrolling interval.
     *
     * @param menu the popup menu
     * @return the MenuScroller
     */
    public static MenuScroller setScrollerFor(JPopupMenu menu) {
        return new MenuScroller(menu);
    }

    /**
     * Registers a menu to be scrolled with the default number of items to
     * display at a time and the specified scrolling interval.
     *
     * @param menu the menu
     * @param scrollCount the number of items to display at a time
     * @return the MenuScroller
     * @throws IllegalArgumentException if scrollCount is 0 or negative
     */
    public static MenuScroller setScrollerFor(JMenu menu, int scrollCount) {
        return new MenuScroller(menu, scrollCount);
    }

    /**
     * Registers a popup menu to be scrolled with the default number of items to
     * display at a time and the specified scrolling interval.
     *
     * @param menu the popup menu
     * @param scrollCount the number of items to display at a time
     * @return the MenuScroller
     * @throws IllegalArgumentException if scrollCount is 0 or negative
     */
    public static MenuScroller setScrollerFor(JPopupMenu menu, int scrollCount) {
        return new MenuScroller(menu, scrollCount);
    }

    /**
     * Registers a menu to be scrolled, with the specified number of items to
     * display at a time and the specified scrolling interval.
     *
     * @param menu the menu
     * @param scrollCount the number of items to be displayed at a time
     * @param interval the scroll interval, in milliseconds
     * @return the MenuScroller
     * @throws IllegalArgumentException if scrollCount or interval is 0 or negative
     */
    public static MenuScroller setScrollerFor(JMenu menu, int scrollCount, int interval) {
        return new MenuScroller(menu, scrollCount, interval);
    }

    /**
     * Registers a popup menu to be scrolled, with the specified number of items to
     * display at a time and the specified scrolling interval.
     *
     * @param menu the popup menu
     * @param scrollCount the number of items to be displayed at a time
     * @param interval the scroll interval, in milliseconds
     * @return the MenuScroller
     * @throws IllegalArgumentException if scrollCount or interval is 0 or negative
     */
    public static MenuScroller setScrollerFor(JPopupMenu menu, int scrollCount, int interval) {
        return new MenuScroller(menu, scrollCount, interval);
    }

    /**
     * Registers a menu to be scrolled, with the specified number of items
     * to display in the scrolling region, the specified scrolling interval,
     * and the specified numbers of items fixed at the top and bottom of the
     * menu.
     *
     * @param menu the menu
     * @param scrollCount the number of items to display in the scrolling portion
     * @param interval the scroll interval, in milliseconds
     * @param topFixedCount the number of items to fix at the top.  May be 0.
     * @param bottomFixedCount the number of items to fix at the bottom. May be 0
     * @throws IllegalArgumentException if scrollCount or interval is 0 or
     * negative or if topFixedCount or bottomFixedCount is negative
     * @return the MenuScroller
     */
    public static MenuScroller setScrollerFor(JMenu menu, int scrollCount, int interval,
                                              int topFixedCount, int bottomFixedCount) {
        return new MenuScroller(menu, scrollCount, interval,
                topFixedCount, bottomFixedCount);
    }

    /**
     * Registers a popup menu to be scrolled, with the specified number of items
     * to display in the scrolling region, the specified scrolling interval,
     * and the specified numbers of items fixed at the top and bottom of the
     * popup menu.
     *
     * @param menu the popup menu
     * @param scrollCount the number of items to display in the scrolling portion
     * @param interval the scroll interval, in milliseconds
     * @param topFixedCount the number of items to fix at the top.  May be 0
     * @param bottomFixedCount the number of items to fix at the bottom.  May be 0
     * @throws IllegalArgumentException if scrollCount or interval is 0 or
     * negative or if topFixedCount or bottomFixedCount is negative
     * @return the MenuScroller
     */
    public static MenuScroller setScrollerFor(JPopupMenu menu, int scrollCount, int interval,
                                              int topFixedCount, int bottomFixedCount) {
        return new MenuScroller(menu, scrollCount, interval,
                topFixedCount, bottomFixedCount);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a menu with the
     * default number of items to display at a time, and default scrolling
     * interval.
     *
     * @param menu the menu
     */
    public MenuScroller(JMenu menu) {
        this(menu, 15);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a popup menu with the
     * default number of items to display at a time, and default scrolling
     * interval.
     *
     * @param menu the popup menu
     */
    public MenuScroller(JPopupMenu menu) {
        this(menu, 15);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a menu with the
     * specified number of items to display at a time, and default scrolling
     * interval.
     *
     * @param menu the menu
     * @param scrollCount the number of items to display at a time
     * @throws IllegalArgumentException if scrollCount is 0 or negative
     */
    public MenuScroller(JMenu menu, int scrollCount) {
        this(menu, scrollCount, 150);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a popup menu with the
     * specified number of items to display at a time, and default scrolling
     * interval.
     *
     * @param menu the popup menu
     * @param scrollCount the number of items to display at a time
     * @throws IllegalArgumentException if scrollCount is 0 or negative
     */
    public MenuScroller(JPopupMenu menu, int scrollCount) {
        this(menu, scrollCount, 150);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a menu with the
     * specified number of items to display at a time, and specified scrolling
     * interval.
     *
     * @param menu the menu
     * @param scrollCount the number of items to display at a time
     * @param interval the scroll interval, in milliseconds
     * @throws IllegalArgumentException if scrollCount or interval is 0 or negative
     */
    public MenuScroller(JMenu menu, int scrollCount, int interval) {
        this(menu, scrollCount, interval, 0, 0);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a popup menu with the
     * specified number of items to display at a time, and specified scrolling
     * interval.
     *
     * @param menu the popup menu
     * @param scrollCount the number of items to display at a time
     * @param interval the scroll interval, in milliseconds
     * @throws IllegalArgumentException if scrollCount or interval is 0 or negative
     */
    public MenuScroller(JPopupMenu menu, int scrollCount, int interval) {
        this(menu, scrollCount, interval, 0, 0);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a menu with the
     * specified number of items to display in the scrolling region, the
     * specified scrolling interval, and the specified numbers of items fixed at
     * the top and bottom of the menu.
     *
     * @param menu the menu
     * @param scrollCount the number of items to display in the scrolling portion
     * @param interval the scroll interval, in milliseconds
     * @param topFixedCount the number of items to fix at the top.  May be 0
     * @param bottomFixedCount the number of items to fix at the bottom.  May be 0
     * @throws IllegalArgumentException if scrollCount or interval is 0 or
     * negative or if topFixedCount or bottomFixedCount is negative
     */
    public MenuScroller(JMenu menu, int scrollCount, int interval,
                        int topFixedCount, int bottomFixedCount) {
        this(menu.getPopupMenu(), scrollCount, interval, topFixedCount, bottomFixedCount);
    }

    /**
     * Constructs a <code>MenuScroller</code> that scrolls a popup menu with the
     * specified number of items to display in the scrolling region, the
     * specified scrolling interval, and the specified numbers of items fixed at
     * the top and bottom of the popup menu.
     *
     * @param menu the popup menu
     * @param scrollCount the number of items to display in the scrolling portion
     * @param interval the scroll interval, in milliseconds
     * @param topFixedCount the number of items to fix at the top.  May be 0
     * @param bottomFixedCount the number of items to fix at the bottom.  May be 0
     * @throws IllegalArgumentException if scrollCount or interval is 0 or
     * negative or if topFixedCount or bottomFixedCount is negative
     */
    public MenuScroller(JPopupMenu menu, int scrollCount, int interval,
                        int topFixedCount, int bottomFixedCount) {
        if (scrollCount <= 0 || interval <= 0) {
            throw new IllegalArgumentException("scrollCount and interval must be greater than 0");
        }
        if (topFixedCount < 0 || bottomFixedCount < 0) {
            throw new IllegalArgumentException("topFixedCount and bottomFixedCount cannot be negative");
        }

        upItem = new MenuScrollItem(MenuIcon.UP, -1);
        downItem = new MenuScrollItem(MenuIcon.DOWN, +1);
        setScrollCount(scrollCount);
        setInterval(interval);
        setTopFixedCount(topFixedCount);
        setBottomFixedCount(bottomFixedCount);

        this.menu = menu;
        menu.addPopupMenuListener(menuListener);

        menu.addMouseWheelListener(new MenuWheelListener());
        this.menu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // Captura o componente que acionou o popup
                Component invoker = menu.getInvoker();
                if (invoker instanceof JComponent) {
                    invoker.addKeyListener(new MenuKeyListener((JComponent)invoker));
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                Component invoker = menu.getInvoker();
                if (invoker instanceof JComponent) {
                    // Remove os listeners quando o menu fecha
                    for (KeyListener kl : invoker.getKeyListeners()) {
                        if (kl instanceof MenuKeyListener) {
                            invoker.removeKeyListener(kl);
                        }
                    }
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                popupMenuWillBecomeInvisible(e);
            }
        });
    }

    /**
     * Scrolls the menu up by one item
     */
    /*public void scrollUp() {
        if (firstIndex > topFixedCount) {
            firstIndex--;
            refreshMenu();
        }
    }

    public void scrollDown() {
        if (firstIndex + scrollCount < menuItems.length - bottomFixedCount) {
            firstIndex++;
            refreshMenu();
        }
    }*/

    /**
     * Scrolls the menu up by the specified number of items
     * @param items number of items to scroll up
     */
    public void scrollUp(int items) {
        int newIndex = Math.max(topFixedCount, firstIndex - items);
        if (newIndex != firstIndex) {
            firstIndex = newIndex;
            refreshMenu();
        }
    }

    /**
     * Scrolls the menu down by the specified number of items
     * @param items number of items to scroll down
     */
    public void scrollDown(int items) {
        int newIndex = Math.min(menuItems.length - bottomFixedCount - scrollCount, firstIndex + items);
        if (newIndex != firstIndex) {
            firstIndex = newIndex;
            refreshMenu();
        }
    }


    /**
     * Returns the scroll interval in milliseconds
     *
     * @return the scroll interval in milliseconds
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Sets the scroll interval in milliseconds
     *
     * @param interval the scroll interval in milliseconds
     * @throws IllegalArgumentException if interval is 0 or negative
     */
    public void setInterval(int interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("interval must be greater than 0");
        }
        upItem.setInterval(interval);
        downItem.setInterval(interval);
        this.interval = interval;
    }

    /**
     * Returns the number of items in the scrolling portion of the menu.
     *
     * @return the number of items to display at a time
     */
    public int getscrollCount() {
        return scrollCount;
    }

    /**
     * Sets the number of items in the scrolling portion of the menu.
     *
     * @param scrollCount the number of items to display at a time
     * @throws IllegalArgumentException if scrollCount is 0 or negative
     */
    public void setScrollCount(int scrollCount) {
        if (scrollCount <= 0) {
            throw new IllegalArgumentException("scrollCount must be greater than 0");
        }
        this.scrollCount = scrollCount;
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    /**
     * Returns the number of items fixed at the top of the menu or popup menu.
     *
     * @return the number of items
     */
    public int getTopFixedCount() {
        return topFixedCount;
    }

    /**
     * Sets the number of items to fix at the top of the menu or popup menu.
     *
     * @param topFixedCount the number of items
     */
    public void setTopFixedCount(int topFixedCount) {
        if (firstIndex <= topFixedCount) {
            firstIndex = topFixedCount;
        } else {
            firstIndex += (topFixedCount - this.topFixedCount);
        }
        this.topFixedCount = topFixedCount;
    }

    /**
     * Returns the number of items fixed at the bottom of the menu or popup menu.
     *
     * @return the number of items
     */
    public int getBottomFixedCount() {
        return bottomFixedCount;
    }

    /**
     * Sets the number of items to fix at the bottom of the menu or popup menu.
     *
     * @param bottomFixedCount the number of items
     */
    public void setBottomFixedCount(int bottomFixedCount) {
        this.bottomFixedCount = bottomFixedCount;
    }

    /**
     * Scrolls the specified item into view each time the menu is opened.  Call this method with
     * <code>null</code> to restore the default behavior, which is to show the menu as it last
     * appeared.
     *
     * @param item the item to keep visible
     * @see #keepVisible(int)
     */
    public void keepVisible(JMenuItem item) {
        if (item == null) {
            keepVisibleIndex = -1;
        } else {
            int index = menu.getComponentIndex(item);
            keepVisibleIndex = index;
        }
    }

    /**
     * Scrolls the item at the specified index into view each time the menu is opened.  Call this
     * method with <code>-1</code> to restore the default behavior, which is to show the menu as
     * it last appeared.
     *
     * @param index the index of the item to keep visible
     * @see #keepVisible(javax.swing.JMenuItem)
     */
    public void keepVisible(int index) {
        keepVisibleIndex = index;
    }

    /**
     * Removes this MenuScroller from the associated menu and restores the
     * default behavior of the menu.
     */
    public void dispose() {
        if (menu != null) {
            menu.removePopupMenuListener(menuListener);
            menu = null;
        }
    }

    private boolean isScrollingNeeded() {
        if (menuItems == null) return false;
        return menuItems.length > topFixedCount + scrollCount + bottomFixedCount;
    }

    /**
     * Ensures that the <code>dispose</code> method of this MenuScroller is
     * called when there are no more refrences to it.
     *
     * @exception  Throwable if an error occurs.
     * @see MenuScroller#dispose()
     */
    @Override
    public void finalize() throws Throwable {
        dispose();
    }

    private void refreshMenu() {
        if (menuItems != null && menuItems.length > 0) {
            firstIndex = Math.max(topFixedCount, firstIndex);
            firstIndex = Math.min(menuItems.length - bottomFixedCount - scrollCount, firstIndex);

            boolean scrollingNeeded = isScrollingNeeded();
            upItem.setVisible(scrollingNeeded && firstIndex > topFixedCount);
            downItem.setVisible(scrollingNeeded && firstIndex + scrollCount < menuItems.length - bottomFixedCount);

            menu.removeAll();
            for (int i = 0; i < topFixedCount; i++) {
                menu.add(menuItems[i]);
            }
            if (topFixedCount > 0) {
                menu.addSeparator();
            }

            if (upItem.isVisible()) {
                menu.add(upItem);
            }

            int endIndex = scrollingNeeded ? scrollCount + firstIndex : menuItems.length - bottomFixedCount;
            for (int i = firstIndex; i < endIndex; i++) {
                menu.add(menuItems[i]);
            }

            if (downItem.isVisible()) {
                menu.add(downItem);
            }

            if (bottomFixedCount > 0) {
                menu.addSeparator();
                for (int i = menuItems.length - bottomFixedCount; i < menuItems.length; i++) {
                    menu.add(menuItems[i]);
                }
            }

            menu.revalidate();
            menu.repaint();
        }
    }

    /*private void refreshMenu() {
        if (menuItems != null && menuItems.length > 0) {
            firstIndex = Math.max(topFixedCount, firstIndex);
            firstIndex = Math.min(menuItems.length - bottomFixedCount - scrollCount, firstIndex);

            upItem.setEnabled(firstIndex > topFixedCount);
            downItem.setEnabled(firstIndex + scrollCount < menuItems.length - bottomFixedCount);

            menu.removeAll();
            for (int i = 0; i < topFixedCount; i++) {
                menu.add(menuItems[i]);
            }
            if (topFixedCount > 0) {
                menu.addSeparator();
            }

            // Adiciona seta para cima se necessário
            if (upItem.isVisible()) {
                menu.add(upItem);
            }

            // Itens scrolláveis
            for (int i = firstIndex; i < scrollCount + firstIndex; i++) {
                menu.add(menuItems[i]);
            }

            // Adiciona seta para baixo se necessário
            if (downItem.isVisible()) {
                menu.add(downItem);
            }

            // Itens fixos na base
            if (bottomFixedCount > 0) {
                menu.addSeparator();
                for (int i = menuItems.length - bottomFixedCount; i < menuItems.length; i++) {
                    menu.add(menuItems[i]);
                }
            }
        }
    }
     */
    //       menu.revalidate();
    //        menu.repaint();

            /*menu.add(upItem);
            for (int i = firstIndex; i < scrollCount + firstIndex; i++) {
                menu.add(menuItems[i]);
            }
            menu.add(downItem);

            if (bottomFixedCount > 0) {
                menu.addSeparator();
            }
            for (int i = menuItems.length - bottomFixedCount; i < menuItems.length; i++) {
                menu.add(menuItems[i]);
            }

            //JComponent parent = (JComponent) upItem.getParent();
            //parent.revalidate();
            //parent.repaint();

            if (menu.getComponentCount() > 0) {
                menu.revalidate();
                menu.repaint();
            }*/


    private class MenuScrollListener implements PopupMenuListener {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            setMenuItems();
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            restoreMenuItems();
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            restoreMenuItems();
        }

        private void setMenuItems() {
            menuItems = menu.getComponents();
            if (keepVisibleIndex >= topFixedCount
                    && keepVisibleIndex <= menuItems.length - bottomFixedCount
                    && (keepVisibleIndex > firstIndex + scrollCount
                    || keepVisibleIndex < firstIndex)) {
                firstIndex = Math.min(firstIndex, keepVisibleIndex);
                firstIndex = Math.max(firstIndex, keepVisibleIndex - scrollCount + 1);
            }
            if (isScrollingNeeded()) {
                refreshMenu();
            } else {
                firstIndex = topFixedCount; // Reset to show all items if no scrolling needed
                menu.removeAll();
                for (Component component : menuItems) {
                    menu.add(component);
                }
                menu.revalidate();
                menu.repaint();
            }
        }

        private void restoreMenuItems() {
            menu.removeAll();
            for (Component component : menuItems) {
                menu.add(component);
            }
        }
    }

    private class MenuScrollTimer extends Timer {

        public MenuScrollTimer(final int increment, int interval) {
            super(interval, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    firstIndex += increment;
                    refreshMenu();
                }
            });
        }
    }

    private class MenuScrollItem extends JMenuItem
            implements ChangeListener {

        private MenuScrollTimer timer;

        public MenuScrollItem(MenuIcon icon, int increment) {
            setIcon(icon);
            setDisabledIcon(icon);
            timer = new MenuScrollTimer(increment, interval);
            addChangeListener(this);
        }

        public void setInterval(int interval) {
            timer.setDelay(interval);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (isArmed() && !timer.isRunning()) {
                timer.start();
            }
            if (!isArmed() && timer.isRunning()) {
                timer.stop();
            }
        }
    }

    private static enum MenuIcon implements Icon {

        UP(9, 1, 9),
        DOWN(1, 9, 1);
        final int[] xPoints = {1, 5, 9};
        final int[] yPoints;

        MenuIcon(int... yPoints) {
            this.yPoints = yPoints;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Dimension size = c.getSize();
            Graphics g2 = g.create(size.width / 2 - 5, size.height / 2 - 5, 10, 10);
            g2.setColor(Color.GRAY);
            g2.drawPolygon(xPoints, yPoints, 3);
            if (c.isEnabled()) {
                g2.setColor(Color.BLACK);
                g2.fillPolygon(xPoints, yPoints, 3);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 0;
        }

        @Override
        public int getIconHeight() {
            return 10;
        }
    }

    private class MenuWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int rotation = e.getWheelRotation();
            if (rotation > 0) {
                scrollDown(Math.abs(rotation));
            } else if (rotation < 0) {
                scrollUp(Math.abs(rotation));
            }
        }
    }

    private class MenuKeyListener extends KeyAdapter {
        private final JComponent triggerComponent;

        public MenuKeyListener(JComponent triggerComponent) {
            this.triggerComponent = triggerComponent;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!menu.isVisible()) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    navigateMenu(-1);
                    e.consume();
                    break;

                case KeyEvent.VK_DOWN:
                    navigateMenu(1);
                    e.consume();
                    break;

                case KeyEvent.VK_ENTER:
                    selectCurrentItem();
                    e.consume();
                    break;

                case KeyEvent.VK_ESCAPE:
                    menu.setVisible(false);
                    triggerComponent.requestFocus();
                    e.consume();
                    break;
            }
        }

        private void navigateMenu(int direction) {
            MenuElement[] currentSelection = MenuSelectionManager.defaultManager().getSelectedPath();
            JMenuItem toSelect = null;

            // Se não há seleção atual, seleciona o primeiro item
            if (currentSelection.length == 0) {
                toSelect = findFirstSelectableItem();
            } else {
                // Encontra o item atualmente selecionado
                JMenuItem current = findCurrentlySelectedMenuItem(currentSelection);
                if (current != null) {
                    toSelect = findNextSelectableItem(current, direction);
                }
            }

            if (toSelect != null) {
                MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{menu, toSelect});
                ensureVisible(toSelect);
            }
        }

        private JMenuItem findFirstSelectableItem() {
            for (Component comp : menu.getComponents()) {
                if (comp instanceof JMenuItem && comp != upItem && comp != downItem && comp.isEnabled()) {
                    return (JMenuItem) comp;
                }
            }
            return null;
        }

        private JMenuItem findCurrentlySelectedMenuItem(MenuElement[] path) {
            for (int i = path.length - 1; i >= 0; i--) {
                if (path[i] instanceof JMenuItem) {
                    return (JMenuItem) path[i];
                }
            }
            return null;
        }

        private JMenuItem findNextSelectableItem(JMenuItem current, int direction) {
            Component[] components = menu.getComponents();
            int currentIndex = -1;

            // Encontra o índice do item atual
            for (int i = 0; i < components.length; i++) {
                if (components[i] == current) {
                    currentIndex = i;
                    break;
                }
            }

            if (currentIndex == -1) return null;

            // Procura pelo próximo item selecionável
            for (int i = currentIndex + direction; i >= 0 && i < components.length; i += direction) {
                Component comp = components[i];
                if (comp instanceof JMenuItem && comp != upItem && comp != downItem && comp.isEnabled()) {
                    return (JMenuItem) comp;
                }
            }

            return null;
        }

        private void ensureVisible(JMenuItem item) {
            int index = -1;
            Component[] components = menu.getComponents();

            // Encontra o índice do item
            for (int i = 0; i < components.length; i++) {
                if (components[i] == item) {
                    index = i - topFixedCount; // Ajusta para o índice no scroll
                    break;
                }
            }

            if (index != -1) {
                if (index < firstIndex) {
                    scrollUp(firstIndex - index);
                } else if (index >= firstIndex + scrollCount) {
                    scrollDown(index - (firstIndex + scrollCount) + 1);
                }
            }
        }

        private void selectCurrentItem() {
            MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();
            if (path.length > 1 && path[path.length-1] instanceof JMenuItem) {
                JMenuItem selected = (JMenuItem) path[path.length-1];
                if (selected != upItem && selected != downItem) {
                    if (triggerComponent instanceof JTextField) {
                        ((JTextField)triggerComponent).setText(selected.getText());
                    }
                    menu.setVisible(false);
                    triggerComponent.requestFocus();
                }
            }
        }
    }
}