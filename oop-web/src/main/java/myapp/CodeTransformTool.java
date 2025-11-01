package myapp;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * CodeTransformTool: placeholder-driven demo transforms.
 */
public class CodeTransformTool extends Tool {
    private final DatabaseManager db;
    private final JComboBox<String> cbMode;
    private final JTextField tfInput = new JTextField();
    private final JTextArea taResult = new JTextArea(6,40);
    private PlaceholderUpdater ph;

    private static final Map<String,String> MAP = Map.of(
        "B2G","Binary เช่น 1011",
        "G2B","Gray เช่น 1110",
        "BCD2DEC","กลุ่ม BCD เช่น 0011 0101",
        "DEC2BCD","เลขฐาน 10 เช่น 35",
        "ASC2BIN","ข้อความ ASCII เช่น HELLO",
        "BIN2ASC","Binary 8-bit เว้นวรรค เช่น 01001000 01001001"
    );

    public CodeTransformTool(DatabaseManager db) {
        super("codetrans", "Code Transform");
        this.db = db;
        cbMode = new JComboBox<>(MAP.keySet().toArray(new String[0]));
    }

    @Override
    public void buildUI() {
        panel.setLayout(new BorderLayout(6,6));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Mode:"));
        top.add(cbMode);
        top.add(new JLabel("Input:"));
        tfInput.setColumns(30);
        JButton btn = new JButton("Transform");
        top.add(tfInput);
        top.add(btn);

        taResult.setEditable(false);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(taResult), BorderLayout.CENTER);

        ph = new PlaceholderUpdater(tfInput, () -> "[ " + MAP.get(cbMode.getSelectedItem()) + " ]");
        cbMode.addActionListener(e -> ph.updatePlaceholder());
        ph.updatePlaceholder();

        btn.addActionListener(e -> doTransform());
    }

    private void doTransform() {
        String mode = (String) cbMode.getSelectedItem();
        String input = tfInput.getText().trim();
        if (input.isEmpty() || input.startsWith("[ ")) {
            JOptionPane.showMessageDialog(panel, "กรุณากรอกข้อมูล", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Demo: implement BASIC BIN2ASC and ASC2BIN for usefulness
        String out;
        try {
            switch (mode) {
                case "BIN2ASC":
                    out = binToAscii(input);
                    break;
                case "ASC2BIN":
                    out = asciiToBin(input);
                    break;
                default:
                    out = "Mode=" + mode + "\nInput=" + input + "\n(Result: Demo transform)";
            }
        } catch (Exception ex) {
            out = "Error during transform: " + ex.getMessage();
        }
        taResult.setText(out);
        db.insertHistory("codetrans", "mode:" + mode + " input:" + input);
    }

    private String asciiToBin(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0')).append(' ');
        }
        return sb.toString().trim();
    }

    private String binToAscii(String bin) {
        String[] parts = bin.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            int val = Integer.parseInt(p, 2);
            sb.append((char) val);
        }
        return sb.toString();
    }
}
