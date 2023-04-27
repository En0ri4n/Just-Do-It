package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.commands.utils.BaseCommand;
import fr.en0ri4n.justdo.config.GameConfig;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.utils.JDObjectives;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class ObjectivesCommand extends BaseCommand
{
    public ObjectivesCommand()
    {
        super("objectives");
    }

    @Override
    protected boolean execute(CommandSender sender, Command command, String alias, String[] args)
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
    }
}
