package perftracker;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import swingutils.spring.application.SwingApplication;
import swingutils.spring.application.SwingApplicationBootstrap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
@SwingApplication
public class Main {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException {
        SwingApplicationBootstrap.beforeSpring("/icon.png");
        new SpringApplicationBuilder(Main.class).headless(false).run(args);

    }
}
