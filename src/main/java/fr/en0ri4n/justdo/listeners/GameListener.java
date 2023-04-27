package fr.en0ri4n.justdo.listeners;

import fr.en0ri4n.justdo.core.GameCore;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameListener implements Listener
{
    @EventHandler
    public void onPickUp(EntityPickupItemEvent event)
    {
        if(event.getEntity() instanceof Player player && GameCore.isGame())
        {
            if(GameCore.getInstance().isMode(player, GameCore.GameMode.PICKUP) && GameCore.getInstance().isItem(player, event.getItem().getItemStack()))
                GameCore.getInstance().win(player);
        }
    }

    @EventHandler
    public void onPlayerInventory(InventoryInteractEvent event)
    {
        if(GameCore.isGame())
            if(GameCore.getInstance().isMode((Player) event.getWhoClicked(), GameCore.GameMode.PICKUP))
                if(Arrays.stream(event.getWhoClicked().getInventory().getContents()).anyMatch(is -> GameCore.getInstance().isItem((Player) event.getWhoClicked(), is)))
                    GameCore.getInstance().win((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event)
    {
        if(GameCore.isGame())
            if(GameCore.getInstance().isMode(event.getPlayer(), GameCore.GameMode.PLACE))
                if(GameCore.getInstance().isBlock(event.getPlayer(), event.getBlock()))
                    GameCore.getInstance().win(event.getPlayer());
    }

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event)
    {
        if(GameCore.isGame())
            if(GameCore.getInstance().isMode(event.getPlayer(), GameCore.GameMode.BREAK))
                if(GameCore.getInstance().isBlock(event.getPlayer(), event.getBlock()))
                    GameCore.getInstance().win(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(GameCore.isGame() && GameCore.getInstance().isMode(event.getPlayer(), GameCore.GameMode.STAND))
        {
            Block currentBlock = event.getFrom().getBlock();
            Block bottomBlock = event.getFrom().add(0, -1, 0).getBlock();

            if(GameCore.getInstance().isBlock(event.getPlayer(), currentBlock) || GameCore.getInstance().isBlock(event.getPlayer(), bottomBlock))
                GameCore.getInstance().win(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event)
    {
        if(GameCore.isGame())
            if(GameCore.getInstance().isMode((Player) event.getWhoClicked(), GameCore.GameMode.CRAFT))
                if(GameCore.getInstance().isItem((Player) event.getWhoClicked(), event.getRecipe().getResult()))
                    GameCore.getInstance().win((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onPlayerSmelt(FurnaceExtractEvent event)
    {
        if(GameCore.isGame())
            if(GameCore.getInstance().isMode(event.getPlayer(), GameCore.GameMode.CRAFT))
                if(GameCore.getInstance().isItem(event.getPlayer(), new ItemStack(event.getItemType())))
                    GameCore.getInstance().win(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event)
    {
        if(event.getEntity().getKiller() != null)
        {
            Player player = event.getEntity().getKiller();

            if(GameCore.getInstance().isMode(player, GameCore.GameMode.KILL) && GameCore.getInstance().isEntity(player, event.getEntityType()))
                GameCore.getInstance().win(player);
        }
    }

    @EventHandler
    public void onEvent(FurnaceStartSmeltEvent event)
    {
        event.setTotalCookTime(60);
    }

    @EventHandler
    public void onPopulate(ChunkPopulateEvent event)
    {
        Arrays.stream(event.getChunk().getEntities()).filter(e -> e.getType() != EntityType.PLAYER).forEach(entity -> event.getWorld().spawnEntity(entity.getLocation(), entity.getType()));
    }
}
