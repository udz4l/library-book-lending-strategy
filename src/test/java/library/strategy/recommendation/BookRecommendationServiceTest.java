package library.strategy.recommendation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookRecommendationServiceTest {

    private final BookRecommendationService service =
            new BookRecommendationService();

    private final List<BookInfo> books = List.of(
            new BookInfo("b1", "Java Basics", "A. Teacher", 250, 15),
            new BookInfo("b2", "Clean Code", "R. Martin", 460, 80),
            new BookInfo("b3", "Refactoring", "M. Fowler", 380, 40)
    );

    private final RecommendationRequest request =
            new RecommendationRequest(books, "M. Fowler", true);

    @Test
    void recommendsTheMostPopularBook() {
        assertEquals(
                "b2",
                service.recommend(RecommendationMode.MOST_POPULAR, request).id()
        );
    }

    @Test
    void recommendsTheShortestBook() {
        assertEquals(
                "b1",
                service.recommend(RecommendationMode.SHORTEST_BOOK, request).id()
        );
    }

    @Test
    void recommendsABookByTheFavoriteAuthor() {
        assertEquals(
                "b3",
                service.recommend(RecommendationMode.SAME_AUTHOR, request).id()
        );
    }

    @ParameterizedTest
    @EnumSource(RecommendationMode.class)
    void everyModeReturnsABook(RecommendationMode mode) {
        assertTrue(books.contains(service.recommend(mode, request)));
    }
}