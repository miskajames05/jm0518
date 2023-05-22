import java.time.DayOfWeek;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Jackhammer extends Tool {

    public Jackhammer(String brand) {
        super(brand, ToolType.JACKHAMMER);
    }

    /**
     * Calculates the number of charged days for this item from the day after checkout (not including first day) through and including the due date.
     *
     * Note: Jackhammers have no charge on weekends and holidays.
     */
    @Override
    public int calculateChargeDays(Instant checkoutDate, Instant dueDate, int rentalDayCount) {
        int chargeDays = 0;
        Instant date = checkoutDate;
        for(int dayCounter = 0; dayCounter <= rentalDayCount; dayCounter++) {
            DayOfWeek dayOfWeek = getDayOfWeek(date);
            if(dayCounter == 0 || isHoliday(date) || isWeekend(dayOfWeek)) {
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
