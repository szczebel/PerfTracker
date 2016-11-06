package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.components.ComponentFactory;

import javax.swing.*;
import java.awt.*;

import static swingutils.components.ComponentFactory.withGradientHeader;

@Component
class TeamMemberViewContainer2 {

    @Autowired
    private TeamMemberViewBuilder teamMemberViewBuilder;

    private PerformanceTrackingSystem system;
    private JPanel panel = new JPanel(new BorderLayout());

    public JComponent getComponent() {
        return panel;
    }

    void resetWith(PerformanceTrackingSystem system) {
        this.system = system;
        panel.removeAll();
        panel.revalidate();
    }

    void showDetails(TeamMember selection) {
        panel.removeAll();
        panel.add(withGradientHeader(
                teamMemberViewBuilder.build(selection, system),
                "Details of " + selection.getName())
        );
        panel.revalidate();
    }
}
