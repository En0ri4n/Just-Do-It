package fr.en0ri4n.justdo.utils;

import fr.en0ri4n.justdo.JustDoMain;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@SuppressWarnings({"ConstantConditions"})
public class NBTHelper
{
    public static <T> void set(ItemStack is, String key, T value, PersistentDataType<T, T> dataType)
    {
        ItemMeta meta = is.getItemMeta();
        meta.getPersistentDataContainer().set(JustDoMain.getKey(key), dataType, value);
        is.setItemMeta(meta);
    }

    public static <T> T get(ItemStack is, String key, PersistentDataType<T, T> dataType)
    {
        ItemMeta meta = is.getItemMeta();
        return meta.getPersistentDataContainer().get(JustDoMain.getKey(key), dataType);
    }

    public static <T> boolean has(ItemStack is, String key, PersistentDataType<T, T> dataType)
    {
        ItemMeta meta = is.getItemMeta();
        return meta.getPersistentDataContainer().has(JustDoMain.getKey(key), dataType);
    }
}
