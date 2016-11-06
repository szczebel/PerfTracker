package perftracker.domain.impl;

import org.springframework.stereotype.Component;
import perftracker.domain.*;

import static perftracker.domain.CriteriaType.HARDSKILL;
import static perftracker.domain.CriteriaType.SOFTSKILL;

@Component
public class Factory {
    public Criteria createCriteria(String name, CriteriaType type, int maxGrade) {
        return new CriteriaImpl(name, type, maxGrade);
    }

    public Class<? extends PerformanceTrackingSystem> getPTSClass() {
        return PTSImpl.class;
    }

    public PerformanceTrackingSystem createEmptySystem() {
        PTSImpl pts = new PTSImpl();
//        pts.addCriteria(createCriteria("Being nice to strangers", SOFTSKILL, 10));
//        pts.addCriteria(createCriteria("Walking elderly through the street", SOFTSKILL, 10));
//        pts.addCriteria(createCriteria("Brushing teeth", SOFTSKILL, 10));
//        pts.addCriteria(createCriteria("Coding in Java", HARDSKILL, 10));
//        pts.addCriteria(createCriteria("Coding in Python", HARDSKILL, 10));
//        pts.addCriteria(createCriteria("Coding in Erlang", HARDSKILL, 10));
//
//        pts.addTeamMember("John Rambo");
//        pts.addTeamMember("Wayne Gretzky");
//        pts.addTeamMember("Lucky Luke");
//        pts.addTeamMember("Queen Elizabeth");
//        pts.addTeamMember("Bruce Wayne");
//        pts.addTeamMember("Mick Jagger");
//        pts.addTeamMember("Harry Potter");
//        pts.addTeamMember("Dirty Harry");
        return pts;
    }
}
