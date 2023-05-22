import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This application serves as a checkout platform for customers to rent tools.
 */
public class Checkout {

    private static final int MINIMUM_RENTAL_DAY_COUNT = 1;
    private static final int MINIMUM_DISCOUNT_PERCENT = 0;
    private static final int MAXIMUM_DISCOUNT_PERCENT = 100;
    private static final String PATTERN_FORMAT = "MM/dd/yy";

    public static void main(String[] args) {
        Checkout checkout = new Checkout();
        checkout.checkout("CHNS", 10, 10, "12/30/23");
    }

    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discountPercent, String checkoutDateString) {
        if (rentalDayCount < MINIMUM_RENTAL_DAY_COUNT) {
            throw new IllegalArgumentException("Please input a value for 'rentalDayCount' that is equal to or greater than 1");
        }
        if (discountPercent < MINIMUM_DISCOUNT_PERCENT || discountPercent > MAXIMUM_DISCOUNT_PERCENT) {
            throw new IllegalArgumentException("Please input a whole number for 'discountPercent' that is in the range of 0-100");
        }

        Instant checkoutDate = parseDate(checkoutDateString);

        final var toolMap = Collections.unmodifiableMap(initializeMapping());
        final var tool = toolMap.get(toolCode);

        if(tool == null) {
            System.out.println("No tool exists for tool code=" + toolCode);
            return null;
        }

        final var toolType = tool.getToolType();
        final var toolBrand = tool.getBrand();
        final var dueDate = checkoutDate.plus(rentalDayCount, ChronoUnit.DAYS);
        final var dailyRentalCharge = toolType.getDailyCharge();
        final var chargeDays = tool.calculateChargeDays(checkoutDate, dueDate, rentalDayCount);
        final var preDiscountCharge = formatTwoDecimalHalfUp(chargeDays * dailyRentalCharge);
        final var discountAmount = formatTwoDecimalHalfUp(preDiscountCharge * ((double)discountPercent / 100));
        final var finalCharge = preDiscountCharge - discountAmount;

        RentalAgreement rentalAgreement = new RentalAgreement(
                toolCode,
                toolType.getDisplayName(),
                toolBrand,
                rentalDayCount,
                checkoutDate,
                dueDate,
                dailyRentalCharge,
                chargeDays,
                preDiscountCharge,
                discountPercent,
                discountAmount,
                finalCharge
        );

        rentalAgreement.printRentalAgreement();

        return rentalAgreement;
    }

    private Map<String, Tool> initializeMapping() {
        Map<String, Tool> map = new HashMap<>();
        map.put("CHNS", new Chainsaw("Stihl"));
        map.put("LADW", new Ladder("Werner"));
        map.put("JAKD", new Jackhammer("DeWalt"));
        map.put("JAKR", new Jackhammer("Rigid"));
        return map;
    }

    private Instant parseDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_FORMAT);
        try {
            return formatter.parse(date).toInstant();
        } catch (ParseException e) {
            throw new IllegalArgumentException("There was an issue parsing the 'checkoutDate'. Please use the format mm/dd/yy");
        }
    }

    private double formatTwoDecimalHalfUp(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
