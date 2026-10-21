package library.strategy.fine;

import java.time.LocalDate;

public interface LibraryCalendar {
    boolean isOpen(LocalDate date);
}