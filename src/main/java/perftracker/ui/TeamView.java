package perftracker.ui;

import ca.odell.glazedlists.EventList;
import org.jdesktop.swingx.painter.MattePainter;
import org.springframework.stereotype.Component;
import perftracker.domain.PerformanceTrackingSystem;
import perftracker.domain.TeamMember;
import swingutils.components.table.FactorialPainterHighlighter;
import swingutils.components.table.TableFactory;
import swingutils.components.table.TablePanel;
import swingutils.components.table.descriptor.ColumnAction;
import swingutils.components.table.descriptor.Columns;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import static perftracker.domain.CriteriaType.HARDSKILL;
import static perftracker.domain.CriteriaType.SOFTSKILL;
import static swingutils.EventListHelper.*;
import static swingutils.components.ComponentFactory.button;
import static swingutils.components.ComponentFactory.label;
import static swingutils.layout.LayoutBuilders.borderLayout;
import static swingutils.layout.LayoutBuilders.flowLayout;

@Component
public class TeamView {

    private final JComponent component;

    private PerformanceTrackingSystem system;
    private EventList<Row> viewModel = eventList();
    private TablePanel<Row> tablePanel;
    private java.util.List<Consumer<TeamMember>> selectionListeners = new ArrayList<>();

    public TeamView() {
        this.component = build();
    }

    TeamMember getSelectedTeamMember() {
        Row selection = tablePanel.getSelection();
        return selection != null ? selection.teamMember : null;
    }

    void whenSelectionChanged(Consumer<TeamMember> listener) {
        selectionListeners.add(listener);
    }

    JComponent getComponent() {
        return component;
    }

    private JComponent build() {
        createTeamTable();
        return borderLayout()
                .center(tablePanel.getComponent())
                .south(buildAddNewTeamMemberPanel())
                .build();
    }

    //todo: fix dropping on resize caused by flow layout

    private void notifyListenersOnSelectionChange() {
        tablePanel.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                TeamMember selection = getSelectedTeamMember();
                selectionListeners.forEach(l -> l.accept(selection));
            }
        });
    }

    private void createTeamTable() {
        ColumnAction<Row> deleteAction = new ColumnAction<>(
                new ImageIcon(getClass().getResource("/delete.png")),
                "Delete this team member",
                this::deleteClicked);

        tablePanel = TableFactory
                .createTablePanel(
                        viewModel,
                        Columns.create(Row.class)
                                .column("Name", String.class, Row::getName)
                                .column(HARDSKILL + "s total score", Integer.class, Row::getTotalHardskillGrade)
                                .column(SOFTSKILL + "s total score", Integer.class, Row::getTotalSoftskillGrade)
                                .column(deleteAction)

                );
        tablePanel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePanel.getTable().addHighlighter(factorial(1, hardskillScorePercentage()));
        tablePanel.getTable().addHighlighter(factorial(2, softskillScorePercentage()));
        notifyListenersOnSelectionChange();
    }

    private void deleteClicked(Row row) {
        system.deleteTeamMember(row.teamMember);
    }

    private FactorialPainterHighlighter factorial(int column, Function<Integer, Float> factorFunction) {
        return new FactorialPainterHighlighter(new MattePainter(GUI.FACTORIAL_COLOR), column, factorFunction);
    }

    private Function<Integer, Float> softskillScorePercentage() {
        return modelRowIndex -> {
            Row row = viewModel.get(modelRowIndex);
            return row.getTotalSoftskillGrade() / (float) system.getMaxGrade(SOFTSKILL);
        };
    }

    private Function<Integer, Float> hardskillScorePercentage() {
        return modelRowIndex -> {
            Row row = viewModel.get(modelRowIndex);
            return row.getTotalHardskillGrade() / (float) system.getMaxGrade(HARDSKILL);
        };
    }

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        clearEventList(viewModel);

        system.getTeam().forEach(this::addTeamMemberToViewModel);
        system.whenTeamMemberAdded(this::addTeamMemberToViewModel);
        system.whenTeamMemberDeleted(this::removeTeamMemberFromViewModel);
        system.whenCriteriaDeleted(c -> tablePanel.getTable().repaint());
    }

    private JComponent buildAddNewTeamMemberPanel() {
        JTextField textField = new JTextField(15);
        return flowLayout(FlowLayout.RIGHT,
                label("New:"),
                textField,
                button("Add", () -> {
                    String trimmed = textField.getText().trim();
                    if (!trimmed.isEmpty()) system.addTeamMember(trimmed);
                })
        );
    }

    private void removeTeamMemberFromViewModel(TeamMember tm) {
        viewModel.remove(findRow(tm));
    }

    private void addTeamMemberToViewModel(TeamMember tm) {
        addToList(viewModel, new Row(tm));
        tm.whenScoreChanged((s, i) -> resetTeamMember(tm));
    }

    private void resetTeamMember(TeamMember tm) {
        Row row = findRow(tm);
        setInList(viewModel, viewModel.indexOf(row), row);
    }

    private Row findRow(TeamMember tm) {
        return viewModel.stream().filter(r -> tm.getName().equals(r.getName())).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    private class Row {
        final TeamMember teamMember;

        Row(TeamMember teamMember) {
            this.teamMember = teamMember;
        }

        String getName() {
            return teamMember.getName();
        }

        int getTotalHardskillGrade() {
            return teamMember.getTotalScore(HARDSKILL);
        }

        int getTotalSoftskillGrade() {
            return teamMember.getTotalScore(SOFTSKILL);
        }
    }
}
