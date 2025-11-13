package ui;

import dao.LostItemDAO;
import model.LostItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LostItemApp {
    private final LostItemDAO dao = new LostItemDAO();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n=== 분실물 관리 시스템 ===");
            System.out.println("1. 분실물 등록");
            System.out.println("2. 분실물 목록 보기");
            System.out.println("3. 상태 변경 (보관 중 → 찾아감)");
            System.out.println("4. 분실물 삭제 (관리자)");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ignored) {}

            switch (choice) {
                case 1 -> addItem();
                case 2 -> showItems();
                case 3 -> updateStatus();
                case 4 -> deleteItem();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void addItem() {
        System.out.print("물건 이름: ");
        String name = sc.nextLine().trim();
        System.out.print("분실 장소: ");
        String loc = sc.nextLine().trim();
        System.out.print("분실 날짜(YYYY-MM-DD): ");
        String date = sc.nextLine().trim();
        System.out.print("학번(5자리, 예: 20607) - 없으면 Enter: ");
        String studentId = sc.nextLine().trim();
        System.out.print("상세 설명 (예: 검은색 지갑, 안에 학생증 포함): ");
        String description = sc.nextLine().trim();

        LostItem item = new LostItem(name, loc, date, "보관 중", description, studentId);
        dao.addLostItem(item);
        System.out.println("✅ 등록 완료!");
    }

    private void showItems() {
        System.out.println("정렬 기준을 선택하세요 (여러 개 가능, 쉼표로 구분)");
        System.out.println("1. 날짜순  2. 이름순  3. 장소순  4. 상태순  (예: 1,3)");
        System.out.print("선택: ");
        String input = sc.nextLine();

        String[] choices = input.split(",");
        List<String> orderByList = new ArrayList<>();

        for (String choice : choices) {
            switch (choice.trim()) {
                case "1" -> orderByList.add("date");
                case "2" -> orderByList.add("name");
                case "3" -> orderByList.add("location");
                case "4" -> orderByList.add("status");
            }
        }

        List<LostItem> items = dao.getAllItems(orderByList);
        if (items.isEmpty()) {
            System.out.println("등록된 분실물이 없습니다.");
        } else {
            System.out.println("\n=== 정렬 결과 ===");
            items.forEach(System.out::println);
        }
    }

    private void updateStatus() {
        System.out.print("상태를 변경할 ID: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        dao.updateStatus(id, "찾아감");
        System.out.println("✅ 상태가 '찾아감'으로 변경되었습니다.");
    }

    private void deleteItem() {
        System.out.print("삭제할 ID: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        dao.deleteItem(id);
        System.out.println("✅ 삭제 완료!");
    }
}
