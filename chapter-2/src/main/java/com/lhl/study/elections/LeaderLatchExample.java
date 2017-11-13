package com.lhl.study.elections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.lhl.study.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

/**
 * @author Liu Hailin
 * @create 2017-11-10 下午7:31
 **/
public class LeaderLatchExample {

    private static String latchPath = "/chapter-2/elections/leader_latch";

    private String name;

    private LeaderLatch leaderLatch;

    public LeaderLatchExample(String name) {
        this.name = name;
    }


    public boolean isLeader(){
        return leaderLatch.hasLeadership();
    }


    public void init(CuratorFramework client){
        leaderLatch = new LeaderLatch( client, latchPath );
        leaderLatch.addListener( new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println(name + " is Leader");
            }

            @Override
            public void notLeader() {
                System.out.println(name + " is not Leader");
            }
        } );
        try {
            //非阻塞
            leaderLatch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy(){
        if(null!=leaderLatch){
            try {
                leaderLatch.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Client1
    public static void main(String[] args) throws InterruptedException, IOException {

        Thread client1 = new Thread( new Runnable() {


            @Override
            public void run() {

                CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut( 30000 ).sessionTimeOut( 30000 ).build();

                client.start();

                LeaderLatchExample example1 = new LeaderLatchExample( "client1" );

                example1.init( client );

                System.out.println("client1 done");

            }
        } );


        Thread client2 = new Thread( new Runnable() {


            @Override
            public void run() {

                CuratorFramework client = ClientFactory.builder().serverString( "127.0.0.1:2181" ).connectionTimeOut( 30000 ).sessionTimeOut( 30000 ).build();

                client.start();

                LeaderLatchExample example2 = new LeaderLatchExample( "client2" );

                example2.init( client );

                System.out.println("client2 done");
            }
        } );

        client1.start();
        client2.start();

        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}
