package guru.qa.niffler.utils;

public enum DataFilterValues {
    ALL_TIME ("All time"),
    LAST_MONTH ("Last month"),
    LAST_WEEK ("Last week"),
    TODAY("Today");

    private String period;

    DataFilterValues(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }
}
