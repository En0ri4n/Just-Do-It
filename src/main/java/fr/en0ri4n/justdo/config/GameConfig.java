package fr.en0ri4n.justdo.config;

import com.mojang.datafixers.util.Pair;
import fr.en0ri4n.justdo.JustDoMain;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.utils.CraftHelper;
import fr.en0ri4n.justdo.utils.Randomizer;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class GameConfig
{
    private static final GameConfig INSTANCE = new GameConfig();

    private static final String BLOCKS_CONFIG_NAME = "blocks_config.yml";
    private static final String ITEMS_CONFIG_NAME = "items_config.yml";
    private static final String ENTITIES_CONFIG_NAME = "entities_config.yml";

    // Available Blocks, Items and Entities
    private final Map<ObjectiveDifficulty, List<String>> availableBreakableBlocks = initializeEmptyMap();
    private final Map<ObjectiveDifficulty, List<String>> availablePlaceableBlocks = initializeEmptyMap();
    private final Map<ObjectiveDifficulty, List<String>> availableWalkableBlocks = initializeEmptyMap();

    private final Map<ObjectiveDifficulty, List<String>> availablePickableItems = initializeEmptyMap();
    private final Map<ObjectiveDifficulty, List<String>> availableCraftableItems = initializeEmptyMap();

    private final Map<ObjectiveDifficulty, List<String>> availableKillableEntities = initializeEmptyMap();

    private final Map<ObjectiveDifficulty, Integer> objectivesDifficulty = new HashMap<>();

    private GameConfig()
    {
        setup();
    }

    private void setup()
    {
        if(PluginConfig.getInstance().areTasksRandom())
        {
            objectivesDifficulty.put(ObjectiveDifficulty.EASY, Randomizer.randomInt(PluginConfig.getInstance().getEasyRandomRange()));
            objectivesDifficulty.put(ObjectiveDifficulty.MEDIUM, Randomizer.randomInt(PluginConfig.getInstance().getMediumRandomRange()));
            objectivesDifficulty.put(ObjectiveDifficulty.HARD, Randomizer.randomInt(PluginConfig.getInstance().getHardRandomRange()));
        }
        else
        {
            objectivesDifficulty.put(ObjectiveDifficulty.EASY, PluginConfig.getInstance().getEasyCount());
            objectivesDifficulty.put(ObjectiveDifficulty.MEDIUM, PluginConfig.getInstance().getMediumCount());
            objectivesDifficulty.put(ObjectiveDifficulty.HARD, PluginConfig.getInstance().getHardCount());
        }
    }

    public Pair<Pair<List<GameCore.GameMode>, List<ObjectiveDifficulty>>, List<String>> getRandoms()
    {
        List<GameCore.GameMode> gameModes = new ArrayList<>();
        List<ObjectiveDifficulty> difficulties = new ArrayList<>();

        for(int i = 0; i < getObjectivesCount(); i++)
            gameModes.add(Randomizer.random(GameCore.GameMode.values()));

        List<String> objectives = new ArrayList<>();

        int index = 0;
        for(ObjectiveDifficulty difficulty : objectivesDifficulty.keySet())
            for(int i = 0; i < objectivesDifficulty.get(difficulty); i++)
            {
                List<String> availables = getAvailables(gameModes.get(index)).get(difficulty);
                objectives.add(Randomizer.random(availables));
                difficulties.add(difficulty);
                index++;
            }

        return Pair.of(Pair.of(gameModes, difficulties), objectives);
    }

    @SuppressWarnings({"ConstantConditions"})
    public void load()
    {
        JustDoMain.getInstance().saveResource(BLOCKS_CONFIG_NAME, false);
        JustDoMain.getInstance().saveResource(ITEMS_CONFIG_NAME, false);
        JustDoMain.getInstance().saveResource(ENTITIES_CONFIG_NAME, false);

        // Blocks, Items and Entities Config
        FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(new File(JustDoMain.getInstance().getDataFolder(), BLOCKS_CONFIG_NAME));
        FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(new File(JustDoMain.getInstance().getDataFolder(), ITEMS_CONFIG_NAME));
        FileConfiguration entitiesConfig = YamlConfiguration.loadConfiguration(new File(JustDoMain.getInstance().getDataFolder(), ENTITIES_CONFIG_NAME));

        for(GameCore.GameMode gameMode : GameCore.GameMode.values())
        {
            GameCore.GameModeEntry type = gameMode.getEntryType();
            FileConfiguration currentConfig = type.is(GameCore.GameModeEntry.BLOCK) ? blockConfig : type.is(GameCore.GameModeEntry.ITEM) ? itemConfig : entitiesConfig;

            for(ObjectiveDifficulty difficulty : ObjectiveDifficulty.values())
                getAvailables(gameMode).get(difficulty).addAll(currentConfig.getConfigurationSection(gameMode.getConfigName()).getStringList(difficulty.getConfigName()));
        }
    }

    public boolean isBlock(String name)
    {
        return CraftHelper.getBlockRegistry().c(MinecraftKey.a(name));
    }

    public boolean isItem(String name)
    {
        return CraftHelper.getItemRegistry().c(MinecraftKey.a(name));
    }

    protected Map<ObjectiveDifficulty, List<String>> getAvailables(GameCore.GameMode gameMode)
    {
        return switch(gameMode)
                {
                    case PLACE -> availablePlaceableBlocks;
                    case STAND -> availableWalkableBlocks;
                    case BREAK -> availableBreakableBlocks;

                    case PICKUP -> availablePickableItems;
                    case CRAFT -> availableCraftableItems;

                    case KILL -> availableKillableEntities;
                };
    }

    public int getObjectivesCount()
    {
        return objectivesDifficulty.values().stream().mapToInt(Integer::intValue).sum();
    }

    private static Map<ObjectiveDifficulty, List<String>> initializeEmptyMap()
    {
        Map<ObjectiveDifficulty, List<String>> map = new HashMap<>();

        Arrays.stream(ObjectiveDifficulty.values()).forEach(dif -> map.put(dif, new ArrayList<>()));

        return map;
    }

    public static GameConfig getInstance()
    {
        return INSTANCE;
    }

    public enum ObjectiveDifficulty
    {
        EASY("easy"),
        MEDIUM("medium"),
        HARD("hard");

        private final String configName;

        ObjectiveDifficulty(String configName)
        {
            this.configName = configName;
        }

        public String getConfigName()
        {
            return configName;
        }
    }
}
