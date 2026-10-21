package library.strategy.recommendation;

import java.util.List;

public record RecommendationRequest(
        List<BookInfo> books,
        String favoriteAuthor,
        boolean prefersShortRead) {

    public RecommendationRequest {
        books = List.copyOf(books);
    }
}