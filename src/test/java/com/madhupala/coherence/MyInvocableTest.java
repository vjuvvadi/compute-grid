package com.madhupala.coherence;

import com.tangosol.net.AbstractInvocable;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import org.junit.Test;

import java.util.Map;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class MyInvocableTest {
    @Test
    public void testInvocable() {


        NamedCache cache = CacheFactory.getCache("dist-extend");
        Integer IValue = (Integer) cache.get("key");
        if (IValue == null) {
            IValue = new Integer(1);
        } else {
            IValue = new Integer(IValue.intValue() + 1);
        }
        cache.put("key", IValue);




        InvocationService service = (InvocationService)
                CacheFactory.getConfigurableCacheFactory()
                        .ensureService("ComputeGridProxyService");

        Map map = service.query(new AbstractInvocable() {
            public void run() {
                setResult(CacheFactory.getCache("dist-extend").get("key"));
            }
        }, null);

        Integer IValue1 = (Integer) map.get(service.getCluster().
                getLocalMember());
        System.out.print("The value of the key is " + IValue1);


    }


}