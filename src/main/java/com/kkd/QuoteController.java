package com.kkd;

import java.util.Scanner;

public class QuoteController {
    private final QuoteService service;
    private final Scanner sc = new Scanner(System.in);

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            printMenu();
            System.out.print("명령: ");
            String command = sc.next();
            handleCommand(command);
        }
    }

    private void printMenu() {
        System.out.println("메뉴를 선택하세요.");
        System.out.println("1. 명언 등록 || 2. 번호 조회 || 3. 전체 조회 || 4. 명언 수정 || 5. 명언 삭제 || 6. 빌드 || 7. 종료");
    }

    private void handleCommand(String command) {
        try {
            switch (command) {
                case "1":
                    addQuote();
                    break;
                case "2":
                    viewQuote();
                    break;
                case "3":
                    System.out.println(service.getAllQuotes());
                    break;
                case "4":
                    updateQuote();
                    break;
                case "5":
                    deleteQuote();
                    break;
                case "6":
                    service.build();
                    break;
                case "7":
                    System.out.println("명언 앱을 종료합니다.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못된 명령입니다. 다시 입력해주세요.");
            }
        } catch (Exception e) {
            System.out.println("처리 중 오류가 발생했습니다: " + e.getMessage());
            sc.nextLine(); // 입력 버퍼 비우기
        }
    }

    private void addQuote() {
        sc.nextLine(); // 버퍼 비우기
        System.out.print("명언: ");
        String content = sc.nextLine();
        System.out.print("작가: ");
        String author = sc.nextLine();
        Quote quote = service.addQuote(content, author);
        System.out.println(quote.getId() + "번 명언이 등록되었습니다.");
    }

    private void viewQuote() {
        int id = getIdInput("조회할 명언 번호: ");
        System.out.println(service.getQuoteById(id));
    }

    private void updateQuote() {
        int id = getIdInput("수정할 명언 번호: ");
        sc.nextLine(); // 버퍼 비우기
        System.out.print("새로운 명언: ");
        String newContent = sc.nextLine();
        System.out.print("새로운 작가: ");
        String newAuthor = sc.nextLine();
        System.out.println(service.updateQuote(id, newContent, newAuthor));
    }

    private void deleteQuote() {
        int id = getIdInput("삭제할 명언 번호: ");
        System.out.println(service.deleteQuote(id));
    }

    private int getIdInput(String prompt) {
        System.out.print(prompt);
        return sc.nextInt();
    }
}
