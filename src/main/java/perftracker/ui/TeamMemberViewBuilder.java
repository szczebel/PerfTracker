package perftracker.ui;

import ca.odell.glazedlists.EventList;
import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.components.table.TableFactory;
import swingutils.components.table.TablePanel;
import swingutils.components.table.descriptor.Columns;

import javax.swing.*;
import java.util.Optional;
import java.util.function.BiConsumer;

import static swingutils.EventListHelper.*;

@Component
public class TeamMemberViewBuilder {

    JComponent build(TeamMember teamMember, PerformanceTrackingSystem system) {

        EventList<Row> viewModel = eventList();
        teamMember.getGrades().forEach((critName, grade) -> addToList(viewModel, new Row(critName, grade)));

        teamMember.whenGradeChanged((criteria, newGrade) -> {
            Optional<Row> row = viewModel.stream().filter(r -> criteria.equals(r.criteria)).findFirst();
            if (row.isPresent()) {
                int index = viewModel.indexOf(row.get());
                setInList(viewModel, index, new Row(criteria, newGrade));
            } else {
                addToList(viewModel, new Row(criteria, newGrade));
            }
        });

        BiConsumer<Row, Integer> setter = (row, newValue) ->
                system.updateGrade(teamMember.getName(), row.criteria, newValue);
        TablePanel<Row> tablePanel = TableFactory.createTablePanel(
                viewModel,
                Columns.create(Row.class)
                        .column("Criteria", String.class, Row::getCriteriaName)
                        .column("Type", CriteriaType.class, Row::getType)
                        .column("Grade", Integer.class, Row::getGrade, setter)
        );
        return tablePanel.getComponent();
    }

    private static class Row {
        final Criteria criteria;
        final int grade;

        Row(Criteria criteria, int grade) {
            this.criteria = criteria;
            this.grade = grade;
        }

        String getCriteriaName() {
            return criteria.getName();
        }

        CriteriaType getType() {
            return criteria.getType();
        }

        int getGrade() {
            return grade;
        }
    }
}
