package myapp;

import javax.swing.*;
import java.awt.*;

/**
 * ConvertTool: convert simple integer from given base to decimal.
 */
public class ConvertTool extends Tool {
    private final DatabaseManager db;
    private final JComboBox<Integer> cbBase = new JComboBox<>(new Integer[]{2,8,10,16});
    private final JTextField tfInput = new JTextField();
    private final JButton btnConvert = new JButton("Convert");
    private final JTextArea taResult = new JTextArea(6, 40);
    private PlaceholderUpdater ph;

    public ConvertTool(DatabaseManager db) {
        super("convert", "Convert");
        this.db = db;
    }

    @Override
    public void buildUI() {
        panel.setLayout(new BorderLayout(6,6));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("From Base:"));
        top.add(cbBase);
        top.add(new JLabel("Input:"));
        tfInput.setColumns(25);
        top.add(tfInput);
        top.add(btnConvert);

        taResult.setEditable(false);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(taResult), BorderLayout.CENTER);

        ph = new PlaceholderUpdater(tfInput, () -> {
            int b = (Integer) cbBase.getSelectedItem();
            return switch (b) {
                case 2 -> "[ ใส่เลขฐาน 2 เช่น 101 ]";
                case 8 -> "[ ใส่เลขฐาน 8 เช่น 765 ]";
                case 10 -> "[ ใส่เลขฐาน 10 เช่น 123 ]";
                default -> "[ ใส่เลขฐาน 16 เช่น 2AF ]";
            };
        });

        cbBase.addActionListener(e -> ph.updatePlaceholder());
        btnConvert.addActionListener(e -> doConvert());
    }

    private void doConvert() {
        String raw = tfInput.getText().trim();
        if (raw.isEmpty() || raw.startsWith("[ ")) {
            JOptionPane.showMessageDialog(panel, "กรุณากรอกข้อมูล", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int base = (Integer) cbBase.getSelectedItem();
        try {
            int value = Integer.parseInt(raw.replaceAll("\\s+",""), base);
            String msg = String.format("Parsed (base %d) -> decimal: %d", base, value);
            taResult.setText(msg);
            db.insertHistory("convert", "base:" + base + " input:" + raw + " => " + value);
        } catch (NumberFormatException ex) {
            taResult.setText("ไม่สามารถแปลงได้: รูปแบบไม่ถูกต้องสำหรับฐาน " + base);
        }
    }
}
