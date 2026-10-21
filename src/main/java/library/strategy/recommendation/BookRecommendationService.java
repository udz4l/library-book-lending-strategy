package library.strategy.recommendation;

import java.util.Comparator;

public class BookRecommendationService {

    public BookInfo recommend(
            RecommendationMode mode,
            RecommendationRequest request) {

        if (request.books().isEmpty()) {
            throw new IllegalArgumentException("Book list must not be empty.");
        }

        return switch (mode) {
            case MOST_POPULAR -> request.books().stream()
                    .max(Comparator.comparingInt(BookInfo::loanCount))
                    .orElseThrow();

            case SHORTEST_BOOK -> request.books().stream()
                    .min(Comparator.comparingInt(BookInfo::pageCount))
                    .orElseThrow();

            case SAME_AUTHOR -> request.books().stream()
                    .filter(book -> book.author()
                            .equalsIgnoreCase(request.favoriteAuthor()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No book by the favorite author was found."));
        };
    }
}