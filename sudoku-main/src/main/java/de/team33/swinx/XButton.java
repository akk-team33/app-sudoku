package de.team33.swinx;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class XButton extends JButton implements ActionListener {
    public XButton(final Icon ico) {
        super(ico);
        addActionListener(this);
    }

    public XButton(final String text) {
        super(text);
        addActionListener(this);
    }
}
