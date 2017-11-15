package com.lhl.study.cache;

import com.lhl.study.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * @author Liu Hailin
 * @create 2017-11-15 下午5:37
 **/
public class NodeCacheExample {
    private String path = "/chapter-3/node-cache";

    private NodeCache nodeCache;

    public void init(CuratorFramework client){
        nodeCache = new NodeCache( client ,path);
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        nodeCache.getListenable().addListener( new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData childData = nodeCache.getCurrentData();
                if(childData!=null){
                    System.out.println("node changed data="+new String(childData.getData()));
                }else {
                    System.out.println("delete node");
                }

            }
        } );

    }

    public static void main(String[] args) throws InterruptedException {

        CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut(
            30000 ).sessionTimeOut( 30000 ).build();
        client.start();
        NodeCacheExample nodeCacheExample = new NodeCacheExample();
        nodeCacheExample.init( client );

        while (true){
            Thread.sleep( 3000 );
        }

    }

}
