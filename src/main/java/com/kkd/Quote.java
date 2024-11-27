package com.kkd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quote {
    private final int id;
    private String content;
    private String author;

    public Quote(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    @Override
    public String toString() {
        return "번호: " + id + "\n명언: " + content + "\n작가: " + author;
    }

}
