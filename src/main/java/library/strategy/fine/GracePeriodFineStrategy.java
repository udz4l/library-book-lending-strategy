package library.strategy.fine;

import java.time.temporal.ChronoUnit;

public class GracePeriodFineStrategy implements FineCalculationStrategy {
    private static final long GRACE_DAYS = 3;

    @Override
    public double calculate(FineCalculationRequest request) {
        long overdueDays = Math.max(
                0,
                ChronoUnit.DAYS.between(request.dueDate(), request.returnDate())
        );
        long chargeableDays = Math.max(0, overdueDays - GRACE_DAYS);
        return chargeableDays * request.dailyRate();
    }
}