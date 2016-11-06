package perftracker.ui;

import ca.odell.glazedlists.EventList;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.components.table.TableFactory;
import swingutils.components.table.TablePanel;
import swingutils.components.table.descriptor.Columns;

import javax.swing.*;
import java.util.Optional;
import java.util.function.BiConsumer;

import static swingutils.EventListHelper.addToList;
import static swingutils.EventListHelper.eventList;
import static swingutils.EventListHelper.setInList;

@Component
public class TeamMemberViewBuilder {

    JComponent build(TeamMember teamMember, PerformanceTrackingSystem system) {

        EventList<Row> viewModel = eventList();
        teamMember.getGrades().forEach((critName, grade) -> addToList(viewModel, new Row(critName, grade)));

        teamMember.whenGradeChanged((critName, newGrade) -> {
            Optional<Row> row = viewModel.stream().filter(r -> critName.equals(r.getCriteriaName())).findFirst();
            if (row.isPresent()) {
                int index = viewModel.indexOf(row.get());
                setInList(viewModel, index, new Row(critName, newGrade));
            } else {
                addToList(viewModel, new Row(critName, newGrade));
            }
        });

        BiConsumer<Row, Integer> setter = (row, newValue) ->
                system.updateGrade(teamMember.getName(), row.getCriteriaName(), newValue);
        TablePanel<Row> tablePanel = TableFactory.createTablePanel(
                viewModel,
                Columns.create(Row.class)
                        .column("Criteria", String.class, Row::getCriteriaName)
                        .column("Grade", Integer.class, Row::getGrade, setter)
        );
        return tablePanel.getComponent();
    }

    private static class Row {
        final String criteriaName;
        final int grade;

        Row(String criteriaName, int grade) {
            this.criteriaName = criteriaName;
            this.grade = grade;
        }

        String getCriteriaName() {
            return criteriaName;
        }

        int getGrade() {
            return grade;
        }
    }
}
