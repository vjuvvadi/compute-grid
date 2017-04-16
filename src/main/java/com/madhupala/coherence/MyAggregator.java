package com.madhupala.coherence;

import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.AbstractAggregator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by vjuvvadi on 19/03/2017.
 */
public class MyAggregator extends AbstractAggregator implements Serializable {

    private static final long serialVersionUID = -2369438253692973245L;

    private Set<String> processedKeys = new HashSet<String>();

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
        if (isFinal) {
            Set<String> partial = (Set<String>) ((Map) o).get("partial");
            processedKeys.addAll(partial);
        } else {
            System.out.print("In process for " + o);
            InvocableMap.Entry e = (InvocableMap.Entry) o;
            processedKeys.add((String) e.getKey());
        }

    }

    protected Object finalizeResult(boolean isFinal) {
        if (isFinal) {
            return processedKeys;
        } else {
            System.out.println("Finalise with false");
            HashMap hashMap = new HashMap();
            hashMap.put("partial", processedKeys);
            return hashMap;
        }

    }

    @Override
    protected void processEntry(InvocableMap.Entry entry) {
        process(entry, false);
    }

    protected Object extractFromEntry(InvocableMap.Entry entry) {
        return entry.extract(getValueExtractor());
    }
}
