package com.madhupala.coherence;

import com.oracle.tools.runtime.console.SystemApplicationConsole;
import com.oracle.tools.runtime.java.ContainerBasedJavaApplicationBuilder;
import com.oracle.tools.runtime.java.JavaApplication;
import com.oracle.tools.runtime.java.JavaVirtualMachine;
import com.oracle.tools.runtime.java.SimpleJavaApplicationSchema;

/**
 * Created by vjuvvadi on 16/04/2017.
 */
public class InProcessCoherenceServer {

    private final JavaApplication application;


    public InProcessCoherenceServer(JavaApplication application) {
        this.application = application;
    }

    public void start() {

    }

    public void stop() {
        application.close();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String classpath = System.getProperty("java.class.path");
        private String coherenceConfigFile = "coherence-cache-server-config.xml";
        private int port = 12345;
        private String name = "Default";

        public Builder setCoherenceConfigResource(String coherenceConfigFile) {
            this.coherenceConfigFile = this.getClass().getClassLoader()
                    .getResource(coherenceConfigFile).getFile();
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setClasspath(String classpath) {
            this.classpath = classpath;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public InProcessCoherenceServer build() {

            final SimpleJavaApplicationSchema schema =
                    new SimpleJavaApplicationSchema("com.tangosol.net.DefaultCacheServer", classpath)
                            .setSystemProperty("tangosol.coherence.cacheconfig", coherenceConfigFile)
                            .setSystemProperty("tangosol.coherence.clusterport", port);

            final ContainerBasedJavaApplicationBuilder builder =
                    new ContainerBasedJavaApplicationBuilder(JavaVirtualMachine.getInstance());

            final JavaApplication application = builder.realize(schema, name, new SystemApplicationConsole());

            return new InProcessCoherenceServer(application);
        }
    }

}
