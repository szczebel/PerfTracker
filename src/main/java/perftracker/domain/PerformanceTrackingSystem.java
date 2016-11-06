package perftracker.domain;

import java.util.List;
import java.util.function.Consumer;

public interface PerformanceTrackingSystem {

    List<Criteria> getCriteria();

    List<TeamMember> getTeam();

    void addCriteria(Criteria newCriteria);

    void addTeamMember(String newTeamMember);

    void updateGrade(String teamMember, String criteria, int newValue);

    void whenCriteriaAdded(Consumer<Criteria> listener);

    void whenTeamMemberAdded(Consumer<TeamMember> listener);
}
