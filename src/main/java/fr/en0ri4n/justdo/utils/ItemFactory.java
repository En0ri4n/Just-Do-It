package fr.en0ri4n.justdo.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemFactory
{
    private final ItemStack itemStack;

    private ItemFactory(Material itemMaterial)
    {
        this.itemStack = new ItemStack(itemMaterial);
    }

    /**
     * Set the {@link ItemStack} unbreakable
     */
    public ItemFactory unbreakable()
    {
        ItemMeta meta = getMeta();
        meta.setUnbreakable(true);
        setMeta(meta);

        return this;
    }

    /**
     * Set the amount of the {@link ItemStack}
     * @param amount The amount to set
     */
    public ItemFactory amount(int amount)
    {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Set the display name of the {@link ItemStack}
     * @param name The name to set
     */
    public ItemFactory name(String name)
    {
        ItemMeta meta = getMeta();
        meta.setDisplayName(name);
        setMeta(meta);

        return this;
    }

    /**
     * Set the lore of the {@link ItemStack}
     * @param lore The lore to set
     */
    public ItemFactory lore(List<String> lore)
    {
        ItemMeta meta = getMeta();
        meta.setLore(lore);
        setMeta(meta);

        return this;
    }

    /**
     * Add an enchantment to the {@link ItemStack}
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     */
    public ItemFactory enchant(Enchantment enchantment, int level)
    {
        ItemMeta meta = getMeta();
        meta.addEnchant(enchantment, level, true);
        setMeta(meta);

        return this;
    }

    /**
     * Add a {@link ItemFlag} to the {@link ItemStack}
     * @param flags The flags to add
     */
    public ItemFactory addFlag(ItemFlag... flags)
    {
        ItemMeta meta = getMeta();
        meta.addItemFlags(flags);
        setMeta(meta);

        return this;
    }

    /**
     * Get the final {@link ItemStack}
     * @return The final ItemStack with all modifications
     */
    public ItemStack build()
    {
        return itemStack;
    }

    /**
     * Get the actual {@link ItemMeta} of the {@link ItemStack}
     * @return the ItemMeta of the ItemStack
     */
    private ItemMeta getMeta()
    {
        return itemStack.getItemMeta();
    }

    /**
     * Set the {@link ItemMeta} of the {@link ItemStack}
     * @param itemMeta The ItemMeta to set
     */
    private void setMeta(ItemMeta itemMeta)
    {
        itemStack.setItemMeta(itemMeta);
    }

    public static ItemFactory create(Material item)
    {
        return new ItemFactory(item);
    }
}
