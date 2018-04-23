package perftracker.ui;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swingutils.frame.RichFrame;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static swingutils.components.ComponentFactory.*;
import static swingutils.layout.LayoutBuilders.borderLayout;

@Component
public class MainFrame extends RichFrame {

    private static final String TEAM = "Team";
    private static final String COMPARISON_GRAPH = "Comparison graph";
    private static final String CRITERIA = "Criteria";

    @Autowired
    TeamView teamView;
    @Autowired
    CriteriaView criteriaView;
    @Autowired
    GraphView graphView;
    @Autowired
    FileViewBuilder fileViewBuilder;
    @Autowired
    TeamMemberViewContainer detailsContainer;
    @Autowired
    StatusBar statusBar;
    @Autowired
    LogsPresenter logsPresenter;
    @Autowired
    Close close;

    void build() {
        this.setIconImage(new ImageIcon((getClass().getResource("/icon.png"))).getImage());
        this.setTitle("Performance Matrix");
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(close);

        this.add(borderLayout()
                .north(fileViewBuilder.build())
                .center(createCenter())
                .south(borderLayout()
                        .center(statusBar.getComponent())
                        .east(button("Logs", () -> logsPresenter.showLogs(this)))
                        .build()
                )
                .build());
    }

    private JComponent createCenter() {

        return splitPane(HORIZONTAL_SPLIT,
                splitPane(VERTICAL_SPLIT,
                        decorate(criteriaView.build()).withGradientHeader(CRITERIA).withEmptyBorder(0, 4, 0, 0).minSize(200, 200).get(),
                        splitPane(VERTICAL_SPLIT,
                                decorate(teamView.getComponent()).withGradientHeader(TEAM).withEmptyBorder(4, 4, 0, 0).minSize(200, 300).get(),
                                decorate(detailsContainer.getComponent()).withEmptyBorder(4, 4, 4, 0).prefSize(200, 200).get()
                        )
                ),
                decorate(graphView.getComponent()).withGradientHeader(COMPARISON_GRAPH).withEmptyBorder(0, 4, 4, 4).get()
        );
    }

}
//todo pdf
//todo remove team member
