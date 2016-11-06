package perftracker.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.DirtyTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.function.Consumer;

@Component
public class AutoSaver {

    @Autowired Persister persister;
    @Autowired DirtyTracker dirtyTracker;
    @Autowired Consumer<String> statusBar;

    private Timer timer = new Timer(10000, this::tryAutosave);
    {timer.start();}

    private void tryAutosave(ActionEvent event) {
        if(dirtyTracker.isDirty() && persister.hasFile()) {
            try {
                persister.save();
            } catch (IOException e) {
                statusBar.accept("Autosave failed: " + e.getMessage());
            }
        }
    }
}
