package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swingutils.components.console.RollingConsole;
import swingutils.frame.RichFrame;

import javax.swing.*;

import static swingutils.components.ComponentFactory.decorate;

@Component
public class LogsPresenter {

    @Autowired
    RollingConsole logs;

    void showLogs(RichFrame parent) {
        parent.getOverlay().showAndLock(createDialog(parent.getOverlay()::hideAndUnlock));
    }

    private JComponent createDialog(Runnable closeAction) {
        return decorate(logs.getComponent())
                .withGradientHeader("Logs", closeAction, null)
                .get();
    }
}
