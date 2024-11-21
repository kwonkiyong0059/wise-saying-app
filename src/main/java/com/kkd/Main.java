package com.kkd;

public class Main {
    public static void main(String[] args) {
        QuoteRepository repository = new QuoteRepository();
        QuoteService service = new QuoteService(repository);
        QuoteController controller = new QuoteController(service);

        controller.run();
    }
}