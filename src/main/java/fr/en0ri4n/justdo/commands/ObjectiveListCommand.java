package fr.en0ri4n.justdo.commands;

import fr.en0ri4n.justdo.commands.utils.BaseCommand;
import fr.en0ri4n.justdo.config.GameConfig;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.utils.JDObjectives;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static fr.en0ri4n.justdo.utils.Colors.*;

public class ObjectiveListCommand extends BaseCommand implements Listener
{
    private static final ObjectiveListCommand INSTANCE = new ObjectiveListCommand();

    private final String inventoryTitle = blue("Objectifs");
    private final List<Integer> decorationSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);

    private ObjectiveListCommand()
    {
        super("objlist");
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player player))
            return true;

        Inventory inventory = createInventory();

        player.openInventory(inventory);

        return true;
    }

    private Inventory createInventory()
    {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, inventoryTitle);

        decorationSlots.forEach(index -> inventory.setItem(index, getDecoration()));

        GameCore.getInstance().getScores().keySet().forEach(playerId -> addPlayer(inventory, playerId));

        ItemStack enoHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) enoHead.getItemMeta();
        meta.setOwner("En0ri4n");
        meta.setDisplayName(green("Un jeu développé par ") + white("En0ri4n"));
        meta.setLore(Arrays.asList(gray("N'hésitez pas à me faire des retours de bugs !")));
        enoHead.setItemMeta(meta);
        inventory.setItem(49, enoHead);

        return inventory;
    }

    private void addPlayer(Inventory inventory, UUID playerId)
    {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);

        // Create Lore
        List<String> lore = new ArrayList<>();

        lore.add("");

        List<JDObjectives.Objective> objectives = JDObjectives.getInstance().getObjectives(offlinePlayer.getPlayer());

        if(objectives == null || objectives.isEmpty()) return;

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

            int playerWins = GameCore.getInstance().getScore(offlinePlayer.getPlayer());
            String desc = yellow(objectives.get(i).getGameMode().getDescription());
            String name = i > playerWins ? white("???") : gold(objectives.get(i).getObjectiveName()) + (i == playerWins ? darkRed(" X") : green(" O"));
            lore.add(number + desc + " " + name);
        }

        lore.add("");

        lore.add(gray("(" + GameCore.getInstance().getScore(offlinePlayer.getPlayer()) + "/" + GameConfig.getInstance().getObjectivesCount() + ")"));


        // Add Lore
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwningPlayer(offlinePlayer);
        headMeta.setDisplayName(aqua(offlinePlayer.getName() + "'s Objectives"));
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);

        inventory.addItem(head);
    }

    private ItemStack getDecoration()
    {
        ItemStack itemStack = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(null);
        itemMeta.setLocalizedName(null);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if(event.getView().getTitle().equals(this.inventoryTitle))
            event.setCancelled(true);
    }

    public static ObjectiveListCommand getInstance()
    {
        return INSTANCE;
    }
}
