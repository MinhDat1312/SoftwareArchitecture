package iuh.fit.pattern02.strategy;

public class VATTax implements TaxStrategy {
    @Override
    public double calculateTax(double amount) {
        return amount * 0.1;
    }
}
