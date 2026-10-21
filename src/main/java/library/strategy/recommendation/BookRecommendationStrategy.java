package library.strategy.recommendation;

public interface BookRecommendationStrategy {
    BookInfo recommend(RecommendationRequest request);
}