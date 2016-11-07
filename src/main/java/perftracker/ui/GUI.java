package perftracker.ui;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swingutils.frame.RichFrame;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static swingutils.components.ComponentFactory.decorate;
import static swingutils.components.ComponentFactory.splitPane;
import static swingutils.layout.LayoutBuilders.borderLayout;
import static swingutils.layout.LayoutBuilders.gridLayout;

@Component
public class GUI {


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
    Close close;

    public void show() {
        RichFrame f = new RichFrame();
        f.setIconImage(new ImageIcon((getClass().getResource("/icon.png"))).getImage());
        f.setTitle("Performance Matrix");
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(close);


        f.add(borderLayout()
                .north(fileViewBuilder.build())
//                .north(gridLayout(1,2,statusBar.getComponent(),fileViewBuilder.build()))
                .center(createCenter())
                .south(statusBar.getComponent())
                .build());
        f.setVisible(true);
    }

    private JComponent createCenter() {
        return splitPane(HORIZONTAL_SPLIT,
                splitPane(VERTICAL_SPLIT,
                        decorate(criteriaView.build()).withGradientHeader(CRITERIA).withEmptyBorder(0, 4, 0, 0).minSize(200,200).get(),
                        splitPane(VERTICAL_SPLIT,
                                decorate(teamView.getComponent()).withGradientHeader(TEAM).withEmptyBorder(4, 4, 0, 0).minSize(200,300).get(),
                                decorate(detailsContainer.getComponent()).withEmptyBorder(4,4,4,0).prefSize(200,200).get()
                        )
                ),
                decorate(graphView.getComponent()).withGradientHeader(COMPARISON_GRAPH).withEmptyBorder(0, 4, 4, 4).get()
        );

    }

}
