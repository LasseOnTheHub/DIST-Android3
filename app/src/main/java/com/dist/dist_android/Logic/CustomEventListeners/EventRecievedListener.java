package com.dist.dist_android.Logic.CustomEventListeners;

import com.dist.dist_android.POJOS.EventPackage.Event;

import java.util.List;

/**
 * Created by lbirk on 15-04-2017.
 */

public interface EventRecievedListener
{
    void getResult(List<Event> events);
}
