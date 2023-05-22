import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final int rentalDays;
    private final Instant checkoutDate;
    private final Instant dueDate;
    private final double dailyRentalCharge;
    private final int chargeDays;
    private final double preDiscountCharge;
    private final int discountPercent;
    private final double discountAmount;
    private final double finalCharge;


    public RentalAgreement(
            String toolCode,
            String toolType,
            String toolBrand,
            int rentalDays,
            Instant checkoutDate,
            Instant dueDate,
            double dailyRentalCharge,
            int chargeDays,
            double preDiscountCharge,
            int discountPercent,
            double discountAmount,
            double finalCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyRentalCharge = dailyRentalCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    public void printRentalAgreement() {
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + formatDate(checkoutDate));
        System.out.println("Due date: " + formatDate(dueDate));
        System.out.println("Daily Rental Charge: " + formatter.format(dailyRentalCharge));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: " + formatter.format(preDiscountCharge));
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: " + formatter.format(discountAmount));
        System.out.println("Final charge: " + formatter.format(finalCharge));
    }

    private String formatDate(Instant date) {
        return DateTimeFormatter.ofPattern("MM/dd/yy")
                .withZone(ZoneId.of("UTC"))
                .format(date);
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public Instant getCheckoutDate() {
        return checkoutDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public double getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public double getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalCharge() {
        return finalCharge;
    }
}
