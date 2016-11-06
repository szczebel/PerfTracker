package perftracker;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.impl.Factory;
import perftracker.persistence.Persister;
import perftracker.ui.Binder;
import perftracker.ui.GUI;
import swingutils.components.ComponentFactory;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

@ComponentScan(basePackages = "perftracker")
@Component
public class Main {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        ComponentFactory.initLAF();
        SwingUtilities.invokeAndWait(() -> new AnnotationConfigApplicationContext(Main.class));
    }

    @Autowired
    private Persister persister;
    @Autowired
    private Binder binder;
    @Autowired
    private GUI gui;
    @Autowired
    private Factory domainFactory;
    @Autowired
    private Consumer<String> statusBar;

    @SuppressWarnings("unused")
    @PostConstruct
    void startup() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> statusBar.accept(e.getMessage()));
        gui.show();
        PerformanceTrackingSystem initial = domainFactory.createEmptySystem();
        persister.setCurrent(initial);
        binder.bindAllTo(initial);
        persister.loadRecentProject();
    }
}
