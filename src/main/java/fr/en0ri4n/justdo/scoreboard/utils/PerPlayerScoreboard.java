package fr.en0ri4n.justdo.scoreboard.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PerPlayerScoreboard extends BaseScoreboard
{
    private final Map<UUID, Scoreboard> scoreboards;

    protected PerPlayerScoreboard()
    {
        scoreboards = new HashMap<>();
    }

    /**
     * Register a player to the scoreboard system
     * @param player The player to register
     */
    public void registerPlayer(Player player)
    {
        boolean alreadyRegistered = scoreboards.containsKey(player.getUniqueId());
        Scoreboard scoreboard =  alreadyRegistered ? scoreboards.get(player.getUniqueId()) : Bukkit.getScoreboardManager().getNewScoreboard();

        registerObjectives(player, scoreboard);

        getMainObjective(scoreboard).setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);

        if(!alreadyRegistered)
            scoreboards.put(player.getUniqueId(), scoreboard);

        updatePlayerScoreboard(player, scoreboard);
    }

    /**
     * Register the objectives for a player (called when the player is registered)
     * @param player The player
     * @param scoreboard The scoreboard of the player
     */
    protected abstract void registerObjectives(Player player, Scoreboard scoreboard);

    /**
     * Unregister all players from the scoreboard system and reset their scoreboard to the main one<br>
     * Call this when the plugin is disabled
     */
    public void unregisterPlayers()
    {
        scoreboards.clear();
        Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }

    /**
     * Update the scoreboard of all registered players<br>
     * Check if the player is online before updating his scoreboard to avoid errors and handle the case where the player left and rejoin
     */
    @Override
    public void updateScoreboard()
    {
        for(Map.Entry<UUID, Scoreboard> scoreboardEntry : scoreboards.entrySet())
        {
            Player player = Bukkit.getPlayer(scoreboardEntry.getKey());

            if(player == null) continue;

            if(scoreboards.containsKey(player.getUniqueId()) && player.getScoreboard() != scoreboards.get(player.getUniqueId()))
                player.setScoreboard(scoreboards.get(player.getUniqueId()));

            Scoreboard scoreboard = player.getScoreboard();

            updatePlayerScoreboard(player, scoreboard);
        }
    }

    /**
     * Update the scoreboard of a player (Called when the player is registered and when the scoreboard is updated in {@link #updateScoreboard()})
     * @param player The player
     * @param scoreboard The scoreboard of the player
     */
    protected abstract void updatePlayerScoreboard(Player player, Scoreboard scoreboard);
}
