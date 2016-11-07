package perftracker.ui;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swingutils.frame.RichFrame;
import swingutils.layout.cards.CardMenuBuilder;
import swingutils.layout.cards.CardMenuBuilders;
import swingutils.layout.cards.MenuPlacement;

import javax.swing.*;
import java.awt.*;

import static swingutils.components.ComponentFactory.decorate;
import static swingutils.layout.LayoutBuilders.borderLayout;
import static swingutils.layout.cards.CardLayoutBuilder.cardLayout;

@Component
public class GUI {


    private static final String TEAM = "Team";
    private static final String COMPARISON_GRAPH = "Comparison graph";
    private static final String CRITERIA = "Criteria";

    @Autowired TeamView teamView;
    @Autowired CriteriaView criteriaView;
    @Autowired GraphView graphView;
    @Autowired FileViewBuilder fileViewBuilder;
    @Autowired StatusBar statusBar;
    @Autowired Close close;

    public void show() {
        RichFrame f = new RichFrame();
        f.setIconImage(new ImageIcon((getClass().getResource("/icon.png"))).getImage());
        f.setTitle("Performance Tracker");
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        f.addWindowListener(close);


        CardMenuBuilder<JComponent> cardMenuBuilder = CardMenuBuilders.NoBorderOrange()
                .menuPlacement(MenuPlacement.LEFT)
                .menuBarCustomizer(menu -> decorate(menu).withEmptyBorder(8, 8, 8, 4).get());

        JComponent cards = cardLayout(cardMenuBuilder)
                .addTab(TEAM, decorate(teamView.build()).withEmptyBorder(8, 4, 8, 8).get())
                .addTab(COMPARISON_GRAPH, createDecoratedPanel(COMPARISON_GRAPH, graphView))
                .addTab(CRITERIA, createDecoratedPanel(CRITERIA, criteriaView.build()))
                .build();

        f.add(borderLayout()
                .north(fileViewBuilder.build())
                .center(cards)
                .south(statusBar.getComponent())
                .build());
        f.setVisible(true);
    }

    private JComponent createDecoratedPanel(String title, JComponent component) {
        return decorate(component).withGradientHeader(title).withEmptyBorder(8, 4, 8, 8).get();
    }

}
