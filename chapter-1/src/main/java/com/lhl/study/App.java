package com.lhl.study;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient( "localhost:2181",new ExponentialBackoffRetry(1000,3) );

        client.start();


        byte[] result = client.getConfig().forEnsemble();

        System.out.println(new String(result));

    }
}
