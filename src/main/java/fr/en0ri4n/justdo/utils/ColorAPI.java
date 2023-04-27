package fr.en0ri4n.justdo.utils;

import com.google.common.collect.ImmutableMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * A utility class for coloring strings.<br>
 * From <a href="https://github.com/Iridium-Development/IridiumColorAPI">IridiumColorAPI by Iridium-Development.</a><br>
 * Exemple of use:<br>
 * <code>ColorAPI.process("&lt;GRADIENT:2C08BA>Cool string with a gradient&lt;/GRADIENT:028A97>");</code><br>
 * <code>ColorAPI.process("&lt;RAINBOW1>THIS IS A REALLY COOL Rainbow&lt;/RAINBOW>");</code>
 */
public class ColorAPI
{
    /**
     * The current version of the server in the a form of a major version.
     * If the static initialization for this fails, you know something's wrong with
     * the server software.
     *
     * @since 1.0.0
     */
    private static final int VERSION = getVersion();

    /**
     * Cached result if the server version is after the v1.16 RGB update.
     *
     * @since 1.0.0
     */
    private static final boolean SUPPORTS_RGB = VERSION >= 16;

    private static final List<String> SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");

    /**
     * Cached result of all legacy colors.
     *
     * @since 1.0.0
     */
    private static final Map<Color, ChatColor> COLORS = ImmutableMap.<Color, ChatColor>builder().put(new Color(0), ChatColor.getByChar('0')).put(new Color(170), ChatColor.getByChar('1')).put(new Color(43520), ChatColor.getByChar('2')).put(new Color(43690), ChatColor.getByChar('3')).put(new Color(11141120), ChatColor.getByChar('4')).put(new Color(11141290), ChatColor.getByChar('5')).put(new Color(16755200), ChatColor.getByChar('6')).put(new Color(11184810), ChatColor.getByChar('7')).put(new Color(5592405), ChatColor.getByChar('8')).put(new Color(5592575), ChatColor.getByChar('9')).put(new Color(5635925), ChatColor.getByChar('a')).put(new Color(5636095), ChatColor.getByChar('b')).put(new Color(16733525), ChatColor.getByChar('c')).put(new Color(16733695), ChatColor.getByChar('d')).put(new Color(16777045), ChatColor.getByChar('e')).put(new Color(16777215), ChatColor.getByChar('f')).build();

    /**
     * Cached result of patterns.
     *
     * @since 1.0.2
     */
    private static final List<Pattern> PATTERNS = Arrays.asList(new GradientPattern(), new SolidPattern(), new RainbowPattern());

    /**
     * Processes a string to add color to it.
     * Thanks to Distressing for helping with the regex <3
     *
     * @param string The string we want to process
     * @since 1.0.0
     */
    @Nonnull
    public static String process(@Nonnull String string)
    {
        for(Pattern pattern : PATTERNS)
        {
            string = pattern.process(string);
        }

        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }

    /**
     * Processes multiple strings in a collection.
     *
     * @param strings The collection of the strings we are processing
     * @return The list of processed strings
     * @since 1.0.3
     */
    @Nonnull
    public static List<String> process(@Nonnull Collection<String> strings)
    {
        return strings.stream().map(ColorAPI::process).collect(Collectors.toList());
    }

    /**
     * Colors a String.
     *
     * @param string The string we want to color
     * @param color  The color we want to set it to
     * @since 1.0.0
     */
    @Nonnull
    public static String color(@Nonnull String string, @Nonnull Color color)
    {
        return (SUPPORTS_RGB ? ChatColor.of(color) : getClosestColor(color)) + string;
    }

    /**
     * Colors a String with a gradiant.
     *
     * @param string The string we want to color
     * @param start  The starting gradiant
     * @param end    The ending gradiant
     * @since 1.0.0
     */
    @Nonnull
    public static String color(@Nonnull String string, @Nonnull Color start, @Nonnull Color end)
    {
        String originalString = string;

        ChatColor[] colors = createGradient(start, end, withoutSpecialChar(string).length());
        return apply(originalString, colors);
    }

