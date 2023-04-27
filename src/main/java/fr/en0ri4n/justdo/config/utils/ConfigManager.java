package fr.en0ri4n.justdo.config.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigManager
{
    private static ConfigManager instance;

    private final List<BaseConfig> configs;

    private ConfigManager()
    {
        configs = new ArrayList<>();
    }

    public List<BaseConfig> getConfigs()
    {
        return configs;
    }

    public ConfigManager addConfigs(BaseConfig... configurations)
    {
        Collections.addAll(configs, configurations);
        return instance;
    }

    public void loadConfigs()
    {
        for(BaseConfig config : instance.configs)
            config.load();
    }

    public static void create()
    {
        if(instance == null)
            instance = new ConfigManager();
        else
            throw new IllegalStateException("ConfigManager instance already created");
    }

    /**
     * Get the ConfigManager instance<br>
     * <b>WARNING: </b> This method will return null if the ConfigManager instance is not created yet<br>
     * Use {@link ConfigManager#create()} to create the ConfigManager instance
     */
    public static ConfigManager get()
    {
        return instance;
    }
}
