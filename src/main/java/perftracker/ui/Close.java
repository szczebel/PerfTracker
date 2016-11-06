package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.DirtyTracker;
import perftracker.persistence.Persister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static javax.swing.JOptionPane.*;

@Component
public class Close extends WindowAdapter {

    @Autowired
    Persister persister;
    @Autowired
    DirtyTracker dirtyTracker;

    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        if (!dirtyTracker.isDirty()) {
            window.dispose();
        } else {

            int retval = showConfirmDialog(window, "Save changes?", "Confirm close", YES_NO_CANCEL_OPTION);
            if (retval == NO_OPTION) window.dispose();
            else if (retval == YES_OPTION) {
                try {
                    persister.save();
                    window.dispose();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(window, "Save failed");
                }
            }
        }
    }
}
