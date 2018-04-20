package perftracker.ui;

import ca.odell.glazedlists.EventList;
import org.jdesktop.swingx.painter.MattePainter;
import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.components.table.FactorialPainterHighlighter;
import swingutils.components.table.TableFactory;
import swingutils.components.table.TablePanel;
import swingutils.components.table.descriptor.Columns;

import javax.swing.*;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static perftracker.ui.Icons.MINUS;
import static perftracker.ui.Icons.PLUS;
import static swingutils.EventListHelper.*;

@Component
public class TeamMemberViewBuilder {

    JComponent build(TeamMember teamMember, PerformanceTrackingSystem system) {

        EventList<Row> viewModel = eventList();
        teamMember.getScores().forEach((criteria, grade) -> addToList(viewModel, new Row(criteria, grade)));
        teamMember.whenScoreChanged((criteria, newGrade) -> onScoreChange(viewModel, criteria, newGrade));
        system.whenCriteriaDeleted(criteria -> viewModel.removeIf(row -> row.criteria == criteria));

        BiConsumer<Row, Integer> setter = (row, newValue) -> system.updateScore(teamMember.getName(), row.criteria, newValue);
        Consumer<Row> scoreUp = row -> system.updateScore(teamMember.getName(), row.criteria, row.score + 1);
        Consumer<Row> scoreDown = row -> system.updateScore(teamMember.getName(), row.criteria, row.score - 1);

        TablePanel<Row> tablePanel = TableFactory.createTablePanel(
                viewModel,
                Columns.create(Row.class)
                        .column("Criteria", String.class, Row::getCriteriaName)
                        .column("Score", Integer.class, Row::getScore, setter) //todo: red outline when out of range
                        .actionable("Increase", PLUS.icon, scoreUp)
                        .actionable("Decrease", MINUS.icon, scoreDown)
        );
        tablePanel.getTable().packAll();

        addFractionalHighlighter(viewModel, tablePanel);
        return tablePanel.getScrollPane();
    }

    private void addFractionalHighlighter(EventList<Row> viewModel, TablePanel<Row> tablePanel) {
        tablePanel.getTable().addHighlighter(new FactorialPainterHighlighter(
                new MattePainter(GUI.FACTORIAL_COLOR), 1,
                modelRowIndex -> {
                    Row row = viewModel.get(modelRowIndex);
                    return row.score / (float) row.criteria.getMaxScore();
                }
        ));
    }

    private void onScoreChange(EventList<Row> viewModel, Criteria criteria, Integer newScore) {
        Optional<Row> row = viewModel.stream().filter(r -> criteria.equals(r.criteria)).findFirst();
        if (row.isPresent()) {
            int index = viewModel.indexOf(row.get());
            setInList(viewModel, index, new Row(criteria, newScore));
        } else {
            addToList(viewModel, new Row(criteria, newScore));
        }
    }

    private static class Row {
        final Criteria criteria;
        final int score;

        Row(Criteria criteria, int score) {
            this.criteria = criteria;
            this.score = score;
        }

        String getCriteriaName() {
            return criteria.getName();
        }

        CriteriaType getType() {
            return criteria.getType();
        }

        int getScore() {
            return score;
        }
    }
}
