package model;

public class LostItem {
    private int id;
    private String itemName;
    private String location;
    private String foundDate;
    private String status;
    private String description; // 상세 설명
    private String studentId;   // 학번 (5자리)

    public LostItem(int id, String itemName, String location, String foundDate, String status, String description, String studentId) {
        this.id = id;
        this.itemName = itemName;
        this.location = location;
        this.foundDate = foundDate;
        this.status = status;
        this.description = description;
        this.studentId = studentId;
    }

    public LostItem(String itemName, String location, String foundDate, String status, String description, String studentId) {
        this(0, itemName, location, foundDate, status, description, studentId);
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public String getLocation() { return location; }
    public String getFoundDate() { return foundDate; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getStudentId() { return studentId; }

    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    @Override
    public String toString() {
        return "[" + id + "] " + itemName
                + " / " + location
                + " / " + foundDate
                + " / 상태: " + status
                + (studentId != null && !studentId.isEmpty() ? " / 학번: " + studentId : "")
                + (description != null && !description.isEmpty() ? " / 상세: " + description : "");
    }
}
