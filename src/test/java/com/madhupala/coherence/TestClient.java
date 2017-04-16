package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.Member;
import com.tangosol.net.NamedCache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.madhupala.coherence.CacheNames.COMPUTE_GRID_SERVICE;

public class TestClient {



    public static void main(String[] asArgs)
            throws Throwable {
        populateCache();
        callEntryProcessor();
    }

    public static void testSimple() {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID_SERVICE);
        Set<String> keys = new HashSet();
        for (int i=0; i<10; i++) {
            String key = "ent" + i;
            cache.put(key, Integer.valueOf(i));
            keys.add(key);
        }


        cache.put("keys", keys);
        InvocationService service = (InvocationService)
                CacheFactory.getConfigurableCacheFactory()
                        .ensureService("InvocationService");

        Map map = service.query(new ProxyToStorageInvocable("abc"), null);

        System.out.print("The value of the key is " + map);
    }

    private static void callInvocable() {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID_SERVICE);

        InvocationService service = (InvocationService)
                CacheFactory.getConfigurableCacheFactory()
                        .ensureService("ExtendPriceArrayInvocableService");

        Map map = service.query(new ProxyToEntryProcessorInvocable("abc"), null);

        System.out.print("The value of the key is " + map);
    }

    private static void populateCache() {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID_SERVICE);

        Set<String> keys = new HashSet();
        for (int i=0; i<10; i++) {
            String key = "ent" + i;
            cache.put(key, Integer.valueOf(i));
            keys.add(key);
        }


        cache.put("keys", keys);
        InvocationService service = (InvocationService)
                CacheFactory.getConfigurableCacheFactory()
                        .ensureService("ExtendPriceArrayInvocableService");

        PopulateCacheInvocable populateCacheInvocable = new PopulateCacheInvocable();
        Set<Member> memberSet = cache.getCacheService().getCluster().getMemberSet();

        Set<Member> proxies = memberSet.stream().filter(e -> e.getRoleName().contains("proxy")).collect(Collectors.toSet());

        service.query(populateCacheInvocable, null);

        System.out.print("The value of the key is ");
    }

    public static void callAggregator() throws InterruptedException {
        NamedCache cache = CacheFactory.getCache(COMPUTE_GRID_SERVICE);

        Set<String> keys = new HashSet();
        for (int i=0; i<1000; i++) {
            String key = "ent" + i;
            cache.put(key, Integer.valueOf(i));
            keys.add(key);
        }

        cache.put("keys", keys);

        Object aggregate = cache.aggregate(keys, new MyAggregator());

        System.out.print("The value of the aggregate is " + aggregate);
    }

    public static void callEntryProcessor() throws InterruptedException {
        NamedCache storageGridCache = CacheFactory.getCache(CacheNames.STORAGE_GRID_SERVICE);
        Object aggregate = storageGridCache.invoke(null, new MyEntryProcessor());

        System.out.print("The value of the aggregate is " + aggregate);
    }




}