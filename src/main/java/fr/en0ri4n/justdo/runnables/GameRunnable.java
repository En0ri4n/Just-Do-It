package fr.en0ri4n.justdo.runnables;

import fr.en0ri4n.justdo.core.GameCore;
import fr.en0ri4n.justdo.runnables.utils.BaseRunnable;

public class GameRunnable extends BaseRunnable
{
    private GameRunnable()
    {
        super(20);
        // Set Game State
        GameCore.getInstance().setState(GameCore.GameState.IN_GAME);
    }

    @Override
    protected void runTask()
    {

    }

    @Override
    protected void onStop()
    {

    }

    public static void start()
    {
        new GameRunnable();
    }
}
