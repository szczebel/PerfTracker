package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.DirtyTracker;
import perftracker.persistence.Persister;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static swingutils.components.ComponentFactory.button;
import static swingutils.components.ComponentFactory.label;
import static swingutils.layout.LayoutBuilders.borderLayout;
import static swingutils.layout.LayoutBuilders.flowLayout;

@Component
public class FileViewBuilder {

    @Autowired
    private Persister persister;
    @Autowired
    private Consumer<String> statusBar;
    @Autowired
    private DirtyTracker dirtyTracker;

    private JFileChooser fileChooser = new JFileChooser(".");
    private JLabel filenamebox = label("-");
    private JLabel unsaved = label("");

    JComponent build() {
        filenamebox.setFont(filenamebox.getFont().deriveFont(Font.BOLD));
        persister.onCurrentFileChange(f -> filenamebox.setText(f.getName()));
        dirtyTracker.whenDirtyChanged(dirty -> unsaved.setText(dirty ? "(unsaved changes)" : ""));

        return borderLayout()
                .east(
                        flowLayout(
                                label("Current file: "),
                                filenamebox,
                                unsaved,
                                button("Open...", this::open),
                                button("Save as...", this::saveAs),
                                button("Save", this::save)
                                )
                ).build();
    }

    private void save() {
        if (persister.hasFile()) {
            try {
                persister.save();
            } catch (IOException e) {
                e.printStackTrace();
                statusBar.accept("Save failed, check logs");
            }
        } else {
            saveAs();
        }
    }

    private void open() {
        if (APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                persister.loadFrom(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
                statusBar.accept("Load failed, check logs");
            }
        }
    }

    private void saveAs() {
        if (APPROVE_OPTION == fileChooser.showSaveDialog(null)) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                persister.saveAs(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
                statusBar.accept("Save failed, check logs");
            }
        }
    }

}
