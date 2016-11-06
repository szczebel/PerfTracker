package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.DirtyTracker;
import perftracker.domain.PerformanceTrackingSystem;

@Component
public class Binder {

    @Autowired
    private CriteriaView criteriaView;
    @Autowired
    private TeamView teamView;
    @Autowired
    private DirtyTracker dirtyTracker;

    public void bindAllTo(PerformanceTrackingSystem system) {
        dirtyTracker.bindTo(system);
        criteriaView.bindTo(system);
        teamView.bindTo(system);
    }
}
