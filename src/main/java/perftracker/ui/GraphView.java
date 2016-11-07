package perftracker.ui;

import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;
import perftracker.domain.PerformanceTrackingSystem;

import javax.swing.*;
import java.awt.*;

import static perftracker.domain.CriteriaType.HARDSKILL;
import static perftracker.domain.CriteriaType.SOFTSKILL;

@Component
public class GraphView extends JPanel {

    //todo: team table to (multi)select what should be rendered

    private PerformanceTrackingSystem system;

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        //todo: repaint on changes
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (system != null) {
            int maxHardskillGrade = //todo: move getMax(type) to system
                    system.getCriteria().stream().filter(c -> HARDSKILL == c.getType()).mapToInt(Criteria::getMaxGrade).sum();
            int maxSoftskillGrade =
                    system.getCriteria().stream().filter(c -> SOFTSKILL == c.getType()).mapToInt(Criteria::getMaxGrade).sum();
            g.setColor(Color.BLACK);
            system.getTeam().forEach(tm -> {
                int x = maxSoftskillGrade != 0 ?
                        (tm.getTotalGrade(SOFTSKILL) * getWidth()) / maxSoftskillGrade
                        :
                        getWidth() / 2;
                int y = maxHardskillGrade != 0 ?
                        (tm.getTotalGrade(HARDSKILL) * getHeight()) / maxHardskillGrade
                        :
                        getHeight() / 2;
                y = getHeight() - y;

                g.drawString(tm.getName(), x, y);
                g.fillOval(x - 3, y - 3, 6, 6);
            });
        }
    }
}
