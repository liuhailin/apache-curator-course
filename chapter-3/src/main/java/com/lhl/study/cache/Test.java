package com.lhl.study.cache;

import com.lhl.study.ClientFactory;
import org.apache.curator.framework.CuratorFramework;

/**
 * @author Liu Hailin
 * @create 2017-11-15 下午5:28
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut(
            30000 ).sessionTimeOut( 30000 ).build();
        client.start();

        //client.create().creatingParentsIfNeeded().forPath( "/chapter-3/path-cache/test","123".getBytes() );
        //client.delete().forPath("/chapter-3/path-cache/test" );


        //client.create().creatingParentsIfNeeded().forPath( "/chapter-3/node-cache","sfasdfasf".getBytes() );
        //
        //Thread.sleep( 3000 );
        //client.delete().forPath( "/chapter-3/node-cache");



        client.create().creatingParentsIfNeeded().forPath( "/chapter-3/tree-cache/abc");
        client.create().creatingParentsIfNeeded().forPath( "/chapter-3/tree-cache/abc/123");


        client.delete().deletingChildrenIfNeeded().forPath( "/chapter-3/tree-cache/abc/123");

    }
}
