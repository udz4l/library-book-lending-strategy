package library.strategy.recommendation;

public class BookRecommendationService {
    public BookInfo recommend(
            RecommendationMode mode,
            RecommendationRequest request) {

        BookRecommendationStrategy strategy = switch (mode) {
            case MOST_POPULAR -> new MostPopularStrategy();
            case SHORTEST_BOOK -> new ShortestBookStrategy();
            case SAME_AUTHOR -> new SameAuthorStrategy();
        };

        return new BookRecommendationContext(strategy).recommend(request);
    }
}