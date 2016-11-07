package perftracker.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.DirtyTracker;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.impl.Factory;
import perftracker.domain.impl.Serializer;
import perftracker.ui.Binder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

@Component
public class Persister {

    private static final String RECENT_PROPERTIES_FILENAME = "recent.properties";
    private static final String RECENT_FILE_KEY = "recentFile";

    @Autowired
    private Factory domainFactory;
    @Autowired
    private Serializer serializer;
    @Autowired
    private Binder binder;
    @Autowired
    private Consumer<String> statusBar;
    @Autowired
    private DirtyTracker dirtyTracker;

    private File currentProjectFile;
    private PerformanceTrackingSystem current;

    public boolean hasFile() {
        return currentProjectFile!=null;
    }

    public void saveAs(File file) throws IOException {
        saveTo(file);
        saveRecentProjectFile(file);
    }

    public void save() throws IOException {
        saveTo(currentProjectFile);
    }

    private void saveTo(File projectFile) throws IOException {
        serializer.toFile(projectFile, current);
        dirtyTracker.setDirty(false);
        statusBar.accept("Project file saved successfully");
    }

    public void loadFrom(File file) throws IOException {
        PerformanceTrackingSystem system = serializer.fromFile(file);
        this.current = system;
        binder.bindAllTo(system);
        saveRecentProjectFile(file);
        statusBar.accept("Project file loaded successfully");
    }

    private void saveRecentProjectFile(File file) {
        currentProjectFile = file;
        listener.accept(file);
        try {
            Properties properties = new Properties();
            properties.setProperty(RECENT_FILE_KEY, file.getAbsolutePath());
            properties.store(new FileWriter(RECENT_PROPERTIES_FILENAME), "");
        } catch (IOException e) {
            statusBar.accept("Storing recent project file failed: " + e.getMessage());
        }
    }

    public void loadRecentProject() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(RECENT_PROPERTIES_FILENAME));
            File candidate = new File(properties.getProperty(RECENT_FILE_KEY));
            loadFrom(candidate);
        } catch (IOException e) {
            statusBar.accept("Loading recent project file failed: " + e.getMessage());
        }
    }

    public void setCurrent(PerformanceTrackingSystem current) {
        this.current = current;
    }

    private Consumer<File> listener = f -> {};
    public void onCurrentFileChange(Consumer<File> listener) {
        this.listener = listener;
    }
}
