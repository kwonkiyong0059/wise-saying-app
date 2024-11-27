package com.kkd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Quote {
    private final int id;
    private String content;
    private String author;

    @Override
    public String toString() {
        return "번호: " + id + "\n명언: " + content + "\n작가: " + author;
    }

}
