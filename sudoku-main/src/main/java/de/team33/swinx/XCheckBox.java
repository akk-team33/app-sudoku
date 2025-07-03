//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.swinx;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class XCheckBox extends JCheckBox implements ActionListener {
    public XCheckBox(String text) {
        super(text);
        this.addActionListener(this);
    }
}
