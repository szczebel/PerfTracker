package perftracker.ui;

import ca.odell.glazedlists.EventList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import perftracker.domain.CriteriaType;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import perftracker.domain.impl.Factory;
import swingutils.components.table.TableFactory;
import swingutils.components.table.TablePanel;
import swingutils.components.table.descriptor.Columns;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.Predicate;

import static perftracker.domain.CriteriaType.HARDSKILL;
import static perftracker.domain.CriteriaType.SOFTSKILL;
import static swingutils.EventListHelper.*;
import static swingutils.components.ComponentFactory.*;
import static swingutils.layout.LayoutBuilders.borderLayout;
import static swingutils.layout.LayoutBuilders.flowLayout;
import static swingutils.layout.LayoutBuilders.gridLayout;

@Component
public class TeamView {

    @Autowired
    private Factory domainFactory;
    @Autowired
    private TeamMemberViewContainer3 detailsContainer;

    private PerformanceTrackingSystem system;
    private EventList<Row> viewModel = eventList();

    JComponent build() {
        TablePanel<Row> tablePanel = createTeamTable();
        showDetailsOnSelectionChange(tablePanel);

        return gridLayout(1, 2,
                withGradientHeader(borderLayout()
                        .south(buildAddNewTeamMemberPanel())
                        .center(tablePanel.getComponent())
                        .build(), "Team"),
                decorate(detailsContainer.getComponent()).withEmptyBorder(0, 8, 0, 0).get()
        );
    }

    private TablePanel<Row> createTeamTable() {
        TablePanel<Row> tablePanel = TableFactory
                .createTablePanel(
                        viewModel,
                        Columns.create(Row.class)
                                .column("Name", String.class, Row::getName)
                                .column(HARDSKILL.name() + " total grade", Integer.class, Row::getTotalHardskillGrade)
                                .column(SOFTSKILL.name() + " total grade", Integer.class, Row::getTotalSoftskillGrade)
                );
        tablePanel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tablePanel;
    }

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        clearEventList(viewModel);
        detailsContainer.resetWith(system);

        system.getTeam().forEach(this::addTeamMemberToViewModel);
        system.whenTeamMemberAdded(this::addTeamMemberToViewModel);
    }

    private void showDetailsOnSelectionChange(TablePanel<Row> tablePanel) {
        tablePanel.getTable().getSelectionModel().addListSelectionListener(e -> {
            Row selection = tablePanel.getSelection();
            if (!e.getValueIsAdjusting() && selection != null) {
                detailsContainer.showDetails(selection.teamMember);
            }
        });
    }

    private JComponent buildAddNewTeamMemberPanel() {
        JTextField textField = new JTextField(30);
        return flowLayout(FlowLayout.CENTER,
                label("Name of new team member:"),
                textField,
                button("Add", () -> system.addTeamMember(textField.getText().trim()))
        );
    }

    private void addTeamMemberToViewModel(TeamMember tm) {
        addToList(viewModel, new Row(tm));
        tm.whenGradeChanged((s, i) -> onGradeChanged(tm));
    }

    private void onGradeChanged(TeamMember tm) {
        Row row = viewModel.stream().filter(r -> tm.getName().equals(r.getName())).findFirst().orElseThrow(IllegalArgumentException::new);
        setInList(viewModel, viewModel.indexOf(row), row);
    }

    private class Row {
        final TeamMember teamMember;

        Row(TeamMember teamMember) {
            this.teamMember = teamMember;
        }

        String getName() {
            return teamMember.getName();
        }

        int getTotalHardskillGrade() {//todo: cache it
            return teamMember.getGrades().entrySet().stream()
                    .filter(by(HARDSKILL))
                    .mapToInt(Map.Entry::getValue).sum();
        }

        int getTotalSoftskillGrade() {//todo: cache it
            return teamMember.getGrades().entrySet().stream()
                    .filter(by(SOFTSKILL))
                    .mapToInt(Map.Entry::getValue).sum();

        }

        private Predicate<Map.Entry<String, Integer>> by(CriteriaType type) {
            return gradeEntry -> type == system.findCriteria(gradeEntry.getKey()).getType();
        }
    }
}
