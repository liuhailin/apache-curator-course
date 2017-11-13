package com.lhl.study.elections;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

/**
 * @author Liu Hailin
 * @create 2017-11-10 下午7:31
 **/
public class LeaderLatchExample {

    private static String latchPath = "/chapter-2/elections/leader";

    private LeaderLatch leaderLatch;

    public void init(CuratorFramework client) throws Exception {

        leaderLatch = new LeaderLatch( client, latchPath );

        leaderLatch.start();
    }





}
