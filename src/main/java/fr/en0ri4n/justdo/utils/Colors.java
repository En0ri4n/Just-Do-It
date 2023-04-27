package fr.en0ri4n.justdo.utils;

import org.bukkit.ChatColor;

/**
 * Utility class for Spigot Colors<br>
 * Used to simplify the use of colored strings
 */
@SuppressWarnings("unused")
public class Colors
{
    private static final Colors BOLD = new Colors(ChatColor.BOLD);
    private static final Colors ITALIC = new Colors(ChatColor.ITALIC);
    private static final Colors UNDERLINE = new Colors(ChatColor.UNDERLINE);
    private static final Colors STRIKE = new Colors(ChatColor.STRIKETHROUGH);
    private static final Colors OBFUSCATED = new Colors(ChatColor.MAGIC);
    private static final Colors RESET = new Colors(ChatColor.RESET);

    private final ChatColor colorEffect;

    private Colors(ChatColor colorEffect)
    {
        this.colorEffect = colorEffect;
    }

    public String yellowColor(String str)
    {
        return String.valueOf(ChatColor.YELLOW) + colorEffect + str;
    }

    public String redColor(String str)
    {
        return String.valueOf(ChatColor.RED) + colorEffect + str;
    }

    public String greenColor(String str)
    {
        return String.valueOf(ChatColor.GREEN) + colorEffect + str;
    }

    public String blueColor(String str)
    {
        return String.valueOf(ChatColor.BLUE) + colorEffect + str;
    }

    public String grayColor(String str)
    {
        return String.valueOf(ChatColor.GRAY) + colorEffect + str;
    }

    public String whiteColor(String str)
    {
        return String.valueOf(ChatColor.WHITE) + colorEffect + str;
    }

    public String blackColor(String str)
    {
        return String.valueOf(ChatColor.BLACK) + colorEffect + str;
    }

    public String darkRedColor(String str)
    {
        return String.valueOf(ChatColor.DARK_RED) + colorEffect + str;
    }

    public String darkGreenColor(String str)
    {
        return String.valueOf(ChatColor.DARK_GREEN) + colorEffect + str;
    }

    public String darkBlueColor(String str)
    {
        return String.valueOf(ChatColor.DARK_BLUE) + colorEffect + str;
    }

    public String darkGrayColor(String str)
    {
        return String.valueOf(ChatColor.DARK_GRAY) + colorEffect + str;
    }

    public String aquaColor(String str)
    {
        return String.valueOf(ChatColor.AQUA) + colorEffect + str;
    }

    public String goldColor(String str)
    {
        return String.valueOf(ChatColor.GOLD) + colorEffect + str;
    }

    public String lightPurpleColor(String str)
    {
        return String.valueOf(ChatColor.LIGHT_PURPLE) + colorEffect + str;
    }

    public String darkAquaColor(String str)
    {
        return String.valueOf(ChatColor.DARK_AQUA) + colorEffect + str;
    }

    public String darkPurpleColor(String str)
    {
        return String.valueOf(ChatColor.DARK_PURPLE) + colorEffect + str;
    }



    public static String yellow(String str)
    {
        return ChatColor.YELLOW + str;
    }

    public static String red(String str)
    {
        return ChatColor.RED + str;
    }

    public static String green(String str)
    {
        return ChatColor.GREEN + str;
    }

    public static String blue(String str)
    {
        return ChatColor.BLUE + str;
    }

    public static String gray(String str)
    {
        return ChatColor.GRAY + str;
    }

    public static String white(String str)
    {
        return ChatColor.WHITE + str;
    }

    public static String black(String str)
    {
        return ChatColor.BLACK + str;
    }

    public static String darkRed(String str)
    {
        return ChatColor.DARK_RED + str;
    }

    public static String darkGreen(String str)
    {
        return ChatColor.DARK_GREEN + str;
    }

    public static String darkBlue(String str)
    {
        return ChatColor.DARK_BLUE + str;
    }

    public static String darkGray(String str)
    {
        return ChatColor.DARK_GRAY + str;
    }

    public static String aqua(String str)
    {
        return ChatColor.AQUA + str;
    }

    public static String darkAqua(String str)
    {
        return ChatColor.DARK_AQUA + str;
    }

    public static String darkPurple(String str)
    {
        return ChatColor.DARK_PURPLE + str;
    }

    public static String lightPurple(String str)
    {
        return ChatColor.LIGHT_PURPLE + str;
    }

    public static String gold(String str)
    {
        return ChatColor.GOLD + str;
    }



    public static Colors bold()
    {
        return BOLD;
    }

    public static Colors italic()
    {
        return ITALIC;
    }

    public static Colors underline()
    {
        return UNDERLINE;
    }

    public static Colors strike()
    {
        return STRIKE;
    }

    public static Colors obfuscated()
    {
        return OBFUSCATED;
    }

    public static Colors reset()
    {
        return RESET;
    }
}
