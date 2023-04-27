package fr.en0ri4n.justdo.listeners;

import fr.en0ri4n.justdo.JustDoMain;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.scoreboard.JDIScoreboard;
import fr.en0ri4n.justdo.utils.PersistentDataHelper;
import fr.en0ri4n.justdo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if(GameCore.isGame() && GameCore.isPlayer(player))
        {
            event.setJoinMessage(gold(player.getName()) + yellow(" has rejoined !"));
            JDIScoreboard.getInstance().updateScoreboard();
            return;
        }

        if(GameCore.isGame())
        {
            player.setGameMode(GameMode.SPECTATOR);
            event.setJoinMessage(italic().grayColor(player.getName() + " wants to spectate the game..."));
            return;
        }

        GameCore.getInstance().addPlayer(player);
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if(GameCore.isGame())
        {
            GameCore.broadcast(gold(event.getPlayer().getName()) + yellow(" has left !"));
            event.setQuitMessage(null);
            return;
        }

        GameCore.getInstance().removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null)
        {
            ItemStack heldItem = event.getItem();

            if(PersistentDataHelper.ITEMSTACK.hasData(heldItem, PersistentDataType.INTEGER, Utils.PORTAL_PLACER_TAG))
            {
                Location clickedBlock = Objects.requireNonNull(event.getClickedBlock()).getLocation();
                Utils.placePortal(clickedBlock, event.getPlayer().getFacing().getOppositeFace());
                heldItem.setAmount(heldItem.getAmount() - 1);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        Bukkit.getScheduler().runTaskLater(JustDoMain.getInstance(), () -> GameCore.getInstance().giveEffects(event.getPlayer(), true), 40L);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        event.setFormat(gold("%s") + reset().grayColor(" » ") + white("%s"));
    }
}
