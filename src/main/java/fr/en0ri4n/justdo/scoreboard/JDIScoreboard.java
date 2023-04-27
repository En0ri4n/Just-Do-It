package fr.en0ri4n.justdo.scoreboard;

import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.scoreboard.utils.PerPlayerScoreboard;
import fr.en0ri4n.justdo.utils.JDObjectives;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class JDIScoreboard extends PerPlayerScoreboard
{
    private static final JDIScoreboard INSTANCE = new JDIScoreboard();

    private final String objectiveName = "JustDoIt";

    @Override
    protected Objective getMainObjective(Scoreboard scoreboard)
    {
        return scoreboard.getObjective(objectiveName);
    }

    @Override
    protected void registerObjectives(Player player, Scoreboard scoreboard)
    {
        // Register and Display health in player list
        Objective healthObj = scoreboard.registerNewObjective("Health", Criteria.HEALTH, red("❤"));
        healthObj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        healthObj.setRenderType(RenderType.HEARTS);

        // Register Game Scoreboard
        Objective gameObj = scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, darkRed("Just") + red("Do") + gold("It ") + white("Scores"));
        gameObj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Override
    protected void updatePlayerScoreboard(Player player, Scoreboard scoreboard)
    {
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
        List<UUID> playerIds = GameCore.getInstance().getScores().entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Integer::intValue).reversed())).map(Map.Entry::getKey).toList();

        for(UUID playerId : playerIds)
            if(Bukkit.getPlayer(playerId) != null) // Check if player is online because SOMETIMES PLAYERS LEAVE THE GAME AND IT'S NOT HANDLED
            {
                OfflinePlayer displayPlayer = Bukkit.getOfflinePlayer(playerId);
                addLine(scoreboard, lightPurple(displayPlayer.getName() + " : ") + darkPurple(GameCore.getInstance().getScores().get(playerId) + "/" + JDObjectives.getInstance().getObjectivesCount()), line--);
            }

        addLine(scoreboard, yellow(""), line--);
        addLine(scoreboard, gray(date), line--);
    }

    public static JDIScoreboard getInstance()
    {
        return INSTANCE;
    }
}
