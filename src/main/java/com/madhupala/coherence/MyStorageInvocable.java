package com.madhupala.coherence;

import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;

/**
 * Created by vjuvvadi on 16/04/2017.
 */
public class MyStorageInvocable implements Invocable {

    private InvocationService invocationService;

    @Override
    public void init(InvocationService invocationService) {
        this.invocationService = invocationService;
    }

    @Override
    public void run() {
        System.out.println("Running on " + invocationService.getCluster().getLocalMember());
    }

    @Override
    public Object getResult() {
        return null;
    }
}
