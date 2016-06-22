package com.busperapp.util;

public interface EventBus {

    void register(Object suscriber);
    void unregister(Object suscriber);
    void post(Object event);

}
