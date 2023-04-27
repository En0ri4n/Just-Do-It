package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.runnables.BaseRunnable;
import fr.en0ri4n.justdo.utils.JDObjectives;
import fr.en0ri4n.justdo.utils.TaskHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class AskPassCommand implements CommandExecutor
{
    private static final AskPassCommand INSTANCE = new AskPassCommand();
    private int taskId;
    private AskPassRunnable askPassRunnable;

    private AskPassCommand()
    {
        this.taskId = -1;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player player))
            return false;

        if(!GameCore.isGame())
            return false;

        if(taskId == -1)
        {
            taskId = TaskHelper.startRepeatingTask(askPassRunnable = new AskPassRunnable(player), 20L);
            GameCore.send(player, green("You want to pass you current task."));
            JDObjectives.Objective objective = JDObjectives.getInstance().getCurrentObjective(player);
            String desc = yellow(objective.getGameMode().getDescription());
            String name = gold(objective.getObjectiveName());
            GameCore.broadcast(green(player.getName()) + gold(" wants to pass his current task ") + white("[") + desc + " " + name + white("]") + gold(". Type /askpass to agree in the next 10 seconds"));
            askPassRunnable.agree(player);
        }
        else
        {
            askPassRunnable.agree(player);
        }

        return true;
    }

    public static AskPassCommand getInstance()
    {
        return INSTANCE;
    }

    public static class AskPassRunnable extends BaseRunnable
    {
        private final List<UUID> agreedPlayers = new ArrayList<>();
        private final Player player;
        private int time;

        private AskPassRunnable(Player player)
        {
            this.player = player;
            this.time = 10;
        }
        
        @Override
        public void run()
        {
            if(time == 0)
            {
                TaskHelper.cancelTask(AskPassCommand.getInstance().taskId);
                GameCore.broadcast(red(player.getName()) + gold( " will not pass his task."));
                AskPassCommand.getInstance().taskId = -1;
                return;
            }

            if(agreedPlayers.size() >= GameCore.getInstance().getAvailablePlayersCount())
            {
                GameCore.broadcast(green("All players agreed to pass the task of ") + gold(player.getName()));

                TaskHelper.cancelTask(AskPassCommand.getInstance().taskId);
                AskPassCommand.getInstance().taskId = -1;

                GameCore.getInstance().win(player);
                return;
            }

            time--;
        }

        public void agree(Player player)
        {
            if(agreedPlayers.contains(player.getUniqueId()))
                return;

            agreedPlayers.add(player.getUniqueId());
            GameCore.broadcast(green(player.getName()) + gold(" agreed to pass the task of ") + yellow(this.player.getName()) + gold(". ") + gray("(" + agreedPlayers.size() + "/" + Bukkit.getOnlinePlayers().size() + ")" ));
        }
    }
}