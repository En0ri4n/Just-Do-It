package fr.en0ri4n.justdo.config;

import fr.en0ri4n.justdo.JustDoMain;
import fr.en0ri4n.justdo.config.utils.BaseConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig extends BaseConfig
{
    private static final PluginConfig INSTANCE = new PluginConfig();

    // Config
    private int minimumPlayers;
    private boolean autoStart;

    private int easyCount;
    private int mediumCount;
    private int hardCount;

    private Integer[] easyRandomRange;
    private Integer[] mediumRandomRange;
    private Integer[] hardRandomRange;

    private boolean areTasksRandom;

    private PluginConfig() {
        super(JustDoMain.getInstance(), "config.yml", true);
    }

    @Override
    protected void loadConfig(FileConfiguration config)
    {
        minimumPlayers = config.getInt("minimum_players");
        autoStart = config.getBoolean("auto_start");

        easyCount = config.getInt("easy_tasks");
        mediumCount = config.getInt("medium_tasks");
        hardCount = config.getInt("hard_tasks");


        easyRandomRange = config.getIntegerList("easy_random").toArray(new Integer[0]);
        mediumRandomRange = config.getIntegerList("medium_random").toArray(new Integer[0]);
        hardRandomRange = config.getIntegerList("hard_random").toArray(new Integer[0]);

        areTasksRandom = config.getBoolean("random_tasks");

        GameConfig.getInstance().setup();
    }

    public int getMinimumPlayers()
    {
        return minimumPlayers;
    }

    public int getEasyCount()
    {
        return easyCount;
    }

    public int getMediumCount()
    {
        return mediumCount;
    }

    public int getHardCount()
    {
        return hardCount;
    }

    public Integer[] getEasyRandomRange()
    {
        return easyRandomRange;
    }

    public Integer[] getMediumRandomRange()
    {
        return mediumRandomRange;
    }

    public Integer[] getHardRandomRange()
    {
        return hardRandomRange;
    }

    public boolean areTasksRandom()
    {
        return areTasksRandom;
    }

    public static PluginConfig getInstance()
    {
        return INSTANCE;
    }

    public boolean isAutoStart()
    {
        return autoStart;
    }
}
