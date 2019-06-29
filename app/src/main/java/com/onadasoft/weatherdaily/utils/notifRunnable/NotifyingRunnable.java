package com.onadasoft.weatherdaily.utils.notifRunnable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class NotifyingRunnable implements Runnable {
    private final Set<RunnableCompleteListener> listeners = new CopyOnWriteArraySet<RunnableCompleteListener>();

    public final void addListener(final RunnableCompleteListener listener){
        listeners.add(listener);
    }

    public final void removeListener(final RunnableCompleteListener listener){
        listeners.remove(listener);
    }

    private final void notifyListeners(){
        for(RunnableCompleteListener listener : listeners){
            listener.notifyOfRunnableComplete(this);
        }
    }

    @Override
    public final void run() {
        try{
            doRun();
        }finally {
            notifyListeners();
        }
    }

    public abstract void doRun();
}
