package com.kkd;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteRepository {
    private static final String DB_PATH = "db/wiseSaying/";
    private static final String LAST_ID_FILE = DB_PATH + "lastId.txt";
    private final List<Quote> quotes = new ArrayList<>();
    private int lastId;

    public QuoteRepository() {
        try {
            Files.createDirectories(Paths.get(DB_PATH));
            if (!Files.exists(Paths.get(LAST_ID_FILE))) {
                Files.writeString(Paths.get(LAST_ID_FILE), "0", StandardOpenOption.CREATE);
                lastId = 0;
            } else {
                lastId = Integer.parseInt(Files.readString(Paths.get(LAST_ID_FILE)).trim());
            }
            loadQuotesFromFile();
        } catch (IOException e) {
            throw new RuntimeException("데이터베이스 초기화 실패: " + e.getMessage());
        }
    }

    public Quote save(String content, String author) {
        int nextId = getNextId();
        Quote quote = new Quote(nextId, content, author);
        quotes.add(quote);
        return quote;
    }

    public Optional<Quote> findById(int id) {
        return quotes.stream().filter(q -> q.getId() == id).findFirst();
    }

    public List<Quote> findAll() {
        return new ArrayList<>(quotes);
    }

    public boolean deleteById(int id) {
        return quotes.removeIf(q -> q.getId() == id);
    }

    private int getNextId() {
        return ++lastId;
    }

    public void saveToFile() {
        try {
            // 기존 명언 파일들 삭제
            Files.walk(Paths.get(DB_PATH))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);  // 기존 파일 삭제
                        } catch (IOException e) {
                            System.out.println("파일 삭제 실패: " + e.getMessage());
                        }
                    });

            // 메모리에서 데이터를 읽어 새로운 파일로 저장
            for (Quote quote : quotes) {
                String json = toJson(quote);
                Files.writeString(Paths.get(DB_PATH + quote.getId() + ".json"), json);
            }

            // 마지막 ID 저장
            Files.writeString(Paths.get(LAST_ID_FILE), String.valueOf(lastId));

        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    private void loadQuotesFromFile() {
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(Paths.get(DB_PATH), "*.json");
            for (Path file : files) {
                String json = Files.readString(file);
                Quote quote = fromJson(json);
                quotes.add(quote);
            }
        } catch (IOException e) {
            System.out.println("파일에서 명언을 로드하는 중 오류 발생: " + e.getMessage());
        }
    }

    private String toJson(Quote quote) {
        return "{\n" +
                "  \"id\": " + quote.getId() + ",\n" +
                "  \"content\": \"" + escapeJson(quote.getContent()) + "\",\n" +
                "  \"author\": \"" + escapeJson(quote.getAuthor()) + "\"\n" +
                "}";
    }

    private Quote fromJson(String json) {
        try {
            String[] lines = json.split("\n");
            int id = Integer.parseInt(lines[1].split(":")[1].trim().replace(",", ""));
            String content = lines[2].split(":")[1].trim().replace("\"", "").replace(",", "");
            String author = lines[3].split(":")[1].trim().replace("\"", "");
            return new Quote(id, content, author);
        } catch (Exception e) {
            System.out.println("JSON 파싱 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
