package library.strategy.recommendation;

public class MostPopularStrategy implements BookRecommendationStrategy {
    @Override
    public BookInfo recommend(RecommendationRequest request) {
        return request.books().stream()
                .max((first, second) ->
                        Integer.compare(first.loanCount(), second.loanCount()))
                .orElseThrow(() -> new IllegalArgumentException("Book catalog is empty."));
    }
}