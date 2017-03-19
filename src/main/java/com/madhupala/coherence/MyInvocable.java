package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

import java.util.Map;
import java.util.Set;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class MyInvocable implements Invocable {

    private transient InvocationService service;
    private transient Object result;
    private static final long serialVersionUID = -2369438253692973245L;

    private String test;

    public MyInvocable() {
    }

    public MyInvocable(String test) {
        this.test = test;
    }

    public void init(InvocationService invocationService) {
        this.service = service;
    }

    public void run() {
        System.out.println("Running invocable on " + CacheFactory.ensureCluster().getLocalMember().getId());
        NamedCache cache = CacheFactory.getCache("compute-grid");
        Set<String> keys = (Set<String>) cache.get("keys");

        Map map = cache.invokeAll(keys, new MyEntryProcessor());

        result = map;
    }

    public Object getResult() {
        return result;
    }
}
