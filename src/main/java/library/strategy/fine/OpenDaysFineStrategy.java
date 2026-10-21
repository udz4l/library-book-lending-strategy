package library.strategy.fine;

import java.time.LocalDate;

public class OpenDaysFineStrategy implements FineCalculationStrategy {
    private final LibraryCalendar calendar;

    public OpenDaysFineStrategy(LibraryCalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public double calculate(FineCalculationRequest request) {
        double totalFine = 0;
        LocalDate day = request.dueDate().plusDays(1);

        while (!day.isAfter(request.returnDate())) {
            if (calendar.isOpen(day)) {
                totalFine += request.dailyRate();
            }
            day = day.plusDays(1);
        }

        return totalFine;
    }
}