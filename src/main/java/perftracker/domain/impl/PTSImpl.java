package perftracker.domain.impl;

import perftracker.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;


class PTSImpl implements PerformanceTrackingSystem {


    List<CriteriaImpl> criteria = new ArrayList<>();
    private List<TeamMemberImpl> team = new ArrayList<>();
    private List<Consumer<Criteria>> addCriteriaListeners = new ArrayList<>();
    private List<Consumer<Criteria>> deleteCriteriaListeners = new ArrayList<>();
    private List<Consumer<TeamMember>> addTeamMemberListeners = new ArrayList<>();
    private List<Consumer<TeamMember>> deleteTeamMemberListeners = new ArrayList<>();

    @Override
    public List<Criteria> getCriteria() {
        return Collections.unmodifiableList(criteria);
    }

    private Optional<Criteria> findOptionalCriteria(String criteriaName) {
        return getCriteria().stream().filter(c -> criteriaName.equals(c.getName())).findFirst();
    }

    @Override
    public List<TeamMember> getTeam() {
        return Collections.unmodifiableList(team);
    }

    @Override
    public void addCriteria(String name, CriteriaType type, int maxGrade) {
        if(findOptionalCriteria(name).isPresent()) throw new IllegalArgumentException(name + " already exists");
        CriteriaImpl newCriteria = new CriteriaImpl(name, type, maxGrade);
        criteria.add(newCriteria);
        team.forEach(tm -> setGradeOn(tm, newCriteria));
        addCriteriaListeners.forEach(criteriaConsumer -> criteriaConsumer.accept(newCriteria));
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
    public void updateScore(String teamMemberName, Criteria criterion, int newValue) {
        validateCriteria(criterion);
        if(newValue<0 || newValue> criterion.getMaxScore()) throw new IllegalArgumentException("Grade out of range");
        setGradeOn(findTeamMember(teamMemberName), criterion, newValue);
    }

    private TeamMemberImpl findTeamMember(String teamMemberName) {
        return team.stream().filter(tm -> teamMemberName.equals(tm.getName())).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    private void validateCriteria(Criteria criterion) {
        //noinspection SuspiciousMethodCalls
        if(!this.criteria.contains(criterion)) throw new IllegalArgumentException("Unknown criteria" + criterion.getName());
    }

    @Override
    public void deleteCriteria(Criteria criteria) {
        validateCriteria(criteria);
        team.forEach(tm -> tm.deleteCriteria(criteria));
        this.criteria.remove(criteria);
        deleteCriteriaListeners.forEach(l -> l.accept(criteria));
    }

    @Override
    public void deleteTeamMember(TeamMember teamMember) {
        TeamMemberImpl tm = findTeamMember(teamMember.getName());
        this.team.remove(tm);
        deleteTeamMemberListeners.forEach(l -> l.accept(tm));
    }

    private void setGradeOn(TeamMemberImpl tm, Criteria newCriteria) {
        setGradeOn(tm, newCriteria, newCriteria.getMaxScore()/2);
    }
    private void setGradeOn(TeamMemberImpl tm, Criteria criteria, int value) {
        tm.setGrade(criteria, value);
    }

    @Override
    public void whenCriteriaAdded(Consumer<Criteria> listener) {
        addCriteriaListeners.add(listener);
    }

    @Override
    public void whenCriteriaDeleted(Consumer<Criteria> listener) {
        deleteCriteriaListeners.add(listener);
    }

    @Override
    public void whenTeamMemberAdded(Consumer<TeamMember> listener) {
        addTeamMemberListeners.add(listener);
    }

    @Override
    public void whenTeamMemberDeleted(Consumer<TeamMember> listener) {
        deleteTeamMemberListeners.add(listener);
    }


    @Override
    public int getMaxGrade(CriteriaType type) {
        return getCriteria().stream().filter(isOfType(type)).mapToInt(Criteria::getMaxScore).sum();
    }

    private Predicate<Criteria> isOfType(CriteriaType type) {
        return c -> type == c.getType();
    }
}
