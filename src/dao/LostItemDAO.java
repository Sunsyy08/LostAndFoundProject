package dao;

import database.DBConnection;
import model.LostItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LostItemDAO {

    public LostItemDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS lost_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item_name TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "found_date TEXT," +
                "status TEXT DEFAULT '보관 중'" +
                ")";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("테이블 생성 실패: " + e.getMessage());
        }
    }

    public void addLostItem(LostItem item) {
        String sql = "INSERT INTO lost_items(item_name, location, found_date, status) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getLocation());
            pstmt.setString(3, item.getFoundDate());
            pstmt.setString(4, item.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("등록 실패: " + e.getMessage());
        }
    }

    public List<LostItem> getAllItems(List<String> orderByList) {
        List<LostItem> items = new ArrayList<>();
        String sql = "SELECT * FROM lost_items";

        if (orderByList != null && !orderByList.isEmpty()) {
            sql += " ORDER BY ";
            for (int i = 0; i < orderByList.size(); i++) {
                String orderBy = orderByList.get(i);
                switch (orderBy.toLowerCase()) {
                    case "date" -> sql += "found_date DESC";
                    case "name" -> sql += "item_name ASC";
                    case "location" -> sql += "location ASC";
                    case "status" -> sql += "status ASC";
                }
                if (i < orderByList.size() - 1) sql += ", "; // 여러 정렬 기준 연결
            }
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new LostItem(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getString("found_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("조회 실패: " + e.getMessage());
        }
        return items;
    }

    public void updateStatus(int id, String newStatus) {
        String sql = "UPDATE lost_items SET status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("상태 수정 실패: " + e.getMessage());
        }
    }

    public void deleteItem(int id) {
        String sql = "DELETE FROM lost_items WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("삭제 실패: " + e.getMessage());
        }
    }
}
