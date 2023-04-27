package fr.en0ri4n.justdo;

import fr.en0ri4n.justdo.commands.*;
import fr.en0ri4n.justdo.commands.utils.CommandManager;
import fr.en0ri4n.justdo.config.GameConfig;
import fr.en0ri4n.justdo.config.PluginConfig;
import fr.en0ri4n.justdo.config.utils.ConfigManager;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.listeners.GameListener;
import fr.en0ri4n.justdo.listeners.ListenerManager;
import fr.en0ri4n.justdo.listeners.LobbyListener;
import fr.en0ri4n.justdo.listeners.PlayerListener;
import fr.en0ri4n.justdo.runnables.LobbyRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JustDoMain extends JavaPlugin
{
    public static final String ID = "JustDoIt";

    private static JustDoMain INSTANCE;

    @Override
    public void onEnable()
    {
        INSTANCE = this;

        registerConfig();

        registerListeners();

        registerCommands();

        GameCore.getInstance().load();

        LobbyRunnable.start();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerConfig()
    {
        ConfigManager.create();
        ConfigManager.get().addConfigs(
                PluginConfig.getInstance(),
                GameConfig.getInstance());

        ConfigManager.get().loadConfigs();
    }

    private void registerCommands()
    {
        CommandManager.create();
        CommandManager.get().addCommands(
                AskRestartCommand.getInstance(),
                AskPassCommand.getInstance(),
                ObjectiveListCommand.getInstance(),
                new StartCommand(),
                new TopCommand(),
                new ObjectivesCommand());

        CommandManager.get().registerCommands();
    }

    private void registerListeners()
    {
        ListenerManager.create(this);
        ListenerManager.get().addListeners(
                new LobbyListener(),
                new GameListener(),
                new PlayerListener(),
                ObjectiveListCommand.getInstance());

        ListenerManager.get().registerListeners();
    }

    @Override
    public void onDisable()
    {
        GameCore.getInstance().unload();

        Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
    }

    public static JustDoMain getInstance()
    {
        return INSTANCE;
    }
}
