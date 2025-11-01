package myapp;

import java.sql.*;

/**
 * Encapsulates SQLite access via JDBC.
 */
public class DatabaseManager {
    private final String dbFile;
    private Connection conn;

    public DatabaseManager(String dbFile) {
        this.dbFile = dbFile;
        connect();
    }

    private void connect() {
        try {
            String url = "jdbc:sqlite:" + dbFile;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite: " + dbFile);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setupTables() {
        String sql =
    "CREATE TABLE IF NOT EXISTS history (" +
    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
    "category TEXT NOT NULL," +
    "payload TEXT," +
    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
    ");";

        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertHistory(String category, String payload) {
        String sql = "INSERT INTO history (category, payload) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, payload);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.util.List<String[]> readHistory(int limit) {
        java.util.List<String[]> out = new java.util.ArrayList<>();
        String sql = "SELECT id, category, payload, created_at FROM history ORDER BY id DESC LIMIT ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new String[]{
                        Integer.toString(rs.getInt("id")),
                        rs.getString("category"),
                        rs.getString("payload"),
                        rs.getString("created_at")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }
}
