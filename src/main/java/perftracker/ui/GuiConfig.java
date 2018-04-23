package perftracker.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.impl.Factory;
import perftracker.persistence.Persister;
import swingutils.SysoutInterceptor;
import swingutils.spring.application.BeforeEdtInitializer;
import swingutils.spring.application.SwingEntryPoint;

import java.awt.*;
import java.util.function.Consumer;

@Configuration
public class GuiConfig {
    static final Color FACTORIAL_COLOR = new Color(128, 255, 128, 128);

    @Bean
    LogsPresenter logsPresenter(SysoutInterceptor sysoutInterceptor) {
        return new LogsPresenter(sysoutInterceptor);
    }

    @Bean
    BeforeEdtInitializer nonEdtInitializer(
            Persister persister,
            Binder binder,
            Factory domainFactory,
            Consumer<String> statusBar,
            MainFrame mainFrame
    ) {
        return () -> initBeforeEdt(persister, binder, domainFactory, statusBar, mainFrame);
    }

    @Bean
    SwingEntryPoint edtInitializer(Persister persister, MainFrame mainFrame) {
        return () -> initInEdt(persister, mainFrame);
    }

    private void initBeforeEdt(Persister persister, Binder binder, Factory domainFactory, Consumer<String> statusBar, MainFrame mainFrame) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> statusBar.accept(e.getMessage()));
        mainFrame.build();

        PerformanceTrackingSystem initial = domainFactory.createEmptySystem();
        persister.setCurrent(initial);
        binder.bindAllTo(initial);
    }

    private void initInEdt(Persister persister, MainFrame mainFrame) {
        mainFrame.setVisible(true);
        persister.loadRecentProject();//todo: I/O in background
    }
}
