package library.strategy.fine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FineCalculationStrategyTest {
    private FineCalculationRequest request;

    @BeforeEach
    void setUp() {
        request = new FineCalculationRequest(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 20),
                100.0
        );
    }

    @Test
    void dailyFineChargesEveryOverdueDay() {
        assertEquals(800.0, new DailyFineStrategy().calculate(request));
    }

    @Test
    void gracePeriodFineDoesNotChargeFirstThreeOverdueDays() {
        assertEquals(500.0, new GracePeriodFineStrategy().calculate(request));
    }

    @Test
    void openDaysFineUsesInjectedCalendar() {
        LibraryCalendar fixedCalendar = date ->
                !date.equals(LocalDate.of(2026, 10, 17))
                        && !date.equals(LocalDate.of(2026, 10, 18));

        assertEquals(
                600.0,
                new OpenDaysFineStrategy(fixedCalendar).calculate(request)
        );
    }

    @Test
    void selectorChoosesDailyFineWhenThereIsNoDelay() {
        FineCalculationRequest onTimeRequest = new FineCalculationRequest(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 12),
                100.0
        );

        FineCalculationStrategy result =
                new FineCalculationSelector().select(onTimeRequest);

        assertInstanceOf(DailyFineStrategy.class, result);
    }

    @Test
    void selectorChoosesGracePeriodForShortDelay() {
        FineCalculationRequest shortDelayRequest = new FineCalculationRequest(
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 14),
                100.0
        );

        FineCalculationStrategy result =
                new FineCalculationSelector().select(shortDelayRequest);

        assertInstanceOf(GracePeriodFineStrategy.class, result);
    }

    @Test
    void selectorChoosesOpenDaysForLongDelay() {
        FineCalculationStrategy result =
                new FineCalculationSelector().select(request);

        assertInstanceOf(OpenDaysFineStrategy.class, result);
    }

    @ParameterizedTest
    @MethodSource("fineCases")
    void eachStrategyCalculatesExpectedAmount(
            FineCalculationStrategy strategy,
            double expectedAmount) {
        assertEquals(expectedAmount, strategy.calculate(request));
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> fineCases() {
        LibraryCalendar weekdayCalendar = date ->
                date.getDayOfWeek() != java.time.DayOfWeek.SATURDAY
                        && date.getDayOfWeek() != java.time.DayOfWeek.SUNDAY;

        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(
                        new DailyFineStrategy(),
                        800.0
                ),
                org.junit.jupiter.params.provider.Arguments.of(
                        new GracePeriodFineStrategy(),
                        500.0
                ),
                org.junit.jupiter.params.provider.Arguments.of(
                        new OpenDaysFineStrategy(weekdayCalendar),
                        600.0
                )
        );
    }
}