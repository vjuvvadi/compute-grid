package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class MyEntryProcessor extends AbstractProcessor {

    private static final long serialVersionUID = -2369438253692973245L;

    public Object process(InvocableMap.Entry entry) {
        NamedCache cache = CacheFactory.getCache("price-array-grid");
        Object o = cache.get("ent1");
        return  100;
    }

}
