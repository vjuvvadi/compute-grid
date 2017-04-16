package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class ProxyToEntryProcessorInvocable implements Invocable {

    private transient InvocationService service;
    private transient Object result;
    private static final long serialVersionUID = -2369438253692973245L;

    private String test;

    public ProxyToEntryProcessorInvocable() {
    }

    public ProxyToEntryProcessorInvocable(String test) {
        this.test = test;
    }

    public void init(InvocationService invocationService) {
        this.service = service;
    }

    public void run() {

        NamedCache storageGridCache = CacheFactory.getCache(CacheNames.STORAGE_GRID_SERVICE);
        Object aggregate = storageGridCache.invoke(null, new MyEntryProcessor());

    }

    public Object getResult() {
        return result;
    }
}
