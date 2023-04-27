package fr.en0ri4n.justdo.utils;

import java.util.List;
import java.util.Random;

public class Randomizer
{
    private static final Randomizer INSTANCE = new Randomizer();

    private final Random random = new Random();

    public Random getRand()
    {
        setLocalSeed(random.nextLong());
        return random;
    }

    public Random getLocalRand()
    {
        return random;
    }

    public void setLocalSeed(long seed)
    {
        random.setSeed(seed);
    }

    public static int randomInt(Integer[] range)
    {
        return INSTANCE.getRand().nextInt(range[0], range[1]);
    }

    public static <T> T random(List<T> list)
    {
        return list.get(INSTANCE.getRand().nextInt(list.size()));
    }

    public static <T> T random(T[] list)
    {
        return list[INSTANCE.getRand().nextInt(list.length)];
    }

    public static Randomizer setSeed(long seed)
    {
        INSTANCE.setLocalSeed(seed);
        return INSTANCE;
    }

    public static Random getRandom()
    {
        return INSTANCE.getRand();
    }
}
