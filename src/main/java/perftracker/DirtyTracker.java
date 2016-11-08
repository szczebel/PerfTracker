package perftracker;

import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;

import java.util.function.Consumer;

@Component
public class DirtyTracker {

    private boolean dirty;

    public void bindTo(PerformanceTrackingSystem system) {
        setDirty(false);
        system.getTeam().forEach(tm -> tm.whenScoreChanged((s, i) -> setDirty(true)));
        system.whenTeamMemberAdded(tm -> {
            setDirty(true);
            tm.whenScoreChanged((s, i) -> setDirty(true));
        });
        system.whenCriteriaAdded(c -> setDirty(true));
        system.whenCriteriaDeleted(c -> setDirty(true));
        system.whenTeamMemberDeleted(tm -> setDirty(true));
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        listener.accept(dirty);
    }

    public boolean isDirty() {
        return dirty;
    }

    private Consumer<Boolean> listener;
    public void whenDirtyChanged(Consumer<Boolean> listener) {
        this.listener = listener;
    }
}
