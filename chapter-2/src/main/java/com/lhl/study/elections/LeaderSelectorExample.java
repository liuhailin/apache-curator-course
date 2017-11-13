package com.lhl.study.elections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.lhl.study.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

/**
 * @author Liu Hailin
 * @create 2017-11-13 下午6:43
 **/
public class LeaderSelectorExample {

    private static String latchPath = "/chapter-2/elections/leader_selector";

    private String name;

    private LeaderSelector leaderSelector;

    public LeaderSelectorExample(String name) {
        this.name = name;
    }

    public void init(CuratorFramework client) {

        leaderSelector = new LeaderSelector( client, latchPath, new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {

                System.out.println( name + " is Leader" );

                //while (true) {
                //    Thread.sleep( 3000 );
                //}

            }
        } );
        leaderSelector.autoRequeue();
        leaderSelector.start();

    }

    //Client1
    public static void main(String[] args) throws InterruptedException, IOException {

        Thread client1 = new Thread( new Runnable() {

            @Override
            public void run() {

                CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut(
                    30000 ).sessionTimeOut( 30000 ).build();

                client.start();

                LeaderSelectorExample example1 = new LeaderSelectorExample( "client1" );

                example1.init( client );

            }
        } );

        Thread client2 = new Thread( new Runnable() {

            @Override
            public void run() {

                CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut(
                    30000 ).sessionTimeOut( 30000 ).build();

                client.start();

                LeaderSelectorExample example2 = new LeaderSelectorExample( "client2" );

                example2.init( client );

            }
        } );

        client1.start();
        client2.start();

        new BufferedReader( new InputStreamReader( System.in ) ).readLine();
    }
}
