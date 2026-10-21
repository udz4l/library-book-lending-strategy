package library.strategy.fine;

import java.time.temporal.ChronoUnit;

public class DailyFineStrategy implements FineCalculationStrategy {
    @Override
    public double calculate(FineCalculationRequest request) {
        long overdueDays = Math.max(
                0,
                ChronoUnit.DAYS.between(request.dueDate(), request.returnDate())
        );
        return overdueDays * request.dailyRate();
    }
}