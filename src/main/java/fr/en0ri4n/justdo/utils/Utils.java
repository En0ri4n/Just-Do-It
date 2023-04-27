package fr.en0ri4n.justdo.utils;

import com.mojang.datafixers.util.Pair;
import fr.en0ri4n.justdo.runnables.ConstructionRunnable;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftMetaBook;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.en0ri4n.justdo.utils.Colors.*;

@SuppressWarnings("ConstantConditions")
public class Utils
{
    public static final String PORTAL_PLACER_TAG = "portal_placer";

    public static final List<Pair<PotionEffect, String>> DEATH_PENALTIES = Arrays.asList(
            Pair.of(effect(PotionEffectType.SLOW, 120 * 20, 3), gray("Tout doux le loup")),
            Pair.of(effect(PotionEffectType.SLOW_DIGGING, 30 * 20, 3), gold("Un mineur fatigué")),
            Pair.of(effect(PotionEffectType.DARKNESS, 90 * 20, 5), darkPurple("Un réveil compliqué")),
            Pair.of(effect(PotionEffectType.HUNGER, 60 * 20, 4), yellow("Un jeûne venu de nulle part")),
            Pair.of(effect(PotionEffectType.WEAKNESS, 120 * 20, 3), blue("En avril ne te découvre pas d'un fil")));

    public static PotionEffect effect(PotionEffectType effectType, int time, int level)
    {
        return new PotionEffect(effectType, time, level);
    }

    public static ItemStack getTutorialBook()
    {
        ItemStack bookStack = new ItemStack(Material.WRITTEN_BOOK);
        CraftMetaBook bookMeta = (CraftMetaBook) bookStack.getItemMeta();
        bookMeta.setTitle(red("Tutorial Book"));
        bookMeta.setAuthor("En0ri4n");

        List<String> pages = new ArrayList<>();

        pages.add("""
                      §4Just §cDo §6It§r

                §1Règles du jeu §r:
                Terminer les objectifs le plus vites possible avant les autres joueurs.

                §2Commandes §r:
                /objectives §7§o(page 2)§r
                /objlist §7§o(page 3)§r
                /askpass §7§o(page 4)§r
                /askrestart §7§o(page 5)§r
                /top §7§o(page 6)§r
                """);

        pages.add(green("        Objectives\n") + "\n" + underline().goldColor("Alias") + reset().whiteColor(" : ") + italic().grayColor("/obj\n") + "\n" + red("Fonction :\n") + "§rPermet de voir la liste de ses objectifs. Seuls les objectifs validés et l'actuel peuvent être vu.§r");
        pages.add(green("         ObjList\n") + "\n" + underline().goldColor("Alias") + reset().whiteColor(" : ") + italic().grayColor("/ol\n") + "\n" + red("Fonction :\n") + "§rPermet de voir les objectifs de tous les joueurs à la manière de la commande §aObjectives§r.§r");
        pages.add(green("         AskPass\n") + "\n" + underline().goldColor("Alias") + reset().whiteColor(" : ") + italic().grayColor("/ap\n") + "\n" + red("Fonction :\n") + "§rPermet de demander aux autres joueurs de passer son objectif actuel, si tout le monde effectue la commande, l'objectif sera validé automatiquement pour le joueur§r");
        pages.add(green("        AskRestart\n") + "\n" + underline().goldColor("Alias") + reset().whiteColor(" : ") + italic().grayColor("/ar\n") + "\n" + red("Fonction :\n") + "§rPermet de demander aux autres joueurs de redémarrer le jeu et le serveur (en cas de bug). Si tout le monde effectue la commande, le serveur et le jeu redémarreront§r");
        pages.add(green("           Top\n") + "\n" + underline().goldColor("Alias") + reset().whiteColor(" : ") + "§oX§r\n" + "\n" + red("Fonction :\n") + "§rPermet de se téléporter au plus haut point de ses coordonnées (e.g. à la surface si le joueur est dans une grotte.§r");

        pages.add(darkRed("Additional Informations\n\n") + reset().whiteColor("- All recipes given\n- Smelt time of 3s\n- Death Penalties\n- Nether Portal Egg\n- Any skills just luck"));

        bookMeta.setPages(pages);
        bookStack.setItemMeta(bookMeta);

        return bookStack;
    }

