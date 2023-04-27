package fr.en0ri4n.justdo.runnables.utils;

import fr.en0ri4n.justdo.JustDoMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskHelper
{
    private static JavaPlugin getPlugin()
    {
        return JustDoMain.getInstance();
    }

    public static int startScheduledTask(Runnable task, long period)
    {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), task, 0L, period);
    }

    public static void runTaskLater(Runnable task, long delay)
    {
        Bukkit.getScheduler().runTaskLater(getPlugin(), task, delay);
    }

    public static void stopTask(int taskId)
    {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
