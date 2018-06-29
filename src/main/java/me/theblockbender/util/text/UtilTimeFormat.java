package me.theblockbender.util.text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author TimeStamps
 * https://www.spigotmc.org/members/timestamps.329446/
 */
public class UtilTimeFormat {

    /**
     * Converts long to exact string.
     * Example convertMillisToString: 1 day, 2 hours, 30 minutes.
     * Example convertMillisToSimpleString: 1 day.
     *
     * @param millis     time
     * @param longFormat true = use abbreviations (m, h, d), false = use full words (minute, hour, day)
     * @param capUnits   true = the words will be capitalized (Minute, Hour, Day), false = write everything in lowercase
     * @param commas     true = show commas between words (" 1 day, 2 hours, 41 minutes"), false = show no commas
     * @return formatted string
     */
    public static String convertMillisToString(long millis, boolean longFormat, boolean capUnits, boolean commas) {
        int years = (int) (millis / 31536000000L % 60L);
        int months = (int) (millis / 2678400000L % 12L);
        int days = (int) (millis / 86400000L % 60L);
        int hours = (int) (millis / 3600000L % 24L);
        int minutes = (int) (millis / 60000L % 60L);
        int seconds = (int) (millis / 1000L % 60L);

        List<String> timeArray = new ArrayList<>();

        if (years > 0) {
            String unit = "";
            if (longFormat) {
                if (capUnits) {
                    if (years == 1) unit = " Year";
                    if (years > 1) unit = " Years";
                } else {
                    if (years == 1) unit = " year";
                    if (years > 1) unit = " years";
                }
            } else {
                if (capUnits) {
                    unit = "Y";
                } else {
                    unit = "y";
                }
            }
            timeArray.add(years + unit);
        }

        if (months > 0) {
            String unit = "";
            if (longFormat) {
                if (capUnits) {
                    if (months == 1) unit = " Month";
                    if (months > 1) unit = " Months";
                } else {
                    if (months == 1) unit = " month";
                    if (months > 1) unit = " months";
                }
            } else {
                if (capUnits) {
                    unit = "Mon";
                } else {
                    unit = "mon";
                }
            }
            timeArray.add(months + unit);
        }

        if (days > 0) {
            String unit = "";
            if (longFormat) {
                if (capUnits) {
                    if (days == 1) unit = " Day";
                    if (days > 1) unit = " Days";
                } else {
                    if (days == 1) unit = " day";
                    if (days > 1) unit = " days";
                }
            } else {
                if (capUnits) {
                    unit = "D";
                } else {
                    unit = "d";
                }
            }
            timeArray.add(days + unit);
        }

        if (hours > 0) {
            String unit = "";
            if (longFormat) {
                if (capUnits) {
                    if (hours == 1) unit = " Hour";
                    if (hours > 1) unit = " Hours";
                } else {
                    if (hours == 1) unit = " hour";
                    if (hours > 1) unit = " hours";
                }
            } else {
                if (capUnits) {
                    unit = "H";
                } else {
                    unit = "h";
                }
            }
            timeArray.add(hours + unit);
        }

        if (minutes > 0) {
            String unit = "";
            if (longFormat) {
                if (capUnits) {
                    if (minutes == 1) unit = " Minute";
                    if (minutes > 1) unit = " Minutes";
                } else {
                    if (minutes == 1) unit = " minute";
                    if (minutes > 1) unit = " minutes";
                }
            } else {
                if (capUnits) {
                    unit = "M";
                } else {
                    unit = "m";
                }
            }
            timeArray.add(minutes + unit);
        }

        if (seconds > 0) {
            String unit = "";
            if (longFormat) {
                if (capUnits) {
                    if (seconds == 1) unit = " Second";
                    if (seconds > 1) unit = " Seconds";
                } else {
                    if (seconds == 1) unit = " second";
                    if (seconds > 1) unit = " seconds";
                }
            } else {
                if (capUnits) {
                    unit = "S";
                } else {
                    unit = "s";
                }
            }
            timeArray.add(seconds + unit);
        }

        String time = timeArray.toString().replace("[", "").replace("]", "");

        if (!commas) return time.replace(",", "");

        return time;
    }

    /**
     * Converts a millisecond time into a simple (not exact) one word combination.
     * Example convertMillisToString: 1 day, 2 hours, 30 minutes.
     * Example convertMillisToSimpleString: 1 day.
     *
     * @param time time.
     * @return Formatted string
     */
    private String convertMillisToSimpleString(long time) {
        if (time == -1)
            return "Never";
        String type;
        int trim = 1;
        if (time < 60000)
            type = "SECONDS";
        else if (time < 3600000)
            type = "MINUTES";
        else if (time < 86400000)
            type = "HOURS";
        else
            type = "DAYS";
        String text;
        double num;
        switch (type) {
            case "DAYS":
                text = (num = trim(trim, time / 86400000d)) + " Day";
                break;
            case "HOURS":
                text = (num = trim(trim, time / 3600000d)) + " Hour";
                break;
            case "MINUTES":
                text = (num = trim(trim, time / 60000d)) + " Minute";
                break;
            case "SECONDS":
                text = (num = trim(trim, time / 1000d)) + " Second";
                break;
            default:
                text = (int) (num = (int) trim(0, time)) + " Millisecond";
                break;
        }
        if (num != 1)
            text += "s";
        return text;
    }

    /**
     * Formats a number nicely.
     *
     * @param degree Amount of decimal numbers.
     * @param d      Number to be formatted.
     * @return Formatted number.
     */
    private double trim(int degree, double d) {
        StringBuilder format = new StringBuilder("#.#");
        for (int i = 1; i < degree; i++)
            format.append("#");
        DecimalFormatSymbols symb = new DecimalFormatSymbols(Locale.US);
        DecimalFormat twoDForm = new DecimalFormat(format.toString(), symb);
        return Double.valueOf(twoDForm.format(d));
    }
}