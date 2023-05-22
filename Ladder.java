import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Ladder extends Tool {

    public Ladder(String brand) {
        super(brand, ToolType.LADDER);
    }

    /**
     * Calculates the number of charged days for this item from the day after checkout (not including first day) through and including the due date.
     *
     * Note: Ladders have no charge on holidays.
     */
    @Override
    public int calculateChargeDays(Instant checkoutDate, Instant dueDate, int rentalDayCount) {
        int chargeDays = 0;
        Instant date = checkoutDate;
        for(int dayCounter = 0; dayCounter <= rentalDayCount; dayCounter++) {
            if(dayCounter == 0 || isHoliday(date)) {
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
