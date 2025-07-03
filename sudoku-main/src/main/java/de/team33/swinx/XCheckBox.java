package de.team33.swinx;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class XCheckBox extends JCheckBox implements ActionListener {
    public XCheckBox(final String text) {
        super(text);
        addActionListener(this);
    }
}
