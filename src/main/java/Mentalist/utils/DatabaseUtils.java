package Mentalist.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseUtils {
    private static Connection connection = null;

    public static final String USER_DB = "users";

    private static String dbUrl;

    private static String dbSchema;

    private static String dbUser;

    private static String dbPass;

    private static boolean configured = false;

    public static void connect() {
        if (!configured)
            throw new NullPointerException("Database was not configured yet.  Try calling setDBCredentials first.");
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl + dbUrl, dbUser, dbPass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet query(String sql, Object... params) {
        connect();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 1; i <= params.length; i++)
                stmt.setObject(i, params[i - 1]);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void update(String sql, Object... params) {
        connect();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 1; i <= params.length; i++)
                stmt.setObject(i, params[i - 1]);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int insert(String table, Map<String, String> column_value_map) throws SQLException {
        connect();
        PreparedStatement stmt = null;
        String insertLine = "INSERT INTO " + table + "(";
        String valueLine = " VALUES (";
        if (column_value_map != null) {
            for (Map.Entry<String, String> entry : column_value_map.entrySet()) {
                insertLine = insertLine + " ?,";
                valueLine = valueLine + " ?,";
            }
            insertLine = insertLine.substring(0, insertLine.length() - 1);
            valueLine = valueLine.substring(0, valueLine.length() - 1);
            stmt = connection.prepareStatement(insertLine + ")" + insertLine + ");", 1);
            int i = 1;
            for (Map.Entry<String, String> entry : column_value_map.entrySet()) {
                stmt.setString(i, entry.getValue());
                i++;
            }
        }
        if (stmt == null)
            return -1;
        try {
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Deprecated
    public static int createUser() {
        try {
            return insert("USER_DB", null);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setDBCredentials(String dbUrl, String dbSchema, String dbUser, String dbPass) {
        DatabaseUtils.dbUrl = dbUrl;
        DatabaseUtils.dbSchema = dbSchema;
        DatabaseUtils.dbUser = dbUser;
        DatabaseUtils.dbPass = dbPass;
        configured = true;
    }
}