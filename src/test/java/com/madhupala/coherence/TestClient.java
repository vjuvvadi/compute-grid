package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestClient {

    public static final String COMPUTE_GRID = "compute-grid";
    public static final String PRICE_ARRAY_GRID = "price-array-grid";

    public static void main(String[] asArgs)
            throws Throwable {
        callEntryProcessor();
    }

    private static void callInvocable() {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID);

        Set<String> keys = new HashSet();
        for (int i=0; i<10; i++) {
            String key = "ent" + i;
            cache.put(key, Integer.valueOf(i));
            keys.add(key);
        }


        cache.put("keys", keys);
        InvocationService service = (InvocationService)
                CacheFactory.getConfigurableCacheFactory()
                        .ensureService("ComputeGridCalcProxyService");

        Map map = service.query(new MyInvocable("abc"), null);

        System.out.print("The value of the key is " + map);
    }

    public static void callAggregator() throws InterruptedException {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID);

        Set<String> keys = new HashSet();
        for (int i=0; i<10; i++) {
            String key = "ent" + i;
            cache.put(key, Integer.valueOf(i));
            keys.add(key);
        }

        cache.put("keys", keys);


        Object aggregate = cache.aggregate(keys, new MyAggregator());


        System.out.print("The value of the aggregate is " + aggregate);
    }

    public static void callEntryProcessor() throws InterruptedException {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID);
        NamedCache priceArrayCache = CacheFactory.getCache(PRICE_ARRAY_GRID);

        Set<String> keys = new HashSet();
        for (int i=0; i<10; i++) {
            String key = "ent" + i;
            priceArrayCache.put(key, Integer.valueOf(i));
            keys.add(key);
        }

        priceArrayCache.put("keys", keys);

        Object aggregate = cache.invoke(null, new MyEntryProcessor());


        System.out.print("The value of the aggregate is " + aggregate);
    }


}