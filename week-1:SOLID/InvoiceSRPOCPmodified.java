import java.util.*;

class LineItem {
    String sku;
    int quantity;
    double unitPrice;

    LineItem(String sku, int quantity, double unitPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    double total() {
        return unitPrice * quantity;
    }
}

interface Pricing {
    double calculateSubtotal(List<LineItem> items);
}

class PricingService implements Pricing {
    public double calculateSubtotal(List<LineItem> items) {
        double subtotal = 0.0;
        for (LineItem it : items) subtotal += it.total();
        return subtotal;
    }
}

interface Discount {
    double apply(double subtotal);
}

class PercentOffDiscount implements Discount {
    private final double percent;
    PercentOffDiscount(double percent) { this.percent = percent; }
    public double apply(double subtotal) { return subtotal * (percent / 100.0); }
}

class FlatOffDiscount implements Discount {
    private final double amount;
    FlatOffDiscount(double amount) { this.amount = amount; }
    public double apply(double subtotal) { return amount; }
}

interface Tax {
    double calculateTax(double taxableAmount);
}

class TaxService implements Tax {
    private final double taxRate;
    TaxService(double taxRate) { this.taxRate = taxRate; }
    public double calculateTax(double taxableAmount) {
        return taxableAmount * taxRate;
    }
}

interface Renderer {
    String render(List<LineItem> items, double subtotal, double discountTotal, double tax, double grand);
}

class InvoiceRenderer implements Renderer {
    public String render(List<LineItem> items, double subtotal, double discountTotal, double tax, double grand) {
        StringBuilder sb = new StringBuilder();
        sb.append("INVOICE\n");
        for (LineItem it : items) {
            sb.append(it.sku).append(" x").append(it.quantity)
              .append(" @ ").append(it.unitPrice).append("\n");
        }
        sb.append("Subtotal: ").append(subtotal).append("\n")
          .append("Discounts: ").append(discountTotal).append("\n")
          .append("Tax: ").append(tax).append("\n")
          .append("Total: ").append(grand).append("\n");
        return sb.toString();
    }
}

interface Emailer {
    void send(String email);
}

class EmailService implements Emailer {
    public void send(String email) {
        if (email != null && !email.isEmpty()) {
            System.out.println("[SMTP] Sending invoice to " + email + "...");
        }
    }
}

interface Logger {
    void log(String email, double grand);
}

class LoggerService implements Logger {
    public void log(String email, double grand) {
        System.out.println("[LOG] Invoice processed for " + email + " total=" + grand);
    }
}

class InvoiceService {
    private final Pricing pricing;
    private final Tax taxService;
    private final Renderer renderer;
    private final Emailer emailer;
    private final Logger logger;

    InvoiceService(Pricing pricing, Tax taxService, Renderer renderer, Emailer emailer, Logger logger) {
        this.pricing = pricing;
        this.taxService = taxService;
        this.renderer = renderer;
        this.emailer = emailer;
        this.logger = logger;
    }

    String process(List<LineItem> items, List<Discount> discounts, String email) {
        double subtotal = pricing.calculateSubtotal(items);

        double discountTotal = 0.0;
        for (Discount d : discounts) discountTotal += d.apply(subtotal);

        double tax = taxService.calculateTax(subtotal - discountTotal);
        double grand = subtotal - discountTotal + tax;

        String invoiceText = renderer.render(items, subtotal, discountTotal, tax, grand);

        emailer.send(email);
        logger.log(email, grand);

        return invoiceText;
    }
}

public class InvoiceSRPOCPmodified{
    public static void main(String[] args) {
        Pricing pricing = new PricingService();
        Tax tax = new TaxService(0.18);
        Renderer renderer = new InvoiceRenderer();
        Emailer emailer = new EmailService();
        Logger logger = new LoggerService();

        InvoiceService svc = new InvoiceService(pricing, tax, renderer, emailer, logger);

        List<LineItem> items = Arrays.asList(
                new LineItem("BOOK-001", 2, 500.0),
                new LineItem("USB-DRIVE", 1, 799.0)
        );

        List<Discount> discounts = Arrays.asList(
                new PercentOffDiscount(10.0)
        );

        System.out.println(svc.process(items, discounts, "customer@example.com"));
    }
}
