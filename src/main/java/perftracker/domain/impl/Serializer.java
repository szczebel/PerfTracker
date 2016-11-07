package perftracker.domain.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Serializer {

    private final ObjectMapper jackson = new ObjectMapper();

    public PerformanceTrackingSystem fromFile(File file) throws IOException {
        PersistentPTS persistentPTS = jackson.readValue(file, PersistentPTS.class);
        PTSImpl pts = new PTSImpl();
        pts.criteria.addAll(persistentPTS.criteria);

        for (PersistentTeamMember ptm : persistentPTS.team) {
            pts.addTeamMember(ptm.name);
            for (Map.Entry<String, Integer> grade : ptm.grades.entrySet()) {
                pts.updateGrade(ptm.name, persistentPTS.findCriteriaByName(grade.getKey()), grade.getValue());
            }
        }
        return pts;
    }

    public void toFile(File file, PerformanceTrackingSystem system) throws IOException {
        PTSImpl pts = (PTSImpl) system;
        PersistentPTS persistentPTS = new PersistentPTS();
        persistentPTS.criteria = pts.criteria;
        pts.getTeam().forEach(tm -> {
            PersistentTeamMember ptm = new PersistentTeamMember();
            ptm.name = tm.getName();
            persistentPTS.team.add(ptm);
            tm.getGrades().forEach((crit, grade) -> ptm.grades.put(crit.getName(), grade));
        });
        jackson.writeValue(file, persistentPTS);
    }

    @SuppressWarnings("unused")
    private static class PersistentPTS {
        List<CriteriaImpl> criteria = new ArrayList<>();
        List<PersistentTeamMember> team = new ArrayList<>();

        public List<CriteriaImpl> getCriteria() {
            return criteria;
        }

        public void setCriteria(List<CriteriaImpl> criteria) {
            this.criteria = criteria;
        }

        public List<PersistentTeamMember> getTeam() {
            return team;
        }

        public void setTeam(List<PersistentTeamMember> team) {
            this.team = team;
        }

        CriteriaImpl findCriteriaByName(String name) throws IOException {
            return criteria.stream().filter(c -> name.equals(c.getName())).findFirst().orElseThrow(IOException::new);//todo: descriptive error message
        }
    }

    @SuppressWarnings("unused")
    private static class PersistentTeamMember {
        String name;
        Map<String, Integer> grades = new HashMap<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Integer> getGrades() {
            return grades;
        }

        public void setGrades(Map<String, Integer> grades) {
            this.grades = grades;
        }
    }
}
