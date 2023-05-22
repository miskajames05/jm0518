public enum ToolType {
    CHAINSAW(1.49, "Chainsaw"),
    LADDER(1.99, "Ladder"),
    JACKHAMMER(2.99, "Jackhammer");

    private final double dailyCharge;
    private final String displayName;

    ToolType(double dailyCharge, String displayName) {
      this.dailyCharge = dailyCharge;
      this.displayName = displayName;
    }

    public double getDailyCharge() {
        return dailyCharge;
    }

    public String getDisplayName() {
        return displayName;
    }
}
