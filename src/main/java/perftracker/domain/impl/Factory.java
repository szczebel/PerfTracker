package perftracker.domain.impl;

import org.springframework.stereotype.Component;
import perftracker.domain.*;

@Component
public class Factory {

    public PerformanceTrackingSystem createEmptySystem() {
        return new PTSImpl();
    }
}
