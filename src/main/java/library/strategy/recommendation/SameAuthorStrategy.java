package library.strategy.recommendation;

public class SameAuthorStrategy implements BookRecommendationStrategy {
    @Override
    public BookInfo recommend(RecommendationRequest request) {
        return request.books().stream()
                .filter(book -> book.author().equalsIgnoreCase(request.favoriteAuthor()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No book by the favorite author was found."));
    }
}