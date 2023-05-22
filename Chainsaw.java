import java.time.DayOfWeek;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Chainsaw extends Tool {

    public Chainsaw(String brand) {
        super(brand, ToolType.CHAINSAW);
    }

    /**
     * Calculates the number of charged days for this item from the day after checkout (not including first day) through and including the due date.
     *
     * Note: Chainsaws have no charge on the weekends.
     */
    @Override
    public int calculateChargeDays(Instant checkoutDate, Instant dueDate, int rentalDayCount) {
        int chargeDays = 0;
        Instant date = checkoutDate;
        for(int dayCounter = 0; dayCounter <= rentalDayCount; dayCounter++) {
            DayOfWeek dayOfWeek = getDayOfWeek(date);
            if(dayCounter == 0 || isWeekend(dayOfWeek)) {
                date = date.plus(1, ChronoUnit.DAYS);
                // No charge
                continue;
            }
            chargeDays++;
            date = date.plus(1, ChronoUnit.DAYS);
        }
        return chargeDays;
    }
}
