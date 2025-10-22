package model;

public class LostItem {
    private int id;
    private String itemName;
    private String location;
    private String foundDate;
    private String status;

    public LostItem(int id, String itemName, String location, String foundDate, String status) {
        this.id = id;
        this.itemName = itemName;
        this.location = location;
        this.foundDate = foundDate;
        this.status = status;
    }

    public LostItem(String itemName, String location, String foundDate, String status) {
        this(0, itemName, location, foundDate, status);
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public String getLocation() { return location; }
    public String getFoundDate() { return foundDate; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "[" + id + "] " + itemName + " / " + location + " / " + foundDate + " / 상태: " + status;
    }
}
