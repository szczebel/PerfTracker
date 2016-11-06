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
    public List<TeamMember> getTeam() {
        return Collections.unmodifiableList(team);
    }

    @Override
    public void addCriteria(Criteria newCriteria) {
        criteria.add(newCriteria);//todo check duplicates
        addCriteriaListeners.forEach(criteriaConsumer -> criteriaConsumer.accept(newCriteria));
        team.forEach(tm -> setGradeOn(tm, newCriteria));

    }

    @Override
    public void addTeamMember(String newTeamMember) {
        TeamMemberImpl tm = new TeamMemberImpl(newTeamMember);
        team.add(tm);//todo check duplicates
        criteria.forEach(c -> setGradeOn(tm, c));
        addTeamMemberListeners.forEach(listener -> listener.accept(tm));
    }

    @Override
    public void updateGrade(String name, String criteria, int newValue) {
        Optional<TeamMemberImpl> teamMember = team.stream().filter(tm -> name.equals(tm.getName())).findFirst();
        //todo validate newValue in range
        setGradeOn(teamMember.get(), criteria, newValue);
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
