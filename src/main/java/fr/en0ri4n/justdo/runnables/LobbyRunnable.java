package fr.en0ri4n.justdo.runnables;

import fr.en0ri4n.justdo.config.PluginConfig;
import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.runnables.utils.BaseRunnable;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class LobbyRunnable extends BaseRunnable
{
    private static LobbyRunnable INSTANCE;

    private final List<Integer> timeMessages = Arrays.asList(20, 15, 10, 5, 4, 3, 2, 1);

    public LobbyRunnable()
    {
        super(20, 20);
    }

    @Override
    public void runTask()
    {
        if(!canCount())
            resetCounter();

        if(canCount() && PluginConfig.getInstance().isAutoStart())
        {
            if(timeMessages.contains(getCounter()))
                GameCore.broadcast(ChatColor.YELLOW + "Party will begin in " + ChatColor.GOLD + getCounter() + ChatColor.YELLOW + " second" + (!is(1) ? "s" : ""));
        }
    }

    @Override
    public boolean canCount()
    {
        return GameCore.getInstance().hasEnoughPlayer() && super.canCount();
    }

    @Override
    protected void onStop()
    {
        GameCore.getInstance().startGame();
    }

    public static void start()
    {
        INSTANCE = new LobbyRunnable();
    }

    public static LobbyRunnable getInstance()
    {
        return INSTANCE;
    }
}
