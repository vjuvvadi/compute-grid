package com.madhupala.coherence;

public class RunJavaClassInProcess
{
    public static void main(String[] args) throws Exception
    {

        InProcessCoherenceServer coherenceServer1 = InProcessCoherenceServer.builder().build();
        InProcessCoherenceServer coherenceServer2 = InProcessCoherenceServer.builder().setPort(12346).build();

        Thread.sleep(10000);

        System.out.println("Test");

    }
}