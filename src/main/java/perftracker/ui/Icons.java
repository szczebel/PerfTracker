package perftracker.ui;

import javax.swing.*;

public enum Icons {
    PLUS("/plus.png"),
    MINUS("/minus.png"),
    DELETE("/delete.png");

    public final Icon icon;

    Icons(String icon) {
        this.icon = new ImageIcon(Icons.class.getResource(icon));
    }
}
