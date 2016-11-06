package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.layout.cards.FadingCardPanel;

import javax.swing.*;

import static swingutils.components.ComponentFactory.withGradientHeader;

@Component
class TeamMemberViewContainer3 {

    @Autowired
    private TeamMemberViewBuilder teamMemberViewBuilder;

    private PerformanceTrackingSystem system;
    private FadingCardPanel panel = new FadingCardPanel();

    public JComponent getComponent() {
        return panel.getComponent();
    }

    void resetWith(PerformanceTrackingSystem system) {
        this.system = system;
        panel.removeAll();
        panel.addCard("something has to be here, so that first fade-in works ok", new JPanel());

    }

    void showDetails(TeamMember selection) {
        if (!panel.cardExists(selection.getName())) {
            panel.addCard(selection.getName(),
                    withGradientHeader(
                            teamMemberViewBuilder.build(selection, system),
                            "Details of " + selection.getName())
            );
        }
        panel.showCard(selection.getName());
    }
}
