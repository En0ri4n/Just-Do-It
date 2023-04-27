package fr.en0ri4n.justdo.utils;

import fr.en0ri4n.justdo.JustDoMain;
import fr.en0ri4n.justdo.runnables.BaseRunnable;
import org.bukkit.Bukkit;

public class TaskHelper
{
    public static int startRepeatingTask(Runnable task, long period)
    {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(JustDoMain.getInstance(), task, 0L, period);
    }

    public static void cancelTask(int taskId)
    {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public static BaseRunnable getRunnable(int taskId)
    {
        return (BaseRunnable) Bukkit.getScheduler().getPendingTasks().stream().filter(task -> task.getTaskId() == taskId).findFirst().orElse(null);
    }

    public static void runAsync(Runnable task)
    {
        Bukkit.getScheduler().runTaskAsynchronously(JustDoMain.getInstance(), task);
    }

    public static void runTaskAfter(int delay, Runnable task)
    {
        Bukkit.getScheduler().runTaskLater(JustDoMain.getInstance(), task, delay);
    }

    public static void runTask(Runnable task)
    {
        Bukkit.getScheduler().runTask(JustDoMain.getInstance(), task);
    }
}
