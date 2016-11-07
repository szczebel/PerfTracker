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
    private TeamMemberViewContainer teamMemberViewContainer;
    @Autowired
    private GraphView graphView;
    @Autowired
    private DirtyTracker dirtyTracker;

    public void bindAllTo(PerformanceTrackingSystem system) {
        dirtyTracker.bindTo(system);
        criteriaView.bindTo(system);
        graphView.bindTo(system);
        teamView.bindTo(system);
        teamMemberViewContainer.bindTo(system);
    }
}
