package fr.en0ri4n.justdo.runnables;

import com.mojang.datafixers.util.Pair;
import fr.en0ri4n.justdo.utils.BlockStep;
import fr.en0ri4n.justdo.utils.TaskHelper;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.BlockFace;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ConstructionRunnable extends BaseRunnable
{
    private final List<BlockStep> steps;
    private final Location base;
    private final Consumer<Location> endRunnable;

    private ConstructionRunnable(Location base, List<BlockStep> steps, Consumer<Location> endRunnable)
    {
        this.base = base;
        this.endRunnable = endRunnable;
        setCounter(steps.size() - 1);
        this.steps = steps;
    }

    @Override
    public void run()
    {
        if(isCounter(-1))
        {
            TaskHelper.cancelTask(getTaskId());
            endRunnable.accept(base);
            return;
        }

        BlockStep step = steps.get((steps.size() - 1) - getCounter());

        for(BlockStep.StepInfo info : step.getLocations())
        {
            Location loc = base.clone();
            for(Pair<BlockFace, Integer> pair : info.getLocations())
                loc.add(pair.getFirst().getDirection().multiply(pair.getSecond()));
            loc.getBlock().setType(info.getMaterial());
            Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 100F, 1F);
        }

        decreaseCounter();
    }

    public static void start(Location base, List<BlockStep> steps, Consumer<Location> end)
    {
        ConstructionRunnable runnable = new ConstructionRunnable(base, steps, end);
        runnable.setTaskId(TaskHelper.startRepeatingTask(runnable, 5L));
    }
}
