package fr.en0ri4n.justdo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility Class to manage listeners
 */
public class ListenerManager
{
    private static ListenerManager instance;

    private final JavaPlugin plugin;
    private final List<Listener> listeners;

    public ListenerManager(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
    }

    /**
     * Get all listeners
     * @return List of listeners
     */
    public List<Listener> getListeners()
    {
        return listeners;
    }

    /**
     * Add listeners
     * @param listeners Listeners to add
     */
    public ListenerManager addListeners(Listener... listeners)
    {
        Collections.addAll(this.listeners, listeners);
        return this;
    }

    /**
     * Register all listeners
     */
    public void registerListeners()
    {
        this.listeners.forEach(l -> Bukkit.getPluginManager().registerEvents(l, plugin));
    }

    /**
     * Initialize the ListenerManager instance
     * @param plugin Plugin instance
     */
    public static void create(JavaPlugin plugin)
    {
        if(instance == null)
            instance = new ListenerManager(plugin);
        else
            throw new IllegalStateException("ListenerManager instance already created");
    }

    /**
     * Get the ListenerManager instance<br>
     * <b>WARNING: </b> This method will return null if the ListenerManager instance is not created yet<br>
     * Use {@link ListenerManager#create(JavaPlugin)} to create the ListenerManager instance
     * @return ListenerManager instance
     */
    public static ListenerManager get()
    {
        return instance;
    }
}
