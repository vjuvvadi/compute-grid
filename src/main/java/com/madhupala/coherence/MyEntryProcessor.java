package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;

import java.lang.management.ManagementFactory;
import java.util.Random;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class MyEntryProcessor extends AbstractProcessor {

    private static final long serialVersionUID = -2369438253692973245L;

    public Object process(InvocableMap.Entry entry) {

        NamedCache cache = CacheFactory.getCache(CacheNames.COMPUTE_GRID_SERVICE);

        System.out.println("Process id" + ManagementFactory.getRuntimeMXBean().getName());

        System.out.println("Entry processor running on: " + cache.getCacheService().getCluster().getLocalMember());

        int sum = 0;
        for (int i=0; i<10000;i++) {
            sum  += (Integer)cache.get("ent1");
        }

        int nextInt = new Random().nextInt();

        System.out.println("Returning" + nextInt);

        return sum;
    }

}
