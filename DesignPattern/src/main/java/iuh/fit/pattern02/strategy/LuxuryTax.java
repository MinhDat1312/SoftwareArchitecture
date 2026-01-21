package iuh.fit.pattern02.strategy;

public class LuxuryTax implements TaxStrategy {
    @Override
    public double calculateTax(double amount) {
        return amount * 0.3;
    }
}
