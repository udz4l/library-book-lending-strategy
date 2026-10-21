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
}