package fr.en0ri4n.justdo.utils;

import fr.en0ri4n.justdo.JustDoMain;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PersistentDataHelper
{
    public static final ItemStackFactory ITEMSTACK = new ItemStackFactory();
    public static final Factory<Container> CONTAINER = new Factory<>();
    public static final Factory<Entity> ENTITY = new Factory<>();
    public static final Factory<World> WORLD = new Factory<>();
    public static final Factory<Chunk> CHUNK = new Factory<>();

    /**
     * Get the plugin instance, used to centralize the plugin instance
     * @return the plugin instance
     */
    private static JavaPlugin getPlugin()
    {
        return JustDoMain.getInstance();
    }

    /**
     * Create a NamespacedKey with the plugin instance
     * @param key the key
     * @return the NamespacedKey
     */
    private static NamespacedKey getKey(String key)
    {
        return new NamespacedKey(getPlugin(), key);
    }

    /**
     * Factory class to add, get and check data from a PersistentDataHolder
     * @param <K> the type of the PersistentDataHolder {@link PersistentDataType}
     */
    public static class Factory<K extends PersistentDataHolder>
    {
        /**
         * Add a data to a PersistentDataHolder
         * @param dataHolder the PersistentDataHolder
         * @param dataType the PersistentDataType
         * @param key the key
         * @param value the value
         * @return the PersistentDataHolder
         * @param <T> the type of the data {@link PersistentDataType}
         */
        public <T> K addData(K dataHolder, PersistentDataType<T, T> dataType, String key, T value)
        {
            dataHolder.getPersistentDataContainer().set(getKey(key), dataType, value);
            return dataHolder;
        }

        /**
         * Get a data from a PersistentDataHolder
         * @param dataHolder the PersistentDataHolder
         * @param dataType the PersistentDataType
         * @param key the key
         * @return the data
         * @param <T> the type of the data {@link PersistentDataType}
         */
        public <T> T getData(K dataHolder, PersistentDataType<T, T> dataType, String key)
        {
            return dataHolder.getPersistentDataContainer().get(getKey(key), dataType);
        }

        /**
         * Check if a PersistentDataHolder has a data
         * @param dataHolder the PersistentDataHolder
         * @param dataType the PersistentDataType
         * @param key the key
         * @return true if the PersistentDataHolder has the data
         * @param <T> the type of the data {@link PersistentDataType}
         */
        public <T> boolean hasData(K dataHolder, PersistentDataType<T, T> dataType, String key)
        {
            return dataHolder.getPersistentDataContainer().has(getKey(key), dataType);
        }
    }

    /**
     * Factory class to add, get and check data from an ItemStack<br>
     * This class is a copy/paste of {@link Factory} with {@link ItemMeta} as PersistentDataHolder<br>
     * because the PersistentDataHolder of an ItemStack is the ItemMeta, se we need to handle the ItemMeta and not directly the ItemStack
     */
    public static class ItemStackFactory
    {
        public <T> void addData(ItemStack dataHolder, PersistentDataType<T, T> dataType, String key, T value)
        {
            ItemMeta itemMeta = getItemMeta(dataHolder);
            itemMeta.getPersistentDataContainer().set(getKey(key), dataType, value);
            dataHolder.setItemMeta(itemMeta);
        }

        public <T> T getData(ItemStack dataHolder, PersistentDataType<T, T> dataType, String key)
        {
            return getItemMeta(dataHolder).getPersistentDataContainer().get(getKey(key), dataType);
        }

        public <T> boolean hasData(ItemStack dataHolder, PersistentDataType<T, T> dataType, String key)
        {
            return getItemMeta(dataHolder).getPersistentDataContainer().has(getKey(key), dataType);
        }

        private ItemMeta getItemMeta(ItemStack itemStack)
        {
            return itemStack.getItemMeta();
        }
    }
}
