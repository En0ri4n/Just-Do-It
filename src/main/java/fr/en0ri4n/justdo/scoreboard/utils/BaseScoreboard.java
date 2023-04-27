package fr.en0ri4n.justdo.scoreboard.utils;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class BaseScoreboard
{
    /**
     * Update the scoreboard
     */
    public abstract void updateScoreboard();

    /**
     * Clear all lines of a scoreboard
     * @param scoreboard The scoreboard
     */
    protected void clearLines(Scoreboard scoreboard)
    {
        scoreboard.getEntries().forEach(scoreboard::resetScores);
    }

    /**
     * Add a line to a scoreboard at a specific line<br>
     * Example:<br>
     * <code>
     *     int line = 15; // From top to bottom<br>
     *     addLine(scoreboard, "Hello", line--);<br>
     *     addLine(scoreboard, "World", line--);<br>
     *     addLine(scoreboard, "!", line--);<br>
     * </code>
     * @param scoreboard The scoreboard
     * @param message The line content
     * @param line The line number
     */
    protected void addLine(Scoreboard scoreboard, String message, int line)
    {
        getMainObjective(scoreboard).getScore(message).setScore(line);
    }

    /**
     * Get the main objective of the scoreboard (the one that will be displayed)<br>
     * You can create as much objectives as you want, but only the main one will be displayed to the player<br>
     * It will automatically be displayed in the sidebar
     *
     * @param scoreboard The scoreboard
     * @return The main objective
     */
    protected abstract Objective getMainObjective(Scoreboard scoreboard);
}
