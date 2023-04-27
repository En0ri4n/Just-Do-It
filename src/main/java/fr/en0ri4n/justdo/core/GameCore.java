package fr.en0ri4n.justdo.core;

import com.mojang.datafixers.util.Pair;
import fr.en0ri4n.justdo.config.GameConfig;
import fr.en0ri4n.justdo.config.PluginConfig;
import fr.en0ri4n.justdo.runnables.GameRunnable;
import fr.en0ri4n.justdo.runnables.utils.TaskHelper;
import fr.en0ri4n.justdo.scoreboard.JDIScoreboard;
import fr.en0ri4n.justdo.utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fr.en0ri4n.justdo.utils.Colors.*;

@SuppressWarnings("ConstantConditions")
public class GameCore
{
    private static final GameCore INSTANCE = new GameCore();

    // Current Game Datas
    private final JDObjectives objectives;
    private final Map<UUID, Integer> playerScores;
    private GameState currentGameState;

    private GameCore()
    {
        setState(GameState.LOBBY); // Ensure that the game is in LOBBY state when instancing core
        this.playerScores = new HashMap<>();
        this.objectives = JDObjectives.getInstance();
    }

    public void load()
    {
        // Setup Game Rules
        Bukkit.getWorlds().forEach(world ->
        {
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.UNIVERSAL_ANGER, true);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        });

        // Add Players to the game
        Bukkit.getOnlinePlayers().forEach(this::addPlayer);

