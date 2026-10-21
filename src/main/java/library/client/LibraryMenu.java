package library.client;

import library.strategy.fine.DailyFineStrategy;
import library.strategy.fine.FineCalculationContext;
import library.strategy.fine.GracePeriodFineStrategy;
import library.strategy.fine.OpenDaysFineStrategy;
import library.strategy.fine.WeekdayLibraryCalendar;
import library.strategy.recommendation.BookRecommendationContext;
import library.strategy.recommendation.MostPopularStrategy;
import library.strategy.recommendation.ShortestBookStrategy;
import library.facade.LendingFacade;
import library.facade.LibraryAdminFacade;
import library.strategy.fine.FineCalculationRequest;
import library.strategy.fine.FineCalculationSelector;
import library.strategy.recommendation.BookInfo;
import library.strategy.recommendation.RecommendationRequest;
import library.strategy.recommendation.BookRecommendationSelector;
import library.strategy.recommendation.BookRecommendationService;
import library.strategy.recommendation.RecommendationMode;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LibraryMenu {
    private final LendingFacade lendingFacade;
    private final LibraryAdminFacade adminFacade;
    private final BookRecommendationService recommendationService;
    private final BookRecommendationSelector recommendationSelector;
    private final FineCalculationSelector fineSelector;
    private final Scanner scanner = new Scanner(System.in);

    public LibraryMenu(
            LendingFacade lendingFacade,
            LibraryAdminFacade adminFacade,
            BookRecommendationService recommendationService,
            BookRecommendationSelector recommendationSelector,
            FineCalculationSelector fineSelector) {
        this.lendingFacade = lendingFacade;
        this.adminFacade = adminFacade;
        this.recommendationService = recommendationService;
        this.recommendationSelector = recommendationSelector;
        this.fineSelector = fineSelector;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("""
                    
                    === Library Book Lending ===
                    1. Complete a normal loan
                    2. Demo B failure (invalid library card)
                    3. Demo C failure (confirmation fails)
                    4. Show saved data
                    5. Recover unfinished workflows
                    6. Demo Strategy pattern
                    0. Exit
                    """);

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createLoan(true, false);
                    case "2" -> createLoan(false, false);
                    case "3" -> createLoan(true, true);
                    case "4" -> System.out.println(adminFacade.showStoredState());
                    case "5" -> {
                        adminFacade.recoverAfterRestart();
                        System.out.println("Recovery finished.");
                    }
                    case "6" -> demonstrateStrategies();
                    case "0" -> running = false;
                    default -> System.out.println("Please choose a listed option.");
                }
            } catch (RuntimeException e) {
                System.out.println("The request failed: " + e.getMessage());
            }
        }

        System.out.println("Goodbye.");
    }

    private void createLoan(
            boolean cardValid,
            boolean simulateConfirmationFailure) {
        System.out.println("Enter book ID:");
        String bookId = scanner.nextLine().trim();

        System.out.println("Enter reader ID:");
        String readerId = scanner.nextLine().trim();

        var workflow = lendingFacade.lendBook(
                bookId,
                readerId,
                cardValid,
                simulateConfirmationFailure
        );

        System.out.println("Loan completed. Workflow ID: " + workflow.getId());
    }

    private void demonstrateStrategies() {
        List<BookInfo> books = List.of(
                new BookInfo("b1", "Java Basics", "A. Teacher", 250, 15),
                new BookInfo("b2", "Clean Code", "R. Martin", 460, 80),
                new BookInfo("b3", "Refactoring", "M. Fowler", 380, 40)
        );

        var recommendationRequest = new RecommendationRequest(
                books,
                "M. Fowler",
                true
        );

        System.out.println("\nBook recommendation strategies, same request:");
        for (RecommendationMode mode : RecommendationMode.values()) {
            BookInfo result =
                    recommendationService.recommend(mode, recommendationRequest);
            System.out.println(mode + ": " + result.title());
        }

        BookRecommendationContext recommendationContext =
                new BookRecommendationContext(new MostPopularStrategy());

        System.out.println("Before runtime switch: "
                + recommendationContext.recommend(recommendationRequest).title());

        recommendationContext.setStrategy(new ShortestBookStrategy());

        System.out.println("After runtime switch: "
                + recommendationContext.recommend(recommendationRequest).title());

        recommendationContext.setStrategy(
                recommendationSelector.select(recommendationRequest)
        );

        System.out.println("Automatic strategy: "
                + recommendationContext.getStrategy().getClass().getSimpleName());
        System.out.println("Automatic result: "
                + recommendationContext.recommend(recommendationRequest).title());

        var fineRequest = new FineCalculationRequest(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 20),
                100.0
        );

        FineCalculationContext fineContext =
                new FineCalculationContext(new DailyFineStrategy());

        System.out.println("\nFine calculation strategies, same request:");
        System.out.println("Daily fine: " + fineContext.calculate(fineRequest));

        fineContext.setStrategy(new GracePeriodFineStrategy());
        System.out.println("Grace period fine: " + fineContext.calculate(fineRequest));

        fineContext.setStrategy(
                new OpenDaysFineStrategy(new WeekdayLibraryCalendar())
        );
        System.out.println("Open days fine: " + fineContext.calculate(fineRequest));

        fineContext.setStrategy(fineSelector.select(fineRequest));
        System.out.println("Automatic strategy: "
                + fineContext.getStrategy().getClass().getSimpleName());
        System.out.println("Automatic fine: " + fineContext.calculate(fineRequest));
    }
}