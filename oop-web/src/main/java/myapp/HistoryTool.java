package myapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * HistoryTool: reads from DB and shows in a JTable.
 */
public class HistoryTool extends Tool {
    private final DatabaseManager db;
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Category","Payload","Created At"}, 0);
    private final JTable table = new JTable(model);

    public HistoryTool(DatabaseManager db) {
        super("history", "History");
        this.db = db;
    }

    @Override
    public void buildUI() {
        panel.setLayout(new BorderLayout(6,6));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        top.add(btnRefresh);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> reload());
    }

    @Override
    public void onShow() {
        reload();
    }

    private void reload() {
        model.setRowCount(0);
        for (String[] row : db.readHistory(200)) {
            model.addRow(row);
        }
    }
}
