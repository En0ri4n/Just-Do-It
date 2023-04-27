package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.commands.utils.BaseCommand;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.runnables.utils.BaseRunnable;
import fr.en0ri4n.justdo.runnables.utils.TaskHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class AskRestartCommand extends BaseCommand
{
    private static final AskRestartCommand INSTANCE = new AskRestartCommand();
    private int taskId;
    private AskRestartRunnable askRestartRunnable;

    private AskRestartCommand()
    {
        super("askrestart");
        this.taskId = -1;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player player))
            return false;

        if(!GameCore.isGame())
            return false;

        if(taskId == -1)
        {
            taskId = (askRestartRunnable = new AskRestartRunnable()).getTaskId();
            GameCore.send(player, green("You asked to restart the game."));
            GameCore.broadcast(green(player.getName()) + gold(" wants to restart the game."));
            GameCore.broadcast(gold("Type /askrestart to agree in the next 10 seconds"));
            askRestartRunnable.agree(player);
        }
        else
        {
            askRestartRunnable.agree(player);
        }

        return true;
    }

    public static AskRestartCommand getInstance()
    {
        return INSTANCE;
    }

    public static class AskRestartRunnable extends BaseRunnable
    {
        private final List<UUID> agreedPlayers = new ArrayList<>();

        private AskRestartRunnable()
        {
            super(10, 20);
        }
        
        @Override
        public void runTask()
        {
            if(agreedPlayers.size() >= GameCore.getInstance().getAvailablePlayersCount())
            {
                GameCore.broadcast(green("The game will restart in 10 seconds."));
                AskRestartCommand.getInstance().taskId = -1;

                TaskHelper.runTaskLater(() -> GameCore.getInstance().restartGame(), 10 * 20);

                stop();
            }
        }

        @Override
        protected void onStop()
        {
            if(is(0))
            {
                GameCore.broadcast(red("The game will not restart."));
                AskRestartCommand.getInstance().taskId = -1;
            }
        }

        public void agree(Player player)
        {
            if(agreedPlayers.contains(player.getUniqueId()))
                return;

            agreedPlayers.add(player.getUniqueId());

            GameCore.broadcast(green(player.getName()) + gold(" agreed to restart the game. ") + gray("(" + agreedPlayers.size() + "/" + Bukkit.getOnlinePlayers().size() + ")" ));
        }
    }
}