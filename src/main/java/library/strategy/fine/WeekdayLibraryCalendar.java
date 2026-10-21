package library.strategy.fine;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekdayLibraryCalendar implements LibraryCalendar {
    @Override
    public boolean isOpen(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }
}