    /**
     * Colors a String with rainbow colors.
     *
     * @param string     The string which should have rainbow colors
     * @param saturation The saturation of the rainbow colors
     * @since 1.0.3
     */
    @Nonnull
    public static String rainbow(@Nonnull String string, float saturation)
    {
        String originalString = string;

        ChatColor[] colors = createRainbow(withoutSpecialChar(string).length(), saturation);
        return apply(originalString, colors);
    }

    /**
     * Gets a color from hex code.
     *
     * @param string The hex code of the color
     * @since 1.0.0
     */
    @Nonnull
    public static ChatColor getColor(@Nonnull String string)
    {
        return SUPPORTS_RGB ? ChatColor.of(new Color(Integer.parseInt(string, 16))) : getClosestColor(new Color(Integer.parseInt(string, 16)));
    }

    /**
     * Removes all color codes from the provided String, including IridiumColorAPI
     * patterns.
     *
     * @param string The String which should be stripped
     * @return The stripped string without color codes
     * @since 1.0.5
     */
    @Nonnull
    public static String stripColorFormatting(@Nonnull String string)
    {
        return string.replaceAll("<#[0-9A-F]{6}>|[&§][a-f0-9lnokm]|<[/]?[A-Z]{5,8}(:[0-9A-F]{6})?[0-9]*>", "");
    }

    @Nonnull
    private static String apply(@Nonnull String source, ChatColor[] colors)
    {
        StringBuilder specialColors = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        String[] characters = source.split("");
        int outIndex = 0;
        for(int i = 0; i < characters.length; i++)
        {
            if(characters[i].equals("&") || characters[i].equals("§"))
            {
                if(i + 1 < characters.length)
                {
                    if(characters[i + 1].equals("r"))
                    {
                        specialColors.setLength(0);
                    }
                    else
                    {
                        specialColors.append(characters[i]);
                        specialColors.append(characters[i + 1]);
                    }
                    i++;
                }
                else stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
            }
            else stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
        }
        return stringBuilder.toString();
    }

    @Nonnull
    private static String withoutSpecialChar(@Nonnull String source)
    {
        String workingString = source;
        for(String color : SPECIAL_COLORS)
        {
            if(workingString.contains(color))
            {
                workingString = workingString.replace(color, "");
            }
        }
        return workingString;
    }

    /**
     * Returns a rainbow array of chat colors.
     *
     * @param step       How many colors we return
     * @param saturation The saturation of the rainbow
     * @return The array of colors
     * @since 1.0.3
     */
    @Nonnull
    private static ChatColor[] createRainbow(int step, float saturation)
    {
        ChatColor[] colors = new ChatColor[step];
        double colorStep = (1.00 / step);

        for(int i = 0; i < step; i++)
        {
            Color color = Color.getHSBColor((float) (colorStep * i), saturation, saturation);
            if(SUPPORTS_RGB)
            {
                colors[i] = ChatColor.of(color);
            }
            else
            {
                colors[i] = getClosestColor(color);
            }
        }

        return colors;
    }

    /**
     * Returns a gradient array of chat colors.
     *
     * @param start The starting color.
     * @param end   The ending color.
     * @param step  How many colors we return.
     * @author TheViperShow
     * @since 1.0.0
     */
    @Nonnull
    private static ChatColor[] createGradient(@Nonnull Color start, @Nonnull Color end, int step)
    {
        ChatColor[] colors = new ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
        int[] direction = new int[]{start.getRed() < end.getRed() ? +1 : -1, start.getGreen() < end.getGreen() ? +1 : -1, start.getBlue() < end.getBlue() ? +1 : -1};

        for(int i = 0; i < step; i++)
        {
            Color color = new Color(start.getRed() + ((stepR * i) * direction[0]), start.getGreen() + ((stepG * i) * direction[1]), start.getBlue() + ((stepB * i) * direction[2]));
            if(SUPPORTS_RGB)
            {
                colors[i] = ChatColor.of(color);
            }
            else
            {
                colors[i] = getClosestColor(color);
            }
        }

        return colors;
    }

