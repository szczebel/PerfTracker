package perftracker.ui;

import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import swingutils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

import static perftracker.domain.CriteriaType.HARDSKILL;
import static perftracker.domain.CriteriaType.SOFTSKILL;

@Component
public class GraphView extends JPanel {

    //todo: select what should be rendered (all or one)

    private PerformanceTrackingSystem system;

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        repaint();

        system.whenCriteriaAdded(c -> repaint());
        system.whenTeamMemberAdded(t -> {
            repaint();
            t.whenGradeChanged((c, i) -> repaint());
        });
        system.getTeam().forEach(t -> t.whenGradeChanged((c, i) -> repaint()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(Color.white);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (system != null) {

            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);

            int maxHardskillGrade = system.getMaxGrade(HARDSKILL);
            int maxSoftskillGrade = system.getMaxGrade(SOFTSKILL);

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

                g.setColor(Colors.niceBlue);
                g.drawString(tm.getName(), x + 5, y - 5);
                g.setColor(Colors.niceOrange);
                g2.fill(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
                //g.fillOval(x - 3, y - 3, 6, 6);
            });
        }
    }
}
