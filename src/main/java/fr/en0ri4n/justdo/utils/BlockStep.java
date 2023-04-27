package fr.en0ri4n.justdo.utils;

import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockStep
{
    private final ArrayList<StepInfo> locations;

    private BlockStep(Material material, List<Pair<BlockFace, Integer>> locations)
    {
        this.locations = new ArrayList<>();
        this.locations.add(StepInfo.of(material, locations));
    }

    public List<StepInfo> getLocations()
    {
        return locations;
    }

    public BlockStep with(Material material, List<Pair<BlockFace, Integer>> loc)
    {
        locations.add(StepInfo.of(material, loc));
        return this;
    }

    public BlockStep with(Material material, Pair<BlockFace, Integer> loc)
    {
        locations.add(StepInfo.of(material, Arrays.asList(loc)));
        return this;
    }

    public static BlockStep of(Material material, List<Pair<BlockFace, Integer>> locations)
    {
        return new BlockStep(material, locations);
    }

    public static BlockStep of(Material material, Pair<BlockFace, Integer> location)
    {
        return new BlockStep(material, Arrays.asList(location));
    }

    public static class StepInfo
    {
        private final Material material;
        private final List<Pair<BlockFace, Integer>> locations;

        private StepInfo(Material material, List<Pair<BlockFace, Integer>> locations)
        {
            this.material = material;
            this.locations = locations;
        }

        public Material getMaterial()
        {
            return material;
        }

        public List<Pair<BlockFace, Integer>> getLocations()
        {
            return locations;
        }

        public static StepInfo of(Material material, List<Pair<BlockFace, Integer>> locations)
        {
            return new StepInfo(material, locations);
        }
    }
}
