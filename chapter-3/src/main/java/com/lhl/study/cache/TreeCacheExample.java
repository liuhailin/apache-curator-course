package com.lhl.study.cache;

import com.lhl.study.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * @author Liu Hailin
 * @create 2017-11-15 下午6:41
 **/
public class TreeCacheExample {
    private String path = "/chapter-3/tree-cache";

    private TreeCache treeCache;

    private void init(CuratorFramework client) {

        treeCache = new TreeCache( client, path );

        treeCache.getListenable().addListener( new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {

                System.out.println( event );

            }
        } );
        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut(
            30000 ).sessionTimeOut( 30000 ).build();
        client.start();
        TreeCacheExample nodeCacheExample = new TreeCacheExample();
        nodeCacheExample.init( client );

        while (true) {
            Thread.sleep( 3000 );
        }
    }
}
