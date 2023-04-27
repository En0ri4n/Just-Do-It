package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.commands.utils.BaseCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.en0ri4n.justdo.utils.Colors.green;

public class TopCommand extends BaseCommand
{
    public TopCommand()
    {
        super("top");
    }

    @Override
    protected boolean execute(CommandSender sender, Command command, String alias, String[] args)
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
    }
}
