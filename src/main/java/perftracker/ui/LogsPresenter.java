package perftracker.ui;

import org.springframework.stereotype.Component;
import swingutils.SysoutInterceptor;
import swingutils.components.console.RollingConsole;
import swingutils.frame.RichFrame;

import javax.swing.*;

import static swingutils.components.ComponentFactory.decorate;

public class LogsPresenter {

    private RollingConsole logs = new RollingConsole(256);

    LogsPresenter(SysoutInterceptor sysoutInterceptor) {
        sysoutInterceptor.registerSwingConsumer(logs::append);
    }

    void showLogs(RichFrame parent) {
        parent.getOverlay().showAndLock(createDialog(parent.getOverlay()::hideAndUnlock));
    }

    private JComponent createDialog(Runnable closeAction) {
        return decorate(logs.getComponent())
                .withGradientHeader("Logs", closeAction, null)
                .get();
    }
}
