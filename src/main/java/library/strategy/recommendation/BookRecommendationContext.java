package library.strategy.recommendation;

public class BookRecommendationContext {
    private BookRecommendationStrategy strategy;

    public BookRecommendationContext(BookRecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(BookRecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public BookRecommendationStrategy getStrategy() {
        return strategy;
    }

    public BookInfo recommend(RecommendationRequest request) {
        return strategy.recommend(request);
    }

    public BookInfo recommendAutomatically(
            RecommendationRequest request,
            BookRecommendationSelector selector) {
        setStrategy(selector.select(request));
        return recommend(request);
    }
}