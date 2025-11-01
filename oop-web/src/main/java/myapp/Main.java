package myapp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application frame
 */
public class Main extends JFrame {
    private final DatabaseManager db;
    private final JPanel toolsContainer;
    private final List<Tool> tools = new ArrayList<>();

    public Main() {
        super("Base Convert");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        db = new DatabaseManager("appdata.db");
        db.setupTables();

        tools.add(new ConvertTool(db));
        tools.add(new CodeTransformTool(db));
        tools.add(new HistoryTool(db));

        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < tools.size(); i++) {
            Tool t = tools.get(i);
            JRadioButton rb = new JRadioButton(t.getName());
            rb.setActionCommand(t.getId());
            bg.add(rb);
            side.add(rb);
            rb.addActionListener(e -> showTool(t.getId()));
            if (i == 0) rb.setSelected(true);
        }
        toolsContainer = new JPanel(new CardLayout());
        for (Tool t : tools) {
            t.buildUI();
            toolsContainer.add(t.getPanel(), t.getId());
        }
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(side, BorderLayout.WEST);
        getContentPane().add(toolsContainer, BorderLayout.CENTER);

        showTool(tools.get(0).getId());
    }

    private void showTool(String id) {
        CardLayout cl = (CardLayout) toolsContainer.getLayout();
        cl.show(toolsContainer, id);
        for (Tool t : tools) {
            boolean active = t.getId().equals(id);
            t.setActive(active);
            if (active) t.onShow(); else t.onHide();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored){}
            Main m = new Main();
            m.setVisible(true);
        });
    }
}
