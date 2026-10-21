package library.strategy.recommendation;

public class ShortestBookStrategy implements BookRecommendationStrategy {
    @Override
    public BookInfo recommend(RecommendationRequest request) {
        return request.books().stream()
                .min((first, second) ->
                        Integer.compare(first.pageCount(), second.pageCount()))
                .orElseThrow(() -> new IllegalArgumentException("Book catalog is empty."));
    }
}