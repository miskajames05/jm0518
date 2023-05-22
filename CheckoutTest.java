import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutTest {

    private static final String PATTERN_FORMAT = "MM/dd/yy";

    private Map<String, Tool> map = new HashMap<>();

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    @BeforeAll
    private void setup() {
        map.put("CHNS", new Chainsaw("Stihl"));
        map.put("LADW", new Ladder("Werner"));
        map.put("JAKD", new Jackhammer("DeWalt"));
        map.put("JAKR", new Jackhammer("Rigid"));
    }

    @Test
    public void test1() {
        String toolCode = "JAKR";
        int rentalDayCount = 5;
        int discountPercent = 101;
        String checkoutDate = "9/3/15";
        Checkout checkout = new Checkout();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate));

        assertEquals("Please input a whole number for 'discountPercent' that is in the range of 0-100", exception.getMessage());
    }

    @Test
    public void test2() {
        String toolCode = "LADW";
        int rentalDayCount = 3;
        int discountPercent = 10;
        String checkoutDate = "7/2/20";
        Instant parsedCheckoutDate = parseDate(checkoutDate);
        Checkout checkout = new Checkout();

        RentalAgreement rentalAgreement = checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate);

        double expectedDailyRentalCharge = 1.99;
        int expectedChargeDays = 2;
        double expectedPreDiscountCharge = 3.98;
        int expectedDiscountPercent = 10;
        double expectedDiscountAmount = 0.40;
        double expectedFinalCharge = 3.58;
        assertRentalAgreement(toolCode, rentalAgreement, rentalDayCount, parsedCheckoutDate, expectedDailyRentalCharge,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount, expectedFinalCharge);
    }

    @Test
    public void test3() {
        String toolCode = "CHNS";
        int rentalDayCount = 5;
        int discountPercent = 25;
        String checkoutDate = "7/2/15";
        Instant parsedCheckoutDate = parseDate(checkoutDate);
        Checkout checkout = new Checkout();

        RentalAgreement rentalAgreement = checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate);

        double expectedDailyRentalCharge = 1.49;
        int expectedChargeDays = 3;
        double expectedPreDiscountCharge = 4.47;
        int expectedDiscountPercent = 25;
        double expectedDiscountAmount = 1.12;
        double expectedFinalCharge = 3.35;
        assertRentalAgreement(toolCode, rentalAgreement, rentalDayCount, parsedCheckoutDate, expectedDailyRentalCharge,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount, expectedFinalCharge);
    }

    @Test
    public void test4() {
        String toolCode = "JAKD";
        int rentalDayCount = 6;
        int discountPercent = 0;
        String checkoutDate = "9/3/15";
        Instant parsedCheckoutDate = parseDate(checkoutDate);
        Checkout checkout = new Checkout();

        RentalAgreement rentalAgreement = checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate);

        double expectedDailyRentalCharge = 2.99;
        int expectedChargeDays = 3;
        double expectedPreDiscountCharge = 8.97;
        int expectedDiscountPercent = 0;
        double expectedDiscountAmount = 0.00;
        double expectedFinalCharge = 8.97;
        assertRentalAgreement(toolCode, rentalAgreement, rentalDayCount, parsedCheckoutDate, expectedDailyRentalCharge,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount, expectedFinalCharge);
    }

    @Test
    public void test5() {
        String toolCode = "JAKR";
        int rentalDayCount = 9;
        int discountPercent = 0;
        String checkoutDate = "7/2/15";
        Instant parsedCheckoutDate = parseDate(checkoutDate);
        Checkout checkout = new Checkout();

        RentalAgreement rentalAgreement = checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate);

        double expectedDailyRentalCharge = 2.99;
        int expectedChargeDays = 5;
        double expectedPreDiscountCharge = 14.95;
        int expectedDiscountPercent = 0;
        double expectedDiscountAmount = 0.00;
        double expectedFinalCharge = 14.95;
        assertRentalAgreement(toolCode, rentalAgreement, rentalDayCount, parsedCheckoutDate, expectedDailyRentalCharge,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount, expectedFinalCharge);
    }

    @Test
    public void test6() {
        String toolCode = "JAKR";
        int rentalDayCount = 4;
        int discountPercent = 50;
        String checkoutDate = "7/2/20";
        Instant parsedCheckoutDate = parseDate(checkoutDate);
        Checkout checkout = new Checkout();

        RentalAgreement rentalAgreement = checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate);

        double expectedDailyRentalCharge = 2.99;
        int expectedChargeDays = 1;
        double expectedPreDiscountCharge = 2.99;
        int expectedDiscountPercent = 50;
        double expectedDiscountAmount = 1.50;
        double expectedFinalCharge = 1.49;
        assertRentalAgreement(toolCode, rentalAgreement, rentalDayCount, parsedCheckoutDate, expectedDailyRentalCharge,
                expectedChargeDays, expectedPreDiscountCharge, expectedDiscountPercent, expectedDiscountAmount, expectedFinalCharge);
    }

    @Test
    public void checkout_rentalDayCountOutOfRange_throwException() {
        String toolCode = "CHNS";
        int rentalDayCount = 0;
        int discountPercent = 20;
        String checkoutDate = "06/15/23";
        Checkout checkout = new Checkout();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate));

        assertEquals("Please input a value for 'rentalDayCount' that is equal to or greater than 1", exception.getMessage());
    }

    @Test
    public void checkout_discountPercentOutOfRange_throwException() {
        String toolCode = "CHNS";
        int rentalDayCount = 5;
        int discountPercent = 101;
        String checkoutDate = "06/15/23";
        Checkout checkout = new Checkout();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate));

        assertEquals("Please input a whole number for 'discountPercent' that is in the range of 0-100", exception.getMessage());
    }

    @Test
    public void checkout_parseDateFails_throwException() {
        String toolCode = "CHNS";
        int rentalDayCount = 5;
        int discountPercent = 10;
        String checkoutDate = "06-15-23";
        Checkout checkout = new Checkout();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate));

        assertEquals("There was an issue parsing the 'checkoutDate'. Please use the format mm/dd/yy", exception.getMessage());
    }

    @Test
    public void checkout_toolCodeDoesNotExist_returnsNull() {
        String toolCode = "ABC";
        int rentalDayCount = 5;
        int discountPercent = 10;
        String checkoutDate = "06/15/23";
        Checkout checkout = new Checkout();

        RentalAgreement rentalAgreement = checkout.checkout(toolCode, rentalDayCount, discountPercent, checkoutDate);

        assertNull(rentalAgreement);
    }

    private void assertRentalAgreement(String toolCode, RentalAgreement rentalAgreement, int rentalDayCount, Instant parsedCheckoutDate,
                                       double expectedDailyRentalCharge, int expectedChargeDays, double expectedPreDiscountCharge,
                                       int expectedDiscountPercent, double expectedDiscountAmount, double expectedFinalCharge) {
        Tool tool = map.get(toolCode);
        assertNotNull(tool);
        final var toolType = tool.getToolType();
        assertEquals(rentalAgreement.getToolCode(), toolCode);
        assertEquals(rentalAgreement.getToolType(), toolType.getDisplayName());
        assertEquals(rentalAgreement.getToolBrand(), tool.getBrand());
        assertEquals(rentalAgreement.getRentalDays(), rentalDayCount);
        assertEquals(rentalAgreement.getCheckoutDate(), parsedCheckoutDate);
        assertEquals(rentalAgreement.getDueDate(), parsedCheckoutDate.plus(rentalDayCount, ChronoUnit.DAYS));
        assertEquals(formatter.format(rentalAgreement.getDailyRentalCharge()), formatter.format(expectedDailyRentalCharge));
        assertEquals(rentalAgreement.getChargeDays(), expectedChargeDays);
        assertEquals(formatter.format(rentalAgreement.getPreDiscountCharge()), formatter.format(expectedPreDiscountCharge));
        assertEquals(rentalAgreement.getDiscountPercent(), expectedDiscountPercent);
        assertEquals(formatter.format(rentalAgreement.getDiscountAmount()), formatter.format(expectedDiscountAmount));
        assertEquals(formatter.format(rentalAgreement.getFinalCharge()), formatter.format(expectedFinalCharge));
    }

    private Instant parseDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_FORMAT);
        try {
            return formatter.parse(date).toInstant();
        } catch (ParseException e) {
            throw new IllegalArgumentException("There was an issue parsing the 'checkoutDate'. Please use the format mm/dd/yy");
        }
    }
}