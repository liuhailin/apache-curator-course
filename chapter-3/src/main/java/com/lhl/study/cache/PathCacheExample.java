package com.lhl.study.cache;

import javax.sound.midi.Soundbank;

import com.lhl.study.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @author Liu Hailin
 * @create 2017-11-15 下午4:55
 **/
public class PathCacheExample {

    private String path = "/chapter-3/path-cache";

    private PathChildrenCache pathChildrenCache;

    public void init(CuratorFramework client,boolean cacheData){
        pathChildrenCache = new PathChildrenCache(client,path,cacheData);

        pathChildrenCache.getListenable().addListener( new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();

                System.out.println(new String(childData.getData()));
                System.out.println("respond="+event);
            }
        } );
        try {
            pathChildrenCache.start();
            //pathChildrenCache.start( StartMode.BUILD_INITIAL_CACHE );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut(
            30000 ).sessionTimeOut( 30000 ).build();
        client.start();
        PathCacheExample pathCacheExample = new PathCacheExample();

        //client.create().creatingParentsIfNeeded().forPath( pathCacheExample.path,"".getBytes() );

        client.blockUntilConnected();


        pathCacheExample.init( client,true );

        while (true){
            Thread.sleep( 3000 );
        }

    }


}
