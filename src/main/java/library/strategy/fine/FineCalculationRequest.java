package library.strategy.fine;

import java.time.LocalDate;

public record FineCalculationRequest(
        LocalDate dueDate,
        LocalDate returnDate,
        double dailyRate) {
}