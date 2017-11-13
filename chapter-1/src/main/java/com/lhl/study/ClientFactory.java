package com.lhl.study;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author Liu Hailin
 * @create 2017-11-10 下午3:52
 **/
public class ClientFactory {

    private static int DEFAULT_CONNECTION_TIME_OUT = 3000;
    private static int DEFAULT_SESSION_TIME_OUT = 6000;

    private String serverString;
    private int connectionTimeOut;
    private int sessionTimeOut;

    private RetryPolicy defaultPolicy= new ExponentialBackoffRetry( 1000, 3 );

    public ClientFactory(String serverString, String namespace, int connectionTimeOut, int sessionTimeOut) {
        this.serverString = serverString;
        this.connectionTimeOut = connectionTimeOut;
        this.sessionTimeOut = sessionTimeOut;
    }

    private ClientFactory(Builder builder) {
        this.serverString = builder.serverString;
        this.connectionTimeOut = builder.connectionTimeOut;
        this.sessionTimeOut = builder.sessionTimeOut;
    }

    public static Builder builder() {
        return new Builder();
    }

    private CuratorFramework produceClient() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString( this.serverString ).retryPolicy(
            defaultPolicy ).connectionTimeoutMs( this.connectionTimeOut ).sessionTimeoutMs(
            this.sessionTimeOut ).build();
        return client;
    }

    private CuratorFramework produceClientSimple() {
        CuratorFramework client = CuratorFrameworkFactory.newClient( this.serverString,this.sessionTimeOut,this.connectionTimeOut,defaultPolicy );
        return client;
    }

    public static class Builder {

        private String serverString;
        private int connectionTimeOut;
        private int sessionTimeOut;

        public Builder serverString(String serverString) {
            this.serverString = serverString;
            return this;
        }

        public Builder connectionTimeOut(int connectionTimeOut) {
            this.connectionTimeOut = connectionTimeOut;
            return this;
        }

        public Builder sessionTimeOut(int sessionTimeOut) {
            this.sessionTimeOut = sessionTimeOut;
            return this;
        }

        public CuratorFramework build() {
            ClientFactory factory = new ClientFactory( this );
            return factory.produceClient();
        }
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = ClientFactory.builder().serverString( "localhost:2181" ).connectionTimeOut( 30000 ).sessionTimeOut( 3000 ).build();

        client.start();

        client.blockUntilConnected();

        client.create().forPath( "/test" );

    }
}
