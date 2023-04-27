package fr.en0ri4n.justdo.utils;

import fr.en0ri4n.justdo.config.GameConfig;
import fr.en0ri4n.justdo.core.GameCore;
import org.bukkit.entity.Player;

import java.util.*;

public class JDObjectives
{
    private static final JDObjectives INSTANCE = new JDObjectives();
    private final Map<UUID, ArrayList<Objective>> objectives;

    private JDObjectives()
    {
        this.objectives = new HashMap<>();
    }

    public List<Objective> getObjectives(Player player)
    {
        if(player == null) return new ArrayList<>();

        return objectives.get(player.getUniqueId());
    }

    public Objective getObjective(Player player, int id)
    {
        return objectives.get(player.getUniqueId()).stream().filter(obj -> obj.getId() == id).findFirst().orElse(null);
    }

    public Objective getCurrentObjective(Player player)
    {
        return getObjective(player, GameCore.getInstance().getScore(player));
    }

    public void addObjective(Player player, String objectiveName, GameCore.GameMode gameMode, GameConfig.ObjectiveDifficulty difficulty)
    {
        Objective objective = new Objective(objectives.get(player.getUniqueId()) == null ? 0 : objectives.get(player.getUniqueId()).size(), gameMode, objectiveName, difficulty);

        if(!objectives.containsKey(player.getUniqueId()))
            objectives.put(player.getUniqueId(), new ArrayList<>());

        objectives.get(player.getUniqueId()).add(objective);
    }

    public int getObjectivesCount()
    {
        return objectives.entrySet().stream().findFirst().get().getValue().size();
    }

    public static JDObjectives getInstance()
    {
        return INSTANCE;
    }

    public void create(Player player)
    {
        objectives.put(player.getUniqueId(), new ArrayList<>());
    }

    public void remove(Player player)
    {
        objectives.remove(player.getUniqueId());
    }

    public void clear()
    {
        objectives.clear();
    }

    public static class Objective
    {
        private final int id;
        private final GameCore.GameMode gameMode;
        private final String objectiveName;
        private final GameConfig.ObjectiveDifficulty difficulty;

        Objective(int id, GameCore.GameMode gameMode, String objectiveName, GameConfig.ObjectiveDifficulty difficulty)
        {
            this.id = id;
            this.gameMode = gameMode;
            this.objectiveName = objectiveName;
            this.difficulty = difficulty;
        }

        public int getId()
        {
            return id;
        }

        public GameCore.GameMode getGameMode()
        {
            return gameMode;
        }

        public String getObjectiveName()
        {
            return objectiveName;
        }

        public GameConfig.ObjectiveDifficulty getDifficulty()
        {
            return difficulty;
        }
    }
}
