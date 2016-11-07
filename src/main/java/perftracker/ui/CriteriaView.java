package perftracker.ui;

import ca.odell.glazedlists.EventList;
import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;
import perftracker.domain.PerformanceTrackingSystem;
import swingutils.components.table.TableFactory;
import swingutils.components.table.TablePanel;
import swingutils.components.table.descriptor.Columns;

import javax.swing.*;
import java.awt.*;

import static swingutils.EventListHelper.clearEventList;
import static swingutils.EventListHelper.eventList;
import static swingutils.components.ComponentFactory.button;
import static swingutils.components.ComponentFactory.label;
import static swingutils.layout.LayoutBuilders.borderLayout;
import static swingutils.layout.LayoutBuilders.flowLayout;

@Component
public class CriteriaView {

    private PerformanceTrackingSystem system;
    private EventList<Criteria> viewModel = eventList();

    JComponent build() {

        TablePanel<Criteria> tablePanel = TableFactory
                .createTablePanel(
                        viewModel,
                        Columns.create(Criteria.class)
                                .column("Name", String.class, Criteria::getName)
                                .column("Type", CriteriaType.class, Criteria::getType)
                                .column("Max grade", Integer.class, Criteria::getMaxGrade)
                );
        tablePanel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return borderLayout()
                .center(tablePanel.getComponent())
                .south(buildAddNewCriterionPanel())
                .build();
    }

    private JComponent buildAddNewCriterionPanel() {
        JTextField textField = new JTextField(15);
        SpinnerNumberModel numberModel = new SpinnerNumberModel(10, 1, 100, 1);
        JComboBox<CriteriaType> typeSelector = new JComboBox<>(CriteriaType.values());
        return flowLayout(FlowLayout.CENTER,
                label("New:"),
                textField,
                label("Type:"),
                typeSelector,
                label("Max grade:"),
                new JSpinner(numberModel),
                button("Add", () -> system.addCriteria(
                        textField.getText().trim(),
                        (CriteriaType) typeSelector.getSelectedItem(),
                        numberModel.getNumber().intValue()))
        );
    }

    void bindTo(PerformanceTrackingSystem system) {
        this.system = system;
        clearEventList(viewModel);
        viewModel.addAll(system.getCriteria());
        system.whenCriteriaAdded(viewModel::add);
    }

}