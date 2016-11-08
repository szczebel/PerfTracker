package perftracker.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import swingutils.Colors;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

import static perftracker.domain.CriteriaType.HARDSKILL;
import static perftracker.domain.CriteriaType.SOFTSKILL;

@Component
public class GraphView {

    @Autowired
    TeamMemberSelection teamMemberSelection;
    @PostConstruct
    void init() {
        teamMemberSelection.whenSelectionChanged(teamMember -> repaint());
    }

    private GraphCanvas canvas = new GraphCanvas();
    private PerformanceTrackingSystem system;

    JComponent getComponent() {
        return canvas;
    }

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        repaint();

        system.whenCriteriaAdded(c -> repaint());
        system.whenTeamMemberAdded(t -> {
            repaint();
            t.whenScoreChanged((c, i) -> repaint());
        });
        system.getTeam().forEach(t -> t.whenScoreChanged((c, i) -> repaint()));
    }

    private void repaint() {
        canvas.repaint();
    }

    private class GraphCanvas extends JPanel {
        static final int AXIS_MARGIN = 30;

        @Override
        protected void paintComponent(Graphics g) {
            setBackground(Color.white);
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);

            paintAxis(g2);
            if (system != null) {

                int maxHardskillGrade = system.getMaxGrade(HARDSKILL);
                int maxSoftskillGrade = system.getMaxGrade(SOFTSKILL);
                int usableWidth = getWidth() - AXIS_MARGIN;
                int usableHeight = getHeight() - AXIS_MARGIN;

                system.getTeam().forEach(tm -> {
                    int x = maxSoftskillGrade != 0 ?
                            (tm.getTotalScore(SOFTSKILL) * usableWidth) / maxSoftskillGrade
                            :
                            usableWidth / 2;
                    int y = maxHardskillGrade != 0 ?
                            (tm.getTotalScore(HARDSKILL) * usableHeight) / maxHardskillGrade
                            :
                            usableHeight / 2;
                    y = usableHeight - y;
                    x = x + AXIS_MARGIN;

                    boolean selected = teamMemberSelection.get() == tm;
                    g.setColor(selected ? Color.red : Colors.niceBlue);
                    g.drawString(tm.getName(), x + 5, y - 5);
                    g.setColor(selected ? Color.red : Colors.niceOrange);
                    g2.fill(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
                });
            }
        }

        private void paintAxis(Graphics2D g2) {
            g2.setColor(Colors.niceBlue);

            int x0 = AXIS_MARGIN;
            int y0 = getHeight() - AXIS_MARGIN;

            Point xAxisEnd = new Point(getWidth() - AXIS_MARGIN, y0);
            Point yAxisEnd = new Point(x0, AXIS_MARGIN);

            g2.drawLine(x0, y0, xAxisEnd.x, xAxisEnd.y);
            g2.drawLine(xAxisEnd.x, xAxisEnd.y, xAxisEnd.x - 30, xAxisEnd.y - 10);
            g2.drawLine(xAxisEnd.x, xAxisEnd.y, xAxisEnd.x - 30, xAxisEnd.y + 10);

            g2.drawLine(x0, y0, yAxisEnd.x, yAxisEnd.y);
            g2.drawLine(yAxisEnd.x, yAxisEnd.y, yAxisEnd.x - 10, yAxisEnd.y + 30);
            g2.drawLine(yAxisEnd.x, yAxisEnd.y, yAxisEnd.x + 10, yAxisEnd.y + 30);

            int fontHeight = 20;//give or take
            g2.setColor(Color.black);
            g2.setFont(getFont().deriveFont(Font.BOLD));
            g2.drawString("Soft skills score >>>", x0 + AXIS_MARGIN, y0 + fontHeight);
            drawRotatedText(g2, fontHeight, y0 - AXIS_MARGIN, -90, "Hard skills score >>>");
        }

        void drawRotatedText(Graphics2D g2d, double x, double y, int angle, String text) {
            g2d.translate((float) x, (float) y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text, 0, 0);
            g2d.rotate(-Math.toRadians(angle));
            g2d.translate(-(float) x, -(float) y);
        }
    }
}
