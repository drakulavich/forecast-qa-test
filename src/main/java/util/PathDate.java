package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PathDate {

    private final LocalDate date;

    public PathDate(LocalDate date) {
        this.date = date;
    }

    @Override public String toString() {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}