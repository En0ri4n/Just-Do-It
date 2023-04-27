package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.commands.utils.BaseCommand;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.runnables.utils.BaseRunnable;
import fr.en0ri4n.justdo.utils.JDObjectives;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class AskPassCommand extends BaseCommand
{
    private static final AskPassCommand INSTANCE = new AskPassCommand();
    private int taskId;
    private AskPassRunnable askPassRunnable;

    private AskPassCommand()
    {
        super("askpass");
        this.taskId = -1;
    }

    @Override
    protected boolean execute(CommandSender sender, Command command, String alias, String[] args)
    {
        if(!(sender instanceof Player player))
            return false;

        if(!GameCore.isGame())
            return false;

        if(taskId == -1)
        {
            taskId = (askPassRunnable = new AskPassRunnable(player)).getTaskId();
            GameCore.send(player, green("You want to pass you current task."));
            JDObjectives.Objective objective = JDObjectives.getInstance().getCurrentObjective(player);
            String desc = yellow(objective.getGameMode().getDescription());
            String name = gold(objective.getObjectiveName());
            GameCore.broadcast(green(player.getName()) + gold(" wants to pass his current task : "));
            GameCore.broadcast(white("[") + desc + " " + name + white("]") + gold("."));
            GameCore.broadcast(gold("Type /askpass to agree in the next 10 seconds"));
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

        private AskPassRunnable(Player player)
        {
            super(10, 20);
            this.player = player;
        }

        @Override
        protected void runTask()
        {
            if(agreedPlayers.size() >= GameCore.getInstance().getAvailablePlayersCount())
            {
                GameCore.broadcast(green("All players agreed to pass the task of ") + gold(player.getName()));
                AskPassCommand.getInstance().taskId = -1;

                GameCore.getInstance().win(player);

                stop();
            }
        }

        @Override
        protected void onStop()
        {
            if(is(0))
            {
                GameCore.broadcast(red(player.getName()) + gold( " will not pass his task."));
                AskPassCommand.getInstance().taskId = -1;
            }
        }

        public void agree(Player player)
        {
            if(agreedPlayers.contains(player.getUniqueId()))
                return;

            agreedPlayers.add(player.getUniqueId());

            if(player.getUniqueId() != this.player.getUniqueId())
                GameCore.broadcast(green(player.getName()) + gold(" agreed to pass the task of ") + yellow(this.player.getName()) + gold(". ") + gray("(" + agreedPlayers.size() + "/" + Bukkit.getOnlinePlayers().size() + ")" ));
        }
    }
}