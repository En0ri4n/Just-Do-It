package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.commands.utils.BaseCommand;
import fr.en0ri4n.justdo.runnables.LobbyRunnable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StartCommand extends BaseCommand
{
    public StartCommand()
    {
        super("start");
    }

    @Override
    protected boolean execute(CommandSender sender, Command command, String alias, String[] args)
    {
        if(!sender.isOp()) return true;

        LobbyRunnable.getInstance().stop();
        return true;
    }
}
