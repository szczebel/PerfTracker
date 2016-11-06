package perftracker.domain.impl;

import perftracker.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


class PTSImpl implements PerformanceTrackingSystem {


    private List<Criteria> criteria = new ArrayList<>();
    private List<TeamMemberImpl> team = new ArrayList<>();
    private List<Consumer<Criteria>> addCriteriaListeners = new ArrayList<>();
    private List<Consumer<TeamMember>> addTeamMemberListeners = new ArrayList<>();

    @Override
    public List<Criteria> getCriteria() {
        return Collections.unmodifiableList(criteria);
    }

    @Override
    public Criteria findCriteria(String criteriaName) {
        return findOptionalCriteria(criteriaName).orElseThrow(() -> new IllegalArgumentException("Unknown criteria: " + criteriaName));
    }

    private Optional<Criteria> findOptionalCriteria(String criteriaName) {
        return getCriteria().stream().filter(c -> criteriaName.equals(c.getName())).findFirst();
    }

    @Override
    public List<TeamMember> getTeam() {
        return Collections.unmodifiableList(team);
    }

    @Override
    public void addCriteria(Criteria newCriteria) {
        if(findOptionalCriteria(newCriteria.getName()).isPresent()) throw new IllegalArgumentException(newCriteria.getName() + " already exists");
        criteria.add(newCriteria);
        addCriteriaListeners.forEach(criteriaConsumer -> criteriaConsumer.accept(newCriteria));
        team.forEach(tm -> setGradeOn(tm, newCriteria));
    }

    @Override
    public void addTeamMember(String newTeamMember) {
        if(team.stream().filter(tm -> newTeamMember.equals(tm.getName())).findFirst().isPresent()) throw new IllegalArgumentException(newTeamMember + " already exists");
        TeamMemberImpl tm = new TeamMemberImpl(newTeamMember);
        team.add(tm);
        criteria.forEach(c -> setGradeOn(tm, c));
        addTeamMemberListeners.forEach(listener -> listener.accept(tm));
    }

    @Override
    public void updateGrade(String teamMemberName, String criteria, int newValue) {
        if(newValue<0 || newValue>findCriteria(criteria).getMaxGrade()) throw new IllegalArgumentException("Grade out of range");
        TeamMemberImpl teamMember =
                team.stream().filter(tm -> teamMemberName.equals(tm.getName())).findFirst().orElseThrow(IllegalArgumentException::new);
        setGradeOn(teamMember, criteria, newValue);
    }

    private void setGradeOn(TeamMemberImpl tm, Criteria newCriteria) {
        setGradeOn(tm, newCriteria.getName(), newCriteria.getMaxGrade()/2);
    }
    private void setGradeOn(TeamMemberImpl tm, String criteria, int value) {
        tm.setGrade(criteria, value);
    }

    @Override
    public void whenCriteriaAdded(Consumer<Criteria> listener) {
        addCriteriaListeners.add(listener);
    }

    @Override
    public void whenTeamMemberAdded(Consumer<TeamMember> listener) {
        addTeamMemberListeners.add(listener);
    }
}
