package com.kkd;

import java.util.List;
import java.util.Optional;

public class QuoteService {
    private final QuoteRepository repository;

    public QuoteService(QuoteRepository repository) {
        this.repository = repository;
    }

    public Quote addQuote(String content, String author) {
        return repository.save(content, author);
    }

    public String getQuoteById(int id) {
        Optional<Quote> quote = repository.findById(id);
        return quote.map(Quote::toString).orElse("해당 번호의 명언이 없습니다.");
    }

    public String getAllQuotes() {
        List<Quote> quotes = repository.findAll();
        if (quotes.isEmpty()) {
            return "등록된 명언이 없습니다.";
        }

        StringBuilder sb = new StringBuilder();
        for (Quote quote : quotes) {
            sb.append(quote.toString()).append("\n----------------------\n");
        }
        return sb.toString();
    }

    public String updateQuote(int id, String newContent, String newAuthor) {
        Optional<Quote> quote = repository.findById(id);
        if (quote.isPresent()) {
            quote.get().setContent(newContent);
            quote.get().setAuthor(newAuthor);
            return id + "번 명언이 수정되었습니다.";
        }
        return "해당 번호의 명언이 없습니다.";
    }

    public String deleteQuote(int id) {
        boolean deleted = repository.deleteById(id);
        return deleted ? id + "번 명언이 삭제되었습니다." : "해당 번호의 명언이 없습니다.";
    }

    public void build() {
        repository.saveToFile();  // 빌드 시에 파일에 반영
        System.out.println("데이터가 파일에 저장되었습니다.");
    }
}
