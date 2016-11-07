package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.TeamMember;

import java.util.function.Consumer;


@Component
public class TeamMemberSelection {

    @Autowired private TeamView teamView;

    TeamMember get() {
        return teamView.getSelectedTeamMember();
    }

    void whenSelectionChanged(Consumer<TeamMember> listener) {
        teamView.whenSelectionChanged(listener);
    }
}
