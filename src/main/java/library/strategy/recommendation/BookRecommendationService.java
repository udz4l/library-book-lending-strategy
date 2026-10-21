package library.strategy.recommendation;

public class BookRecommendationService {
    private final BookRecommendationSelector selector;

    public BookRecommendationService() {
        this(new BookRecommendationSelector());
    }

    public BookRecommendationService(BookRecommendationSelector selector) {
        this.selector = selector;
    }

    public BookInfo recommend(
            RecommendationMode mode,
            RecommendationRequest request) {
        BookRecommendationContext context =
                new BookRecommendationContext(selector.select(mode));

        return context.recommend(request);
    }
}