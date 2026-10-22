package cz.easyreceipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
//trida Receipt
public class Receipt {
    private final String receiptNumber;
    private final LocalDateTime dateTime;
    private final List<Product> products;

    public Receipt(String receiptNumber, LocalDateTime dateTime, List<Product> products) {
        if (receiptNumber == null || receiptNumber.isBlank()) {
            throw new IllegalArgumentException("Cislo uctenky nesmi byt prazdne.");
        }
        if (dateTime == null) throw new IllegalArgumentException("Datum nesmi byt null.");
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Uctenka musi obsahovat alespon jeden produkt.");
        }
        this.receiptNumber = receiptNumber;
        this.dateTime = dateTime;
        this.products = List.copyOf(products);
    }

    public String getReceiptNumber() { return receiptNumber; }
    public LocalDateTime getDateTime() { return dateTime; }
    public List<Product> getProducts() { return products; }

    public BigDecimal getTotal() {
        return products.stream()
                .map(Product::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
