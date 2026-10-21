package library.strategy.recommendation;

public record BookInfo(
        String id,
        String title,
        String author,
        int pageCount,
        int loanCount) {

}
