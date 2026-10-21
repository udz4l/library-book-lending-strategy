package library.strategy.fine;

public class FineCalculationContext {
    private FineCalculationStrategy strategy;

    public FineCalculationContext(FineCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(FineCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public FineCalculationStrategy getStrategy() {
        return strategy;
    }

    public double calculate(FineCalculationRequest request) {
        return strategy.calculate(request);
    }
}