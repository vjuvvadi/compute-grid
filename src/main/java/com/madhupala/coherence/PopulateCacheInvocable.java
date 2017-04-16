package com.madhupala.coherence;

import com.tangosol.net.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class PopulateCacheInvocable implements Invocable, InvocationObserver {

    private transient InvocationService service;
    private transient Object result;
    private static final long serialVersionUID = -2369438253692973245L;

    private String test;

    public PopulateCacheInvocable() {
    }


    public void init(InvocationService invocationService) {
        this.service = service;
    }

    public void run() {
        System.out.print("Invoked on ");
        System.out.println("Running invocable on " + CacheFactory.ensureCluster().getLocalMember().getId());
        NamedCache cache = CacheFactory.getCache(CacheNames.STORAGE_GRID_SERVICE);

        Set<String> keys = new HashSet();

        for (int i=0; i<10000; i++) {
            cache.put("key" + i, i);
            keys.add("key" + i);
        }

        cache.put("keys", keys);

        result = keys;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public void memberCompleted(Member member, Object o) {

    }

    @Override
    public void memberFailed(Member member, Throwable throwable) {

    }

    @Override
    public void memberLeft(Member member) {

    }

    @Override
    public void invocationCompleted() {

    }
}