    /**
     * Returns the closest legacy color from an rgb color
     *
     * @param color The color we want to transform
     * @since 1.0.0
     */
    @Nonnull
    private static ChatColor getClosestColor(Color color)
    {
        Color nearestColor = null;
        double nearestDistance = Integer.MAX_VALUE;

        for(Color constantColor : COLORS.keySet())
        {
            double distance = Math.pow(color.getRed() - constantColor.getRed(), 2) + Math.pow(color.getGreen() - constantColor.getGreen(), 2) + Math.pow(color.getBlue() - constantColor.getBlue(), 2);
            if(nearestDistance > distance)
            {
                nearestColor = constantColor;
                nearestDistance = distance;
            }
        }
        return COLORS.get(nearestColor);
    }

    /**
     * Gets a simplified major version (..., 9, 10, ..., 14).
     * In most cases, you shouldn't be using this method.
     *
     * @return the simplified major version.
     * @since 1.0.0
     */
    private static int getVersion()
    {
        String version = Bukkit.getVersion();
        // Validate.notEmpty(version, "Cannot get major Minecraft version from null or empty string");

        // getVersion()
        int index = version.lastIndexOf("MC:");
        if(index != -1)
        {
            version = version.substring(index + 4, version.length() - 1);
        }
        else if(version.endsWith("SNAPSHOT"))
        {
            // getBukkitVersion()
            index = version.indexOf('-');
            version = version.substring(0, index);
        }

        // 1.13.2, 1.14.4, etc...
        int lastDot = version.lastIndexOf('.');
        if(version.indexOf('.') != lastDot) version = version.substring(0, lastDot);

        return Integer.parseInt(version.substring(2));
    }

    public interface Pattern
    {

        /**
         * Applies this pattern to the provided String.
         * Output might me the same as the input if this pattern is not present.
         *
         * @param string The String to which this pattern should be applied to
         * @return The new String with applied pattern
         */
        String process(String string);

    }

    public static class SolidPattern implements Pattern
    {

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<SOLID:([0-9A-Fa-f]{6})>|#\\{([0-9A-Fa-f]{6})}");

        /**
         * Applies a solid RGB color to the provided String.
         * Output might me the same as the input if this pattern is not present.
         *
         * @param string The String to which this pattern should be applied to
         * @return The new String with applied pattern
         */
        public String process(String string)
        {
            Matcher matcher = pattern.matcher(string);
            while(matcher.find())
            {
                String color = matcher.group(1);
                if(color == null) color = matcher.group(2);

                string = string.replace(matcher.group(), ColorAPI.getColor(color) + "");
            }
            return string;
        }

    }

    public static class RainbowPattern implements Pattern
    {

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<RAINBOW([0-9]{1,3})>(.*?)</RAINBOW>");

        /**
         * Applies a rainbow pattern to the provided String.
         * Output might me the same as the input if this pattern is not present.
         *
         * @param string The String to which this pattern should be applied to
         * @return The new String with applied pattern
         */
        public String process(String string)
        {
            Matcher matcher = pattern.matcher(string);
            while(matcher.find())
            {
                String saturation = matcher.group(1);
                String content = matcher.group(2);
                string = string.replace(matcher.group(), ColorAPI.rainbow(content, Float.parseFloat(saturation)));
            }
            return string;
        }

    }

    public static class GradientPattern implements Pattern
    {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<GRADIENT:([0-9A-Fa-f]{6})>(.*?)</GRADIENT:([0-9A-Fa-f]{6})>");

        /**
         * Applies a gradient pattern to the provided String.
         * Output might me the same as the input if this pattern is not present.
         *
         * @param string The String to which this pattern should be applied to
         * @return The new String with applied pattern
         */
        public String process(String string)
        {
            Matcher matcher = pattern.matcher(string);
            while(matcher.find())
            {
                String start = matcher.group(1);
                String end = matcher.group(3);
                String content = matcher.group(2);
                string = string.replace(matcher.group(), ColorAPI.color(content, new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16))));
            }
            return string;
        }

    }

}