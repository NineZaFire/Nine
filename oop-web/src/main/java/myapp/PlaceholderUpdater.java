package myapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Supplier;

public class PlaceholderUpdater {
    private final JTextField target;
    private final Supplier<String> placeholderSupplier;
    private Color placeholderColor = Color.GRAY;
    private Color normalColor = Color.BLACK;

    public PlaceholderUpdater(JTextField target, Supplier<String> placeholderSupplier) {
        this.target = target;
        this.placeholderSupplier = placeholderSupplier;
        install();
    }

    private void install() {
        target.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (target.getText().isEmpty()) {
                    target.setForeground(normalColor);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (target.getText().isEmpty()) {
                    target.setForeground(placeholderColor);
                    target.setText(placeholderSupplier.get());
                }
            }
        });
        // initialize placeholder
        target.setText(placeholderSupplier.get());
        target.setForeground(placeholderColor);
        // Remove placeholder on first key typed
        target.addKeyListener(new java.awt.event.KeyAdapter(){
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (target.getForeground().equals(placeholderColor)) {
                    target.setText("");
                    target.setForeground(normalColor);
                }
            }
        });
    }

    public void updatePlaceholder() {
        if (target.getText().isEmpty() || target.getForeground().equals(placeholderColor)) {
            target.setText(placeholderSupplier.get());
            target.setForeground(placeholderColor);
        }
    }
}
