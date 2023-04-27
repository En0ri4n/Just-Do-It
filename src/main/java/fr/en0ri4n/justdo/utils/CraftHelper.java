package fr.en0ri4n.justdo.utils;

import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class CraftHelper
{
    public static IRegistry<Item> getItemRegistry()
    {
        return BuiltInRegistries.i;
    }

    public static RegistryBlocks<Block> getBlockRegistry()
    {
        return BuiltInRegistries.f;
    }

    public static String getRegistryName(ItemStack itemStack)
    {
        Item nmsItem = CraftItemStack.asNMSCopy(itemStack).c();
        return getItemRegistry().b(nmsItem).toString();
    }

    public static String getRegistryName(org.bukkit.block.Block block)
    {
        Block nmsBlock = ((CraftBlock) block).getNMS().b();
        return getBlockRegistry().b(nmsBlock).toString();
    }

    public static String getRegistryName(EntityType entity)
    {
        return entity.getKey().toString();
    }
}
