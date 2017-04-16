package com.madhupala.coherence;

import com.oracle.tools.runtime.LocalPlatform;
import com.oracle.tools.runtime.SimpleApplication;
import com.oracle.tools.runtime.SimpleApplicationBuilder;
import com.oracle.tools.runtime.SimpleApplicationSchema;
import com.oracle.tools.runtime.console.SystemApplicationConsole;

public class RunSomethingForked
{
    public static void main(String[] args) throws Exception
    {
        SimpleApplicationSchema schema =
                new SimpleApplicationSchema("java")
                    .setArgument("-version");
 
        SimpleApplicationBuilder builder
                = new SimpleApplicationBuilder(LocalPlatform.getInstance());
 
        SimpleApplication application
                = builder.realize(schema, "Java", new SystemApplicationConsole());
 
        int exitCode = application.waitFor();
    }
}