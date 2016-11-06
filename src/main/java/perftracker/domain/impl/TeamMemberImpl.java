package perftracker.domain.impl;

import perftracker.domain.TeamMember;

import java.util.*;
import java.util.function.BiConsumer;

class TeamMemberImpl implements TeamMember {
    private String name;
    private Map<String, Integer> grades = new HashMap<>();

    @SuppressWarnings("unused")
    TeamMemberImpl() {}

    TeamMemberImpl(String key) {
        this.name = key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Integer> getGrades() {
        return Collections.unmodifiableMap(grades);
    }

    void setGrade(String key, int value){
        grades.put(key, value);
        listeners.forEach(l -> l.accept(key, value));
    }

    private List<BiConsumer<String, Integer>> listeners = new ArrayList<>();
    @Override
    public void whenGradeChanged(BiConsumer<String, Integer> listener) {
        listeners.add(listener);
    }
}
