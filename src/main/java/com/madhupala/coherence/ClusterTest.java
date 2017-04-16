package com.madhupala.coherence;

import com.oracle.tools.deferred.Eventually;
import com.oracle.tools.runtime.coherence.*;
import com.oracle.tools.runtime.console.SystemApplicationConsole;
import com.oracle.tools.runtime.java.JavaVirtualMachine;
import com.oracle.tools.runtime.java.options.HeapSize;
import com.oracle.tools.runtime.network.AvailablePortIterator;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheFactoryBuilder;
import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.run.xml.XmlDocument;
import com.tangosol.run.xml.XmlElement;
import com.tangosol.run.xml.XmlHelper;
import com.tangosol.run.xml.XmlValue;
import org.junit.*;

import javax.management.ObjectName;
import java.util.List;
import java.util.Properties;

import static com.oracle.tools.deferred.DeferredHelper.eventually;
import static com.oracle.tools.deferred.DeferredHelper.invoking;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ClusterTest {
    private static CoherenceCluster cluster;

    private ConfigurableCacheFactory clientCacheFactory;

    @BeforeClass
    public static void startCluster() throws Exception {
        AvailablePortIterator ports = new AvailablePortIterator(40000);

        CoherenceClusterMemberSchema storage = createStorageNodeSchema(ports);
        CoherenceClusterMemberSchema extend = createExtendProxySchema(ports);



        CoherenceClusterBuilder clusterBuilder = new CoherenceClusterBuilder();
        clusterBuilder.addSchema("Data", storage, 2,
                JavaVirtualMachine.getInstance(), HeapSize.useDefaults());
        clusterBuilder.addSchema("Proxy", extend, 1,
                JavaVirtualMachine.getInstance(), HeapSize.useDefaults());


        cluster = clusterBuilder.realize(new SystemApplicationConsole());
    }

    @Before
    public void setupTest() throws Exception {
        // Assert the cluster is ready
        Assert.assertThat(cluster, is(notNullValue()));
        Eventually.assertThat(eventually(invoking(cluster).getClusterSize()), is(3));

        // Assert the Proxy Service is running
        assertServiceIsRunning("Proxy-1", "TcpProxyService");

        // Get the extend port the proxy is using
        CoherenceClusterMember proxyNode = cluster.get("Proxy-1");
        String extendPort = proxyNode.getSystemProperty("tangosol.coherence.extend.port");

        // Create the client properties
        Properties properties = new Properties(System.getProperties());
        properties.setProperty("tangosol.coherence.extend.port", extendPort);

        // Create the client Cache Factory
        clientCacheFactory = createClientCacheFactory("client-cache-config.xml", properties);
    }

    @Test
    public void shouldPutDataIntoCache() throws Exception {

        NamedCache cache = clientCacheFactory.ensureCache("dist-test", null);
        cache.put("Key-1", "Value-1");
        Assert.assertThat((String) cache.get("Key-1"), is("Value-1"));
    }

    @After
    public void shutdownClient() {
        if (clientCacheFactory != null) {
            CacheFactory.getCacheFactoryBuilder().release(clientCacheFactory);
            clientCacheFactory = null;
        }
    }

    @AfterClass
    public static void stopCluster() {
        if (cluster != null) {
            cluster.close();
            cluster = null;
        }
    }

    public static CoherenceClusterMemberSchema createStorageNodeSchema(AvailablePortIterator ports) {
        return createCommonSchema(ports).setStorageEnabled(true);
    }

    public static CoherenceClusterMemberSchema createExtendProxySchema(AvailablePortIterator ports) {
        return createCommonSchema(ports)
                .setStorageEnabled(false)
                .setSystemProperty("tangosol.coherence.extend.enabled", true)
                .setSystemProperty("tangosol.coherence.extend.port", ports);
    }

    public static CoherenceCacheServerSchema createCommonSchema(AvailablePortIterator ports) {

        return new CoherenceCacheServerSchema()
                .setCacheConfigURI("coherence-cache-config.xml")
                .setPofEnabled(true)
                .setPofConfigURI("pof-config.xml")
                .setClusterPort(12345)
                .setJMXPort(ports)
                .setJMXManagementMode(JMXManagementMode.LOCAL_ONLY);

    }

    private void assertServiceIsRunning(String memberName, String serviceName) throws Exception {
        Assert.assertThat(cluster, is(notNullValue()));
        CoherenceClusterMember member = cluster.get(memberName);
        int nodeId = member.getLocalMemberId();
        ObjectName objectName = new ObjectName(String.format("Coherence:type=Service,name=%s,nodeId=%d", serviceName, nodeId));
        Eventually.assertThat(eventually(invoking(member).getMBeanAttribute(objectName, "Running", Boolean.class)), is(true));
    }

    private ConfigurableCacheFactory createClientCacheFactory(String cacheConfigurationURI, Properties properties) {
        XmlDocument clientConfigXml = XmlHelper.loadFileOrResource(cacheConfigurationURI, "Client");
        replacePropertiesInXml(clientConfigXml, "system-property", properties);

        CacheFactoryBuilder cacheFactoryBuilder = CacheFactory.getCacheFactoryBuilder();
        cacheFactoryBuilder.setCacheConfiguration(cacheConfigurationURI, null, clientConfigXml);
        return cacheFactoryBuilder.getConfigurableCacheFactory(cacheConfigurationURI, null);
    }

    @SuppressWarnings("unchecked")
    private void replacePropertiesInXml(XmlElement xml, String propertyAttributeName, Properties properties) {
        XmlValue attribute = xml.getAttribute(propertyAttributeName);
        if (attribute != null) {
            xml.setAttribute(propertyAttributeName, null);
            try {
                String propertyValue = properties.getProperty(attribute.getString());
                if (propertyValue != null) {
                    xml.setString(propertyValue);
                }
            } catch (Exception _ignored) {
                // ignored on purpose
            }
        }

        for (XmlElement child : (List<XmlElement>) xml.getElementList()) {
            replacePropertiesInXml(child, propertyAttributeName, properties);
        }
    }

    @Test
    public void testOne() {
        System.out.println("One");
    }


}