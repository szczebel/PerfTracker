package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;

import javax.swing.*;

@Component
class TeamMemberViewContainer {

    @Autowired
    private TeamMemberViewBuilder teamMemberViewBuilder;

    private PerformanceTrackingSystem system;
    private JTabbedPane tabsWithDetails = new JTabbedPane();

    public JComponent getComponent() {
        return tabsWithDetails;
    }

    void resetWith(PerformanceTrackingSystem system) {
        this.system = system;
        tabsWithDetails.removeAll();
    }

    void showDetails(TeamMember selection) {
        int tabIndex = tabsWithDetails.indexOfTab(selection.getName());
        if (tabIndex == -1)
            tabsWithDetails.addTab(selection.getName(), teamMemberViewBuilder.build(selection, system));
        tabsWithDetails.setSelectedIndex(tabsWithDetails.indexOfTab(selection.getName()));
        //todo: closeable tabs
    }
}
