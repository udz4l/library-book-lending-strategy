package library.strategy.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookRecommendationStrategyTest {
    private RecommendationRequest request;

    @BeforeEach
    void setUp() {
        request = new RecommendationRequest(
                List.of(
                        new BookInfo("b1", "Java Basics", "A. Teacher", 250, 15),
                        new BookInfo("b2", "Clean Code", "R. Martin", 460, 80),
                        new BookInfo("b3", "Refactoring", "M. Fowler", 380, 40)
                ),
                "M. Fowler",
                true
        );
    }

    @Test
    void mostPopularStrategyReturnsBookWithHighestLoanCount() {
        BookInfo result = new MostPopularStrategy().recommend(request);

        assertEquals("b2", result.id());
    }

    @Test
    void shortestBookStrategyReturnsBookWithFewestPages() {
        BookInfo result = new ShortestBookStrategy().recommend(request);

        assertEquals("b1", result.id());
    }

    @Test
    void sameAuthorStrategyReturnsFavoriteAuthorsBook() {
        BookInfo result = new SameAuthorStrategy().recommend(request);

        assertEquals("b3", result.id());
    }

    @Test
    void selectorChoosesShortestBookWhenReaderPrefersShortRead() {
        BookRecommendationSelector selector = new BookRecommendationSelector();

        BookRecommendationStrategy result = selector.select(request);

        assertInstanceOf(ShortestBookStrategy.class, result);
    }

    @Test
    void selectorChoosesSameAuthorWhenShortReadIsNotPreferred() {
        RecommendationRequest authorRequest = new RecommendationRequest(
                request.books(),
                "M. Fowler",
                false
        );

        BookRecommendationStrategy result =
                new BookRecommendationSelector().select(authorRequest);

        assertInstanceOf(SameAuthorStrategy.class, result);
    }

    @Test
    void selectorFallsBackToMostPopularWhenNoFavoriteAuthorBookExists() {
        RecommendationRequest popularRequest = new RecommendationRequest(
                request.books(),
                "Unknown Author",
                false
        );

        BookRecommendationStrategy result =
                new BookRecommendationSelector().select(popularRequest);

        assertInstanceOf(MostPopularStrategy.class, result);
    }

    @Test
    void contextCanSwitchStrategyAtRuntime() {
        BookRecommendationContext context =
                new BookRecommendationContext(new MostPopularStrategy());

        assertEquals("b2", context.recommend(request).id());

        context.setStrategy(new ShortestBookStrategy());

        assertEquals("b1", context.recommend(request).id());
    }

    @Test
    void contextCanApplyStrategySelectedAutomatically() {
        BookRecommendationSelector selector = new BookRecommendationSelector();
        BookRecommendationContext context =
                new BookRecommendationContext(new MostPopularStrategy());

        context.setStrategy(selector.select(request));
        BookInfo result = context.recommend(request);

        assertEquals("b1", result.id());
        assertInstanceOf(ShortestBookStrategy.class, context.getStrategy());
    }
}