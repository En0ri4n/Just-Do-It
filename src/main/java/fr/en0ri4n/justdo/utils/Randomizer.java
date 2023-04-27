package fr.en0ri4n.justdo.utils;

import java.util.List;
import java.util.Random;

public class Randomizer
{
    private static final Random random = new Random();

    /**
     * Choose a random integer between the two first values of the array
     * @param range the array of two integers representing the range
     * @return a random integer between the two first values of the array
     */
    public static int randomInt(Integer[] range)
    {
        return random.nextInt(range[0], range[1]);
    }

    /**
     * Choose a random element from a list
     * @param list the list to choose from
     * @return a random element from the list
     */
    public static <T> T random(List<T> list)
    {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Like {@link #randomInt(Integer[])} but with a list
     * @param list the list of two integers representing the range
     * @return a random integer between the two first values of the list
     */
    public static Integer randomRange(List<Integer> list)
    {
        return random.nextInt(list.get(0), list.get(1));
    }

    /**
     * Like {@link #random(List)} but with an array
     * @param array the array to choose from
     * @return a random element from the array
     */
    public static <T> T random(T[] array)
    {
        return array[random.nextInt(array.length)];
    }

    /**
     * Choose a random integer between the two values
     * @param min the minimum value
     * @param max the maximum value
     * @return a random integer between the two values
     */
    public static int random(int min, int max)
    {
        return random.nextInt(min, max);
    }

    /**
     * Get the random instance used by this class
     */
    public static Random getRandom()
    {
        return random;
    }
}
