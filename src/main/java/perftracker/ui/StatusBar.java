package perftracker.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import static swingutils.components.ComponentFactory.decorate;
import static swingutils.components.ComponentFactory.label;

@Component
public class StatusBar implements Consumer<String> {

    private JLabel statusBar = label("Ready");
    JComponent getComponent() {return decorate(statusBar).withEmptyBorder(0, 4, 0, 4).get();}

    @Override
    public void accept(String s) {
        statusBar.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + s);
    }
}
