package fr.en0ri4n.justdo.utils;

import fr.en0ri4n.justdo.core.GameCore;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.time.Instant;
import java.util.*;

import static fr.en0ri4n.justdo.utils.Colors.*;

@SuppressWarnings("ConstantConditions")
public class ScoreboardManager
{
    private static final ScoreboardManager INSTANCE = new ScoreboardManager();

    private final Map<UUID, Scoreboard> scoreboards;

    private final String objectiveName = "JustDoIt";

    private ScoreboardManager()
    {
        scoreboards = new HashMap<>();
    }

    public void registerPlayer(Player player)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        // Register and Display health in player list
        Objective healthObj = scoreboard.registerNewObjective("Health", Criteria.HEALTH, red("❤"));
        healthObj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        healthObj.setRenderType(RenderType.HEARTS);

        // Register Game Scoreboard
        Objective gameObj = scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, darkRed("Just") + red("Do") + gold("It ") + white("Scores"));
        gameObj.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);

        scoreboards.put(player.getUniqueId(), scoreboard);

        updateScoreboard(GameCore.getInstance().getScores());
    }

    public void unregisterPlayers()
    {
        Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }

    public void updateScoreboard(Map<UUID, Integer> playerScores)
    {
        for(Map.Entry<UUID, Scoreboard> scoreboardEntry : scoreboards.entrySet())
        {
            Player player = Bukkit.getPlayer(scoreboardEntry.getKey());

            if(player == null) continue;

            Scoreboard scoreboard = player.getScoreboard();

            int line = 15;

            clearLines(scoreboard);

            addLine(scoreboard, red(""), line--);

            String date = DateFormatUtils.format(Instant.now().toEpochMilli(), "dd/MM/yyyy");

            JDObjectives.Objective currentObjective = JDObjectives.getInstance().getCurrentObjective(player);

            if(currentObjective != null)
            {
                String difficulty = yellow("Level : ") + switch(currentObjective.getDifficulty())
                        {
                            case EASY -> green("Easy");
                            case MEDIUM -> gold("Medium");
                            case HARD -> red("Hard");
                        };

                addLine(scoreboard, yellow("Objectif :"), line--);

                addLine(scoreboard, aqua(GameCore.getInstance().getGameMode(player).getDescription()) + " " + darkAqua(currentObjective.getObjectiveName().split(":")[1]), line--);

                addLine(scoreboard, blue(""), line--);

                addLine(scoreboard, difficulty, line--);
            }

            addLine(scoreboard, green(""), line--);

            // Get player ids and sort them by score (first is the max, last is the lowest)
            List<UUID> playerIds = playerScores.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Integer::intValue).reversed())).map(Map.Entry::getKey).toList();

            for(UUID playerId : playerIds)
                if(Bukkit.getPlayer(playerId) != null) // Check if player is online because SOMETIMES PLAYERS LEAVE THE GAME AND IT'S NOT HANDLED
                {
                    OfflinePlayer displayPlayer = Bukkit.getOfflinePlayer(playerId);
                    addLine(scoreboard, lightPurple(displayPlayer.getName() + " : ") + darkPurple(String.valueOf(playerScores.get(playerId) + "/" + JDObjectives.getInstance().getObjectivesCount())), line--);
                }

            addLine(scoreboard, yellow(""), line--);
            addLine(scoreboard, gray(date), line--);
        }
    }

    private void clearLines(Scoreboard scoreboard)
    {
        scoreboard.getEntries().forEach(scoreboard::resetScores);
    }

    private void addLine(Scoreboard scoreboard, String message, int line)
    {
        getGameObjective(scoreboard).getScore(message).setScore(line);
    }

    public Objective getGameObjective(Scoreboard scoreboard)
    {
        return scoreboard.getObjective(objectiveName);
    }

    public static ScoreboardManager getInstance()
    {
        return INSTANCE;
    }
}
