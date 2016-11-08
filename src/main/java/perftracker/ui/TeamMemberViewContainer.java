package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.layout.cards.CardPanel;

import javax.annotation.PostConstruct;
import javax.swing.*;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

import static swingutils.components.ComponentFactory.withGradientHeader;

@Component
class TeamMemberViewContainer {

    private static final String EMPTY_PLACEHOLDER = "$$$empty placeholder$$$";
    @Autowired
    private TeamMemberViewBuilder teamMemberViewBuilder;
    @Autowired
    private TeamMemberSelection teamMemberSelection;

    private PerformanceTrackingSystem system;
    private CardPanel panel = new CardPanel();
//    private CardPanel panel = new FadingCardPanel();//todo long-running change of cards is interfering with immediate card removal

    public JComponent getComponent() {
        return panel.getComponent();
    }

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        panel.removeAll();
        panel.addCard(EMPTY_PLACEHOLDER, withGradientHeader(teamMemberViewBuilder.build(NOONE, system),"Details of selected team member"));
        system.whenTeamMemberDeleted(this::teamMemberRemoved);
    }

    @SuppressWarnings("unused")
    @PostConstruct
    void init(){
        teamMemberSelection.whenSelectionChanged(this::showDetails);
    }

    private void teamMemberRemoved(TeamMember tm) {
        panel.removeCard(tm.getName());
    }

    private void showDetails(TeamMember selection) {
        if(selection == null) {
            panel.showCard(EMPTY_PLACEHOLDER);
        } else {
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

    private static final TeamMember NOONE = new TeamMember() {
        @Override
        public String getName() {
            return "noone";
        }

        @Override
        public Map<Criteria, Integer> getScores() {
            return Collections.emptyMap();
        }

        @Override
        public int getTotalScore(CriteriaType type) {
            return 0;
        }

        @Override
        public void whenScoreChanged(BiConsumer<Criteria, Integer> listener) {

        }
    };
}
