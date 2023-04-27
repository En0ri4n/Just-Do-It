package fr.en0ri4n.justdo.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class BaseCommand implements CommandExecutor
{
    private final String name;

    public BaseCommand(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getAliases()
    {
        return null;
    }

    public TabCompleter getTabCompleter()
    {
        return null;
    }

    public String getPermission()
    {
        return null;
    }

    public String getPermissionMessage()
    {
        return null;
    }

    public String getUsage()
    {
        return null;
    }

    public String getDescription()
    {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        return execute(sender, command, label, args);
    }

    protected abstract boolean execute(CommandSender sender, Command command, String alias, String[] args);
}
