package perftracker.domain.impl;

import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;
import perftracker.domain.TeamMember;

import java.util.*;
import java.util.function.BiConsumer;

class TeamMemberImpl implements TeamMember {
    private String name;
    private Map<Criteria, Integer> grades = new HashMap<>();
    private List<BiConsumer<Criteria, Integer>> listeners = new ArrayList<>();

    @SuppressWarnings("unused")
    TeamMemberImpl() {
    }

    TeamMemberImpl(String key) {
        this.name = key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<Criteria, Integer> getScores() {
        return Collections.unmodifiableMap(grades);
    }

    void setGrade(Criteria key, int value) {
        grades.put(key, value);
        listeners.forEach(l -> l.accept(key, value));
    }

    @Override
    public int getTotalScore(CriteriaType type) {
        return getScores().entrySet().stream()
                .filter(entry -> type == entry.getKey().getType())
                .mapToInt(Map.Entry::getValue).sum();

    }

    @Override
    public void whenScoreChanged(BiConsumer<Criteria, Integer> listener) {
        listeners.add(listener);
    }
    //todo: add stopWatching to fix memory leak
}
