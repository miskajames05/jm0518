import java.time.DayOfWeek;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.util.Objects;

public abstract class Tool {
    private String brand;
    private ToolType toolType;

    public Tool(String brand, ToolType toolType) {
        this.brand = Objects.requireNonNull(brand);
        this.toolType = Objects.requireNonNull(toolType);
    }

    public String getBrand() {
        return brand;
    }
    public ToolType getToolType() {
        return toolType;
    }

    /**
     * Calculates the number of charged days for this item from the day after checkout (not including first day) through and including the due date.
     */
    public abstract int calculateChargeDays(Instant checkoutDate, Instant dueDate, int rentalDayCount);

    protected boolean isWeekend(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    protected boolean isHoliday(Instant date) {
        var month = date.atZone(ZoneId.systemDefault()).getMonth();
        var dayOfMonth = date.atZone(ZoneId.systemDefault()).getDayOfMonth();
        var dayOfWeek = date.atZone(ZoneId.systemDefault()).getDayOfWeek();

        if(isIndependenceDayHoliday(month, dayOfMonth, dayOfWeek)) {
            return true;
        }
        if(isLaborDayHoliday(month, dayOfMonth, dayOfWeek)) {
            return true;
        }

        return false;
    }

    protected DayOfWeek getDayOfWeek(Instant date) {
        return date.atZone(ZoneId.systemDefault()).getDayOfWeek();
    }

    /**
     * Determines if a day is the Independence Day holiday.
     *
     * Note: If July 4th is a Saturday - July 3rd is considered the holiday. If July 4th is a Sunday - July 5th is considered the holiday.
     */
    private boolean isIndependenceDayHoliday(Month month, int dayOfMonth, DayOfWeek dayOfWeek) {
        if(month == Month.JULY && dayOfMonth == 4 && !isWeekend(dayOfWeek)) {
            return true;
        }
        if(month == Month.JULY && dayOfMonth == 3 && dayOfWeek == DayOfWeek.FRIDAY) {
            return true;
        }
        if(month == Month.JULY && dayOfMonth == 5 && dayOfWeek == DayOfWeek.MONDAY) {
            return true;
        }
        return false;
    }

    /**
     * Determines if a day is the Labor Day holiday.
     *
     * Note: This will always be the first Monday in September.
     */
    private boolean isLaborDayHoliday(Month month, int dayOfMonth, DayOfWeek dayOfWeek) {
        return month == Month.SEPTEMBER && dayOfMonth < 8 && dayOfWeek == DayOfWeek.MONDAY;
    }
}
