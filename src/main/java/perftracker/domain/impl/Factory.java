package perftracker.domain.impl;

import org.springframework.stereotype.Component;
import perftracker.domain.*;

@Component
public class Factory {
    public Criteria createCriteria(String name, CriteriaType type, int maxGrade) {
        return new CriteriaImpl(name, type, maxGrade);
    }

    public Class<? extends PerformanceTrackingSystem> getPTSClass() {
        return PTSImpl.class;
    }

    public PerformanceTrackingSystem createEmptySystem() {
        return new PTSImpl();
    }
}
