package perftracker.domain;

import java.util.List;
import java.util.function.Consumer;

public interface PerformanceTrackingSystem {

    List<Criteria> getCriteria();

    List<TeamMember> getTeam();

    void addCriteria(String name, CriteriaType type, int maxGrade);

    void addTeamMember(String newTeamMember);

    void whenTeamMemberDeleted(Consumer<TeamMember> listener);

    int getMaxGrade(CriteriaType type);

    void updateScore(String teamMember, Criteria criteria, int newValue);

    void whenCriteriaAdded(Consumer<Criteria> listener);
    void whenCriteriaDeleted(Consumer<Criteria> listener);

    void whenTeamMemberAdded(Consumer<TeamMember> listener);

    void deleteCriteria(Criteria criteria);

    void deleteTeamMember(TeamMember teamMember);
}
