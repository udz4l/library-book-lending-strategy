package library.strategy.fine;

public class FineCalculationSelector {
    public FineCalculationStrategy select(FineCalculationRequest request) {
        long overdueDays = Math.max(
                0,
                java.time.temporal.ChronoUnit.DAYS.between(
                        request.dueDate(),
                        request.returnDate()
                )
        );

        if (overdueDays == 0) {
            return new DailyFineStrategy();
        }

        if (overdueDays <= 3) {
            return new GracePeriodFineStrategy();
        }

        return new OpenDaysFineStrategy(new WeekdayLibraryCalendar());
    }
}