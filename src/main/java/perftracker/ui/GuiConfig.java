package perftracker.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.impl.Factory;
import perftracker.persistence.Persister;
import swingutils.SysoutInterceptor;
import swingutils.components.console.RollingConsole;
import swingutils.frame.RichFrame;
import swingutils.spring.application.SwingEntryPoint;

import java.awt.*;
import java.util.function.Consumer;

@Configuration
public class GuiConfig {
    static final Color FACTORIAL_COLOR = new Color(128, 255, 128, 128);

    @Bean
    RollingConsole logs(SysoutInterceptor sysoutInterceptor) {
        RollingConsole rollingConsole = new RollingConsole(256);
        sysoutInterceptor.registerSwingConsumer(rollingConsole::append);
        return rollingConsole;
    }

    //todo: when this method is moved below wireThingsUp, NPE is thrown
    @Bean
    RichFrame mainFrame(GuiBuilder guiBuilder) {
        return guiBuilder.build();
    }

    @Bean
    Object wireThingsUp(Persister persister,
                        Binder binder,
                        Factory domainFactory,
                        Consumer<String> statusBar) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> statusBar.accept(e.getMessage()));
        PerformanceTrackingSystem initial = domainFactory.createEmptySystem();
        persister.setCurrent(initial);
        binder.bindAllTo(initial);
        return null;
    }

    @Bean
    SwingEntryPoint showAndLoadRecent(RichFrame mainFrame, Persister persister) {
        return () -> {
            mainFrame.setVisible(true);
            persister.loadRecentProject();
        };
    }
}
