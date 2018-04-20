package perftracker;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.impl.Factory;
import perftracker.persistence.Persister;
import perftracker.ui.Binder;
import perftracker.ui.GUI;
import swingutils.spring.application.SwingApplication;
import swingutils.spring.application.SwingApplicationBootstrap;
import swingutils.spring.application.SwingEntryPoint;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

@SpringBootApplication
@SwingApplication
public class Main {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException {
        SwingApplicationBootstrap.beforeSpring("/icon.png");
        new SpringApplicationBuilder(Main.class).headless(false).run(args);

    }

    @Bean
    SwingEntryPoint startInEdt(Persister persister,
                               Binder binder,
                               GUI gui,
                               Factory domainFactory,
                               Consumer<String> statusBar) {
        return () -> {
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> statusBar.accept(e.getMessage()));
            gui.show();
            PerformanceTrackingSystem initial = domainFactory.createEmptySystem();
            persister.setCurrent(initial);
            binder.bindAllTo(initial);
            persister.loadRecentProject();

        };
    }

}
