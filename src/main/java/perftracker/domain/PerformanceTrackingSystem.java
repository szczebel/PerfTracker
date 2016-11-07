package perftracker.domain;

import java.util.List;
import java.util.function.Consumer;

public interface PerformanceTrackingSystem {

    List<Criteria> getCriteria();

    List<TeamMember> getTeam();

    void addCriteria(String name, CriteriaType type, int maxGrade);

    void addTeamMember(String newTeamMember);

    int getMaxGrade(CriteriaType type);

    void updateGrade(String teamMember, Criteria criteria, int newValue);

    void whenCriteriaAdded(Consumer<Criteria> listener);

    void whenTeamMemberAdded(Consumer<TeamMember> listener);
}
