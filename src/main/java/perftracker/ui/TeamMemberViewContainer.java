package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.layout.cards.FadingCardPanel;

import javax.swing.*;

import static swingutils.components.ComponentFactory.withGradientHeader;

@Component
class TeamMemberViewContainer {

    @Autowired
    private TeamMemberViewBuilder teamMemberViewBuilder;

    private PerformanceTrackingSystem system;
    private FadingCardPanel panel = new FadingCardPanel();

    public JComponent getComponent() {
        return panel.getComponent();
    }

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        panel.removeAll();
        panel.addCard("empty placeholder", withGradientHeader(new JPanel(),"Details of selected team member"));
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
