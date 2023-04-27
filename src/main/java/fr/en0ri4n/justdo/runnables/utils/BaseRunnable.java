package fr.en0ri4n.justdo.runnables.utils;

import fr.en0ri4n.justdo.runnables.utils.TaskHelper;

public abstract class BaseRunnable implements Runnable
{
    private int counter;
    private final int taskId;
    private final int maxCounter;
    private final boolean increaseCounter;

    /**
     * Start a repeating task with a counter that decrease
     * @param counter The counter
     * @param delay The delay between each execution (in ticks)
     */
    public BaseRunnable(int counter, int delay)
    {
        this.counter = counter;
        this.maxCounter = counter;
        this.taskId = TaskHelper.startScheduledTask(this, delay);
        this.increaseCounter = false;
    }

    /**
     * Start a repeating task with a counter that increase
     * @param delay The delay between each execution (in ticks)
     */
    public BaseRunnable(int delay)
    {
        this.counter = 0;
        this.maxCounter = -1;
        this.taskId = TaskHelper.startScheduledTask(this, delay);
        this.increaseCounter = true;
    }

    /**
     * Stop the task
     */
    public void stop()
    {
        TaskHelper.stopTask(taskId);
        onStop();
    }

    /**
     * Run the task<br>
     * If the counter is equal to 0, the task will be stopped (in the decrease counter case)
     */
    @Override
    public void run()
    {
        if(!increaseCounter && getCounter() <= 0)
            stop();

        runTask();

        if(canCount())
        {
            if (increaseCounter)
                increaseCounter();
            else
                decreaseCounter();
        }
    }

    protected abstract void runTask();

    protected abstract void onStop();

    /**
     * Reset the counter to default value (Max counter or -1 if the counter increase)
     */
    protected void resetCounter()
    {
        counter = maxCounter;
    }

    /**
     * Decrease the counter by 1
     */
    protected void decreaseCounter()
    {
        counter--;
    }

    /**
     * Increase the counter by 1
     */
    protected void increaseCounter()
    {
        counter++;
    }

    /**
     * Get the counter
     * @return The counter
     */
    public int getCounter()
    {
        return counter;
    }

    /**
     * Check if the counter is equal to the given number
     * @param count The number to compare
     * @return True if the counter is equal to the given number
     */
    public boolean is(int count)
    {
        return getCounter() == count;
    }

    /**
     * Check if the counter can count
     * @return True if the counter can count
     */
    public boolean canCount()
    {
        return increaseCounter || getCounter() > 0;
    }

    /**
     * Set the counter to the given number
     * @param count The number to set
     */
    public void setCounter(int count)
    {
        this.counter = count;
    }

    /**
     * Get the task id
     * @return The task id
     */
    public int getTaskId()
    {
        return taskId;
    }

    /**
     * Get the total counter value (-1 if the counter increase)
     * @return The total counter value
     */
    public int getMaxCounter()
    {
        return maxCounter;
    }
}
