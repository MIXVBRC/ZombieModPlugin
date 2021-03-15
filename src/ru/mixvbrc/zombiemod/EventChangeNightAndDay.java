package ru.mixvbrc.zombiemod;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class EventChangeNightAndDay extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final Calculations calculations = new Calculations();
    private final double time;

    public EventChangeNightAndDay(double time)
    {
        this.time = time;
    }

    public boolean hasDay()
    {
        return this.time >= this.calculations.getStartDayTime() || this.time < this.calculations.getStartNightTime();
    }

    public boolean hasNight()
    {
        return this.time >= this.calculations.getStartNightTime() || this.time < this.calculations.getStartDayTime();
    }

    public double getTime()
    {
        return this.time;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlerList;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }
}
