package fr.en0ri4n.justdo;

import fr.en0ri4n.justdo.commands.AskPassCommand;
import fr.en0ri4n.justdo.commands.AskRestartCommand;
import fr.en0ri4n.justdo.commands.ObjectiveListCommand;
import fr.en0ri4n.justdo.config.GameConfig;
import fr.en0ri4n.justdo.config.PluginConfig;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.handlers.GameHandler;
import fr.en0ri4n.justdo.handlers.LobbyHandler;
import fr.en0ri4n.justdo.handlers.PlayerHandler;
import fr.en0ri4n.justdo.runnables.LobbyRunnable;
import fr.en0ri4n.justdo.utils.JDObjectives;
import fr.en0ri4n.justdo.utils.TaskHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class JustDoMain extends JavaPlugin
{
    public static final String ID = "JustDoIt";

    private static JustDoMain INSTANCE;

    @Override
    public void onEnable()
    {
        INSTANCE = this;

        PluginConfig.getInstance().load();
        GameConfig.getInstance().load();

        registerListeners();

        registerCommands();

        GameCore.getInstance().load();

        LobbyRunnable.start();
    }

    private void registerCommands()
    {
        registerCommand("start", (sender, command, label, args) ->
        {
            if(!sender.isOp()) return true;

            TaskHelper.cancelTask(LobbyRunnable.getId());
            GameCore.getInstance().startGame();
            return true;
        });

        registerCommand("objectives", (sender, command, label, args) ->
        {
            if(!(sender instanceof Player player)) return true;

            String title = aqua("Objectives :");

            List<JDObjectives.Objective> objectives = JDObjectives.getInstance().getObjectives(player);

            if(objectives == null || objectives.isEmpty()) return true;

            GameCore.send(player, title);
            for(int i = 0; i < objectives.size(); i++)
            {
                GameConfig.ObjectiveDifficulty difficulty = objectives.get(i).getDifficulty();

                String number = "";
                switch(difficulty)
                {
                    case EASY -> number = green((objectives.get(i).getId() + 1) + ". ");
                    case MEDIUM -> number = gold((objectives.get(i).getId() + 1) + ". ");
                    case HARD -> number = red((objectives.get(i).getId() + 1) + ". ");
                }

                int playerWins = GameCore.getInstance().getScore(player);
                String desc = yellow(objectives.get(i).getGameMode().getDescription());
                String name = i > playerWins ? white("???") : gold(objectives.get(i).getObjectiveName());
                GameCore.send(player, number + desc + " " + name);
            }
            return true;
        });

        registerCommand("askrestart", AskRestartCommand.getInstance());

        registerCommand("askpass", AskPassCommand.getInstance());

        registerCommand("objlist", ObjectiveListCommand.getInstance());

        registerCommand("top", (sender, command, label, args) ->
        {
            if(!(sender instanceof Player player)) return true;

            Location location = player.getLocation();

            for(int y = 256; y > 1; y--)
            {
                Location newLoc = location.clone();
                newLoc.setY(y);

                if(newLoc.clone().subtract(0D, 1D, 0D).getBlock().getType() != Material.AIR)
                {
                    player.teleport(newLoc);
                    player.sendMessage(green("Ding! Your floor, sir"));
                    break;
                }
            }

            return true;
        });
    }

    @Override
    public void onDisable()
    {
        GameCore.getInstance().unload();
    }

    private void registerListeners()
    {
        register(new LobbyHandler());
        register(new GameHandler());
        register(new PlayerHandler());
        register(ObjectiveListCommand.getInstance());
    }

    private void registerCommand(String command, CommandExecutor executor)
    {
        Objects.requireNonNull(getCommand(command)).setExecutor(executor);
    }

    private void register(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static NamespacedKey getKey(String path)
    {
        return NamespacedKey.fromString(path, INSTANCE);
    }

    public static JustDoMain getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void saveResource(String resourcePath, boolean replace)
    {
        if(new File(getDataFolder(), resourcePath).exists())
            return;

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if(!outDir.exists())
        {
            outDir.mkdirs();
        }

        try
        {
            if(!outFile.exists() || replace)
            {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
            else
            {
                getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        }
        catch(IOException ex)
        {
            getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }
}