    public static void placePortal(Location clickedBlock, BlockFace playerFacing)
    {
        List<BlockStep> steps = getNetherPortal(isFacing(playerFacing, BlockFace.NORTH, BlockFace.SOUTH));

        clearSpace(clickedBlock, 3, 4, 1, isFacing(playerFacing, BlockFace.NORTH, BlockFace.SOUTH));
        clickedBlock.getWorld().playSound(clickedBlock, Sound.BLOCK_STONE_BREAK, SoundCategory.AMBIENT, 100F, 1F);

        ConstructionRunnable.start(clickedBlock, steps, loc -> loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_WARN, SoundCategory.AMBIENT, 10F, 1F));
    }

    private static void clearSpace(Location base, int widthSize, int heightSize, int depthSize, boolean isNorthFacing)
    {
        BlockFace side = isNorthFacing ? BlockFace.EAST : BlockFace.NORTH;
        BlockFace depth = isNorthFacing ? BlockFace.NORTH : BlockFace.EAST;

        Location start = base.getBlock()
                .getRelative(BlockFace.UP)
                .getRelative(side, widthSize)
                .getRelative(depth, depthSize).getLocation();
        Location end = base.getBlock()
                .getRelative(BlockFace.UP)
                .getRelative(side.getOppositeFace(), widthSize)
                .getRelative(depth.getOppositeFace(), depthSize)
                .getRelative(BlockFace.UP, heightSize).getLocation();

        int minX = Math.min(start.getBlockX(), end.getBlockX());
        int maxX = Math.max(start.getBlockX(), end.getBlockX());
        int minY = Math.min(start.getBlockY(), end.getBlockY());
        int maxY = Math.max(start.getBlockY(), end.getBlockY());
        int minZ = Math.min(start.getBlockZ(), end.getBlockZ());
        int maxZ = Math.max(start.getBlockZ(), end.getBlockZ());

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                {
                    Location loc = new Location(start.getWorld(), x, y, z);
                    loc.getBlock().setType(Material.AIR);
                }
    }

    private static List<BlockStep> getNetherPortal(boolean isNorthFacing)
    {
        List<BlockStep> steps = new ArrayList<>();
        
        BlockFace right = isNorthFacing ? BlockFace.EAST : BlockFace.NORTH;
        BlockFace left = right.getOppositeFace();

        steps.add(BlockStep.of(Material.OBSIDIAN, Pair.of(BlockFace.UP, 0)));
        steps.add(BlockStep.of(Material.OBSIDIAN, Pair.of(right, 1)).with(Material.OBSIDIAN, Pair.of(left, 1)));
        steps.add(BlockStep.of(Material.OBSIDIAN, Pair.of(right, 2)).with(Material.OBSIDIAN, Pair.of(left, 2)));

        steps.add(BlockStep.of(Material.OBSIDIAN, Arrays.asList(Pair.of(right, 2), Pair.of(BlockFace.UP, 1))).with(Material.OBSIDIAN, Arrays.asList(Pair.of(left, 2), Pair.of(BlockFace.UP, 1))));
        steps.add(BlockStep.of(Material.OBSIDIAN, Arrays.asList(Pair.of(right, 2), Pair.of(BlockFace.UP, 2))).with(Material.OBSIDIAN, Arrays.asList(Pair.of(left, 2), Pair.of(BlockFace.UP, 2))));
        steps.add(BlockStep.of(Material.OBSIDIAN, Arrays.asList(Pair.of(right, 2), Pair.of(BlockFace.UP, 3))).with(Material.OBSIDIAN, Arrays.asList(Pair.of(left, 2), Pair.of(BlockFace.UP, 3))));
        steps.add(BlockStep.of(Material.OBSIDIAN, Arrays.asList(Pair.of(right, 2), Pair.of(BlockFace.UP, 4))).with(Material.OBSIDIAN, Arrays.asList(Pair.of(left, 2), Pair.of(BlockFace.UP, 4))));

        steps.add(BlockStep.of(Material.OBSIDIAN, Arrays.asList(Pair.of(right, 1), Pair.of(BlockFace.UP, 4))).with(Material.OBSIDIAN, Arrays.asList(Pair.of(left, 1), Pair.of(BlockFace.UP, 4))));
        steps.add(BlockStep.of(Material.OBSIDIAN, Pair.of(BlockFace.UP, 4)));
        steps.add(BlockStep.of(Material.FIRE, Pair.of(BlockFace.UP, 1)));

        // 78987
        // 6   6
        // 5   5
        // 4 X 4
        // 32123

        return steps;
    }

    public static Pair<PotionEffect, String> getRandomDeathPenalty()
    {
        return Randomizer.random(DEATH_PENALTIES);
    }

    public static ItemStack getPortalPlacer()
    {
        ItemStack stack = ItemFactory.create(Material.HOGLIN_SPAWN_EGG).name(gold("Portal Placer")).enchant(Enchantment.ARROW_INFINITE, 10).build();
        PersistentDataHelper.ITEMSTACK.addData(stack, PersistentDataType.INTEGER, PORTAL_PLACER_TAG, 1);

        return stack;
    }

    private static boolean isFacing(BlockFace from, BlockFace... sources)
    {
        boolean facing = false;

        for(BlockFace blockFace : sources)
            facing = facing || isFrom(from, blockFace);

        return facing;
    }

    private static boolean isFrom(BlockFace from, BlockFace source)
    {
        return switch(from)
                {
                    case NORTH, NORTH_EAST, NORTH_WEST, NORTH_NORTH_WEST, NORTH_NORTH_EAST -> source == BlockFace.NORTH;
                    case EAST, EAST_NORTH_EAST, EAST_SOUTH_EAST -> source == BlockFace.EAST;
                    case SOUTH, SOUTH_EAST, SOUTH_WEST, SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST -> source == BlockFace.SOUTH;
                    case WEST, WEST_NORTH_WEST, WEST_SOUTH_WEST -> source == BlockFace.WEST;
                    default -> false;
                };
    }
}
