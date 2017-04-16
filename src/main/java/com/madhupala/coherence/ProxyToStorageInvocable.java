package com.madhupala.coherence;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;
import com.tangosol.net.Member;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class ProxyToStorageInvocable implements Invocable {

    private transient InvocationService service;
    private transient Object result;
    private static final long serialVersionUID = -2369438253692973245L;

    private String test;

    public ProxyToStorageInvocable() {
    }

    public ProxyToStorageInvocable(String test) {
        this.test = test;
    }

    public void init(InvocationService invocationService) {
        this.service = service;
    }

    public void run() {

        InvocationService invSvc = (InvocationService)

                CacheFactory.getConfigurableCacheFactory().ensureService("ExtendPriceArrayInvocableService");

        Set<Member> memberSet = invSvc.getCluster().getMemberSet();
        Set<Member> storageServers = memberSet.stream()
                .filter(e -> e.getRoleName().contains("storage"))
                .collect(Collectors.toSet());

        System.out.println("Running position view task on member  :: "+ storageServers);

        Map<Member, List> result = invSvc.query(new MyStorageInvocable(), storageServers);
        List resultsRemote = result.values().iterator().next();

    }

    public Object getResult() {
        return result;
    }
}
