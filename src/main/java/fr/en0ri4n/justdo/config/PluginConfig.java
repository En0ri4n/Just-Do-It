package fr.en0ri4n.justdo.config;

import fr.en0ri4n.justdo.JustDoMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PluginConfig
{
    private static final PluginConfig INSTANCE = new PluginConfig();

    // Config Name and File
    private static final String CONFIG_NAME = "config.yml";

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

    private PluginConfig() {}

    public void load()
    {
        JustDoMain.getInstance().saveResource(CONFIG_NAME, false);

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(JustDoMain.getInstance().getDataFolder(), CONFIG_NAME));
        minimumPlayers = config.getInt("minimum_players");
        autoStart = config.getBoolean("auto_start");

        easyCount = config.getInt("easy_tasks");
        mediumCount = config.getInt("medium_tasks");
        hardCount = config.getInt("hard_tasks");


        easyRandomRange = config.getIntegerList("easy_random").toArray(new Integer[0]);
        mediumRandomRange = config.getIntegerList("medium_random").toArray(new Integer[0]);
        hardRandomRange = config.getIntegerList("hard_random").toArray(new Integer[0]);

        areTasksRandom = config.getBoolean("random_tasks");
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
