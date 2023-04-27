package fr.en0ri4n.justdo.runnables;

import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.utils.TaskHelper;

public class GameRunnable implements Runnable
{
    private GameRunnable()
    {
    }

    @Override
    public void run()
    {
    }

    public static void start()
    {
        // Set Game State
        GameCore.getInstance().setState(GameCore.GameState.IN_GAME);

        TaskHelper.startRepeatingTask(new GameRunnable(), 20L);
    }
}
