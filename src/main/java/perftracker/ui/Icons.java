package perftracker.ui;

import swingutils.components.IconUtils;

import javax.swing.*;

public enum Icons {
    PLUS("/plus.png", 24, 24),
    MINUS("/minus.png", 24, 24),
    DELETE("/delete.png", 24, 24);

    public final Icon icon;

    Icons(String icon) {
        this.icon = new ImageIcon(Icons.class.getResource(icon));
    }

    Icons(String icon, int w, int h) {
        this.icon = IconUtils.resizeTo(
                new ImageIcon(Icons.class.getResource(icon)).getImage(),
                w, h);
    }
}
