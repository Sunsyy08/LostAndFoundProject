package dao;

import database.DBConnection;
import model.LostItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LostItemDAO {

    public LostItemDAO() {
        createTableIfNotExists();
        addMissingColumns();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS lost_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item_name TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "found_date TEXT," +
                "status TEXT DEFAULT '보관 중'," +
                "description TEXT," +      // 상세 설명
                "student_id TEXT" +        // 학번 (문자열로 저장, 5자리 숫자 검증은 애플리케이션에서)
                ")";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("테이블 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 기존 DB에 새 컬럼이 없을 경우를 위해 ALTER TABLE로 컬럼 추가 시도.
     * SQLite는 이미 컬럼이 있으면 에러를 던지므로 예외를 무시한다.
     */
    private void addMissingColumns() {
        String[] alterStmts = new String[] {
                "ALTER TABLE lost_items ADD COLUMN description TEXT",
                "ALTER TABLE lost_items ADD COLUMN student_id TEXT"
        };

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : alterStmts) {
                try {
                    stmt.execute(sql);
                } catch (SQLException ignored) {
                }
            }
        } catch (SQLException e) {
            System.out.println("컬럼 추가 시도 중 오류: " + e.getMessage());
        }
    }

    public void addLostItem(LostItem item) {
        // 학번 유효성 검사: null 허용? (학교 앱이라면 필수로 요구하므로 체크)
        String studentId = item.getStudentId();
        if (studentId != null && !studentId.isEmpty()) {
            if (!studentId.matches("\\d{5}")) { // 5자리 숫자 검사
                System.out.println("등록 실패: 학번은 5자리 숫자여야 합니다. 입력값: " + studentId);
                return;
            }
        } else {
            // 학번을 필수로 하고 싶다면 아래를 주석 해제
            // System.out.println("등록 실패: 학번을 입력하세요.");
            // return;
        }

        String sql = "INSERT INTO lost_items(item_name, location, found_date, status, description, student_id) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getLocation());
            pstmt.setString(3, item.getFoundDate());
            pstmt.setString(4, item.getStatus());
            pstmt.setString(5, item.getDescription());
            pstmt.setString(6, item.getStudentId());
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
                if (i < orderByList.size() - 1) sql += ", ";
            }
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // 컬럼이 새로 추가되어도 여기에 포함되어 있으므로 안전하게 읽음
                items.add(new LostItem(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("location"),
                        rs.getString("found_date"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("student_id")
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
