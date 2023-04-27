package fr.en0ri4n.justdo.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager
{
    private static CommandManager instance;

    private final List<BaseCommand> commands;

    private CommandManager()
    {
        commands = new ArrayList<>();
    }

    public List<BaseCommand> getCommands()
    {
        return commands;
    }

    public CommandManager addCommands(BaseCommand... commands)
    {
        Collections.addAll(this.commands, commands);
        return instance;
    }

    public void registerCommands()
    {
        for(BaseCommand command : instance.commands)
        {
            PluginCommand pluginCommand = Bukkit.getPluginCommand(command.getName());
            if(pluginCommand == null) throw new IllegalStateException("Command " + command.getName() + " not found");

            if(command.getAliases() != null) pluginCommand.setAliases(command.getAliases());
            if(command.getTabCompleter() != null) pluginCommand.setTabCompleter(command.getTabCompleter());
            if(command.getPermission() != null) pluginCommand.setPermission(command.getPermission());
            if(command.getPermissionMessage() != null) pluginCommand.setPermissionMessage(command.getPermissionMessage());
            if(command.getUsage() != null) pluginCommand.setUsage(command.getUsage());
            if(command.getDescription() != null) pluginCommand.setDescription(command.getDescription());

            pluginCommand.setExecutor(command);
        }
    }

    public static void create()
    {
        if(instance == null) instance = new CommandManager();
        else throw new IllegalStateException("CommandManager instance already created");
    }

    /**
     * Get the CommandManager instance<br>
     * <b>WARNING: </b> This method will return null if the CommandManager instance is not created yet<br>
     * Use {@link CommandManager#create()} to create the CommandManager instance
     *
     * @return
     */
    public static CommandManager get()
    {
        return instance;
    }
}
