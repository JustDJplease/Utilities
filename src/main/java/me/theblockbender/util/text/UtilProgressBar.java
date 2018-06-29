package me.theblockbender.util.text;

import org.bukkit.ChatColor;

public class UtilProgressBar {
    /**
     * Get a progress bar
     *
     * @param current           how many points the user has
     * @param max               the maximal number of <b>points</b> the user can get
     * @param totalBars         the amount of <b>bars</b> these points should be divided over
     * @param symbol            the symbol used to build the bars
     * @param completedColor    the colour used to indicate bars that are completed
     * @param notCompletedColor the colour used to indicate bars that are <b>not</b> completed
     * @return progress bar string
     */
    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) ((int) totalBars * percent);
        int leftOver = (totalBars - progressBars);

        StringBuilder sb = new StringBuilder();
        sb.append(completedColor);
        for (int i = 0; i < progressBars; i++) {
            sb.append(symbol);
        }
        sb.append(notCompletedColor);
        for (int i = 0; i < leftOver; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }
}
