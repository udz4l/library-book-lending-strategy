package library.strategy.recommendation;

public class BookRecommendationSelector {
    public BookRecommendationStrategy select(RecommendationRequest request) {
        if (request.prefersShortRead()) {
            return new ShortestBookStrategy();
        }



        if (request.favoriteAuthor() != null
                && request.books().stream()
                .anyMatch(book -> book.author().equalsIgnoreCase(request.favoriteAuthor()))) {
            return new SameAuthorStrategy();
        }

        return new MostPopularStrategy();
    }

    public BookRecommendationStrategy select(RecommendationMode mode) {
        return switch (mode) {
            case MOST_POPULAR -> new MostPopularStrategy();
            case SHORTEST_BOOK -> new ShortestBookStrategy();
            case SAME_AUTHOR -> new SameAuthorStrategy();
        };
    }
}