        // Setup Lobby
        setState(GameState.LOBBY);
    }

    public void unload()
    {
        JDIScoreboard.getInstance().unregisterPlayers();
    }

    public void startGame()
    {
        broadcast(green("Starting Game..."));

        // Give Recipes
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "recipe give @a *");

        createObjectives();

        Bukkit.getOnlinePlayers().forEach(p ->
        {
            JDIScoreboard.getInstance().registerPlayer(p);
            broadcastObjectiveTo(p);
            p.setGameMode(org.bukkit.GameMode.SURVIVAL);
            p.setLevel(69);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(darkGreen("A game by ") + white("En0ri4n")));
        });

        giveStuff();

        GameRunnable.start();
    }

    private void giveStuff()
    {
        Bukkit.getOnlinePlayers().forEach(player ->
        {
            giveEffects(player, false);

            player.getInventory().clear();
            player.getInventory().addItem(ItemFactory.create(Material.DIAMOND_SWORD).unbreakable().build());
            player.getInventory().addItem(ItemFactory.create(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 2).enchant(Enchantment.LOOT_BONUS_BLOCKS, 2).build());
            player.getInventory().addItem(ItemFactory.create(Material.STONE_AXE).enchant(Enchantment.DIG_SPEED, 3).enchant(Enchantment.LOOT_BONUS_BLOCKS, 2).build());
            player.getInventory().addItem(ItemFactory.create(Material.GOLDEN_CARROT).unbreakable().amount(32).build());
            player.getInventory().addItem(Utils.getPortalPlacer());
            player.getInventory().setItem(8, Utils.getTutorialBook());

            player.getInventory().setChestplate(ItemFactory.create(Material.NETHERITE_CHESTPLATE).enchant(Enchantment.BINDING_CURSE, 10).build());
            player.getInventory().setLeggings(ItemFactory.create(Material.LEATHER_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
        });
    }

    public void giveEffects(Player player, boolean isDeath)
    {
        player.addPotionEffect(Utils.effect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        player.addPotionEffect(Utils.effect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
        player.addPotionEffect(Utils.effect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

        if(isDeath)
        {
            Pair<PotionEffect, String> deathPenalty = Utils.getRandomDeathPenalty();
            send(player, red("Death penalty : ") + deathPenalty.getSecond());
            player.addPotionEffect(deathPenalty.getFirst());
        }
    }

    public void broadcastObjectiveTo(Player player)
    {
        JDObjectives.Objective objective = objectives.getObjective(player, playerScores.get(player.getUniqueId()));

        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(darkAqua("You need to ")).append(aqua(objective.getGameMode().getDescription()));
        componentBuilder.append(" ");
        TranslatableComponent translation = getTranslateName(objective);
        translation.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        componentBuilder.append(translation);

        send(player, componentBuilder);
    }

    public void createObjectives()
    {
        for(UUID playerId : playerScores.keySet())
        {
            Player player = Bukkit.getPlayer(playerId);

            Pair<Pair<List<GameMode>, List<GameConfig.ObjectiveDifficulty>>, List<String>> generatedObjectives = GameConfig.getInstance().getRandoms();

            for(int i = 0; i < generatedObjectives.getSecond().size(); i++)
            {
                objectives.addObjective(player, generatedObjectives.getSecond().get(i), generatedObjectives.getFirst().getFirst().get(i), generatedObjectives.getFirst().getSecond().get(i));
            }
        }
    }

    public boolean hasEnoughPlayer()
    {
        return Bukkit.getOnlinePlayers().size() >= PluginConfig.getInstance().getMinimumPlayers();
    }

    public int getAvailablePlayersCount()
    {
        return Math.toIntExact(playerScores.keySet().stream().filter(id -> Bukkit.getPlayer(id) != null).count());
    }

    // Handle Player Join and Quit
    public void addPlayer(Player player)
    {
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);

        player.getInventory().clear();
        player.getInventory().addItem(Utils.getTutorialBook());

        playerScores.put(player.getUniqueId(), 0);

        JDObjectives.getInstance().create(player);

        broadcast(yellow(player.getName() + " has joined the party ! ") + gray("(" + Bukkit.getOnlinePlayers().size() + "/" + PluginConfig.getInstance().getMinimumPlayers() + ")"));
    }

    public void removePlayer(Player player)
    {
        playerScores.remove(player.getUniqueId());

        JDObjectives.getInstance().remove(player);

        broadcast(gold(player.getName() + " has left the party ! ") + gray("(" + (Bukkit.getOnlinePlayers().size() - 1) + "/" + PluginConfig.getInstance().getMinimumPlayers() + ")"));
    }

    public void updateScoreboard()
    {
        JDIScoreboard.getInstance().updateScoreboard();
    }

    // Handle Win
    public void win(Player player)
    {
        JDObjectives.Objective currentObjective = objectives.getObjective(player, playerScores.get(player.getUniqueId()));

        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(green(player.getName())).append(aqua(" achieved to "));
        componentBuilder.append(darkAqua(getGameMode(player).getDescription()));
        componentBuilder.append(" ");
        TranslatableComponent translation = getTranslateName(currentObjective);
        translation.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        componentBuilder.append(translation);
        broadcast(componentBuilder);

        playerScores.put(player.getUniqueId(), playerScores.containsKey(player.getUniqueId()) ? playerScores.get(player.getUniqueId()) + 1 : 1);

        updateScoreboard();

        // Win Game
        if(playerScores.get(player.getUniqueId()) >= GameConfig.getInstance().getObjectivesCount())
        {
            setState(GameState.ENDING);
            GameCore.broadcast(bold().lightPurpleColor(player.getName()) + darkPurple(" won !"));
            Bukkit.getOnlinePlayers().forEach(p ->
            {
                p.playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 10F, 1F);
                p.teleport(player);
                p.setGameMode(org.bukkit.GameMode.SPECTATOR);
            });

            TaskHelper.runTaskLater(() -> Bukkit.getOnlinePlayers().forEach(p -> BungeeHelper.teleportPlayerTo(player, "lobby")), 8 * 20);

            TaskHelper.runTaskLater(this::restartGame, 10 * 20);

            return;
        }

        broadcastObjectiveTo(player);
    }

    public void restartGame()
    {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }

    public static TranslatableComponent getTranslateName(JDObjectives.Objective objective)
    {
        String prefix;
        if(GameConfig.getInstance().isBlock(objective.getObjectiveName())) prefix = "block";
        else if(GameConfig.getInstance().isItem(objective.getObjectiveName())) prefix = "item";
        else prefix = "entity";
        return new TranslatableComponent(prefix + ".minecraft." + objective.getObjectiveName().split(":")[1]);
    }

    public Map<UUID, Integer> getScores()
    {
        return this.playerScores;
    }

    // State
    public void setState(GameState currentGameState)
    {
        this.currentGameState = currentGameState;
    }

    public GameState getState()
    {
        return this.currentGameState;
    }

    public boolean isState(GameState state)
    {
        return state == getState();
    }

    // Mode
    public GameMode getGameMode(Player player)
    {
        if(!playerScores.containsKey(player.getUniqueId())) return null;

        JDObjectives.Objective obj = objectives.getObjective(player, playerScores.get(player.getUniqueId()));
        if(obj == null) return null;
        return obj.getGameMode();
    }

    public boolean isMode(Player player, GameMode gameMode)
    {
        return gameMode == getGameMode(player);
    }

    public boolean isItem(Player player, ItemStack itemStack)
    {
        return getCurrentObjectiveName(player).equals(CraftHelper.getRegistryName(itemStack));
    }

    public boolean isEntity(Player player, EntityType entity)
    {
        return getCurrentObjectiveName(player).equals(CraftHelper.getRegistryName(entity));
    }

    public boolean isBlock(Player player, Block block)
    {
        return getCurrentObjectiveName(player).equals(CraftHelper.getRegistryName(block));
    }

    protected String getCurrentObjectiveName(Player player)
    {
        return objectives.getObjective(player, playerScores.get(player.getUniqueId())).getObjectiveName();
    }

    public int getScore(Player player)
    {
        if(player == null) return -1;

        return playerScores.get(player.getUniqueId());
    }

    // Utils
    public static void send(Player player, String message)
    {
        player.sendMessage(getPrefix() + message);
    }

    private static String getPrefix()
    {
        return bold().darkRedColor("Just") + bold().redColor("Do") + bold().goldColor("It") + gray(" » ");
    }

    public static void broadcast(String message)
    {
        Bukkit.broadcastMessage(getPrefix() + message);
    }

    public static void broadcast(ComponentBuilder builder)
    {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        TextComponent component = new TextComponent(getPrefix());
        component.setBold(true);
        componentBuilder.append(component);
        componentBuilder.append(builder.create());

        Bukkit.spigot().broadcast(componentBuilder.create());
    }

    public static void send(Player player, ComponentBuilder builder)
    {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        TextComponent component = new TextComponent(getPrefix());
        component.setBold(true);
        componentBuilder.append(component);
        componentBuilder.append(builder.create());

        player.spigot().sendMessage(componentBuilder.create());
    }

    // Static Utils
    public static boolean isGame()
    {
        return INSTANCE.isState(GameState.IN_GAME);
    }

    public static boolean isLobby()
    {
        return INSTANCE.isState(GameState.LOBBY);
    }

    public static boolean isEnd()
    {
        return INSTANCE.isState(GameState.ENDING);
    }

    public static boolean isPlayer(Player player)
    {
        return getInstance().playerScores.containsKey(player.getUniqueId());
    }

    public static void cancelLobbyEvent(Cancellable cancellable)
    {
        if(isLobby() || isEnd()) cancellable.setCancelled(true);
    }

    // Instance
    public static GameCore getInstance()
    {
        return INSTANCE;
    }

    public enum GameMode
    {
        PLACE("placeable", "place", GameModeEntry.BLOCK),
        STAND("walkable", "stand on", GameModeEntry.BLOCK),
        BREAK("breakable", "break", GameModeEntry.BLOCK),

        PICKUP("pickable", "pickup", GameModeEntry.ITEM),
        CRAFT("craftable", "craft", GameModeEntry.ITEM),

        KILL("killable", "kill", GameModeEntry.ENTITY);

        private final String configName;
        private final String description;
        private final GameModeEntry entryType;

        GameMode(String configName, String description, GameModeEntry entryType)
        {
            this.configName = configName;
            this.description = description;
            this.entryType = entryType;
        }

        public String getConfigName()
        {
            return configName;
        }

        public String getDescription()
        {
            return description;
        }

        public GameModeEntry getEntryType()
        {
            return entryType;
        }
    }

    public enum GameModeEntry
    {
        BLOCK,
        ITEM,
        ENTITY;

        public boolean is(GameModeEntry gameModeEntry)
        {
            return this == gameModeEntry;
        }
    }

    public enum GameState
    {
        LOBBY,
        STARTING,
        IN_GAME,
        ENDING
    }
}
