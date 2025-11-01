package myapp;

import javax.swing.*;

/**
 * Abstract Tool base class for concrete tools.
 */
public abstract class Tool {
    private final String id;
    private final String name;
    protected final JPanel panel;

    public Tool(String id, String name) {
        this.id = id;
        this.name = name;
        this.panel = new JPanel();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public JPanel getPanel() { return panel; }

    public void setActive(boolean active) {
        panel.setVisible(active);
        setEnabledRecursive(panel, active);
    }

    private void setEnabledRecursive(java.awt.Component comp, boolean enabled) {
        comp.setEnabled(enabled);
        if (comp instanceof java.awt.Container container) {
            for (java.awt.Component c : container.getComponents()) setEnabledRecursive(c, enabled);
        }
    }

    public abstract void buildUI();
    public void onShow() {}
    public void onHide() {}
}
