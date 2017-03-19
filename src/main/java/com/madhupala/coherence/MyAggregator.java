package com.madhupala.coherence;

import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.AbstractAggregator;

import java.io.Serializable;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class MyAggregator extends AbstractAggregator implements Serializable {

    private static final long serialVersionUID = -2369438253692973245L;

    public MyAggregator() {
        super();
    }

    public MyAggregator(String sMethod) {
        super(sMethod);
    }

    public MyAggregator(ValueExtractor extractor) {
        super(extractor);
    }

    protected void init(boolean b) {
        System.out.println("Initislising with flag" + b);
    }

    protected void process(Object o, boolean isFinal) {
        System.out.println("Calling with " + o + " and final is " + isFinal);

    }

    protected Object finalizeResult(boolean b) {
        System.out.println("Finalizing result");
        return "test";
    }

    @Override
    protected void processEntry(InvocableMap.Entry entry) {
        process(entry, false);
    }

    protected Object extractFromEntry(InvocableMap.Entry entry) {
        return entry.extract(getValueExtractor());
    }
}
