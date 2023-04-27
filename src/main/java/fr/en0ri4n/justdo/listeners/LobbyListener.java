package fr.en0ri4n.justdo.listeners;

import fr.en0ri4n.justdo.core.GameCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class LobbyListener implements Listener
{
    @EventHandler
    public void onPickUp(EntityPickupItemEvent event)
    {
        GameCore.cancelLobbyEvent(event);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        GameCore.cancelLobbyEvent(event);
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent event)
    {
        GameCore.cancelLobbyEvent(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        GameCore.cancelLobbyEvent(event);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event)
    {
        GameCore.cancelLobbyEvent(event);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        GameCore.cancelLobbyEvent(event);
    }
}
