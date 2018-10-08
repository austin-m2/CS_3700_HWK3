package CS_3700;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        /*Ranker ranker = new Ranker();

	    Official[] officials = new Official[100];
        for (int i = 0; i < officials.length; i++) {
            officials[i] = new Official(i, ranker);
        }

	    ranker.start();

        for (int i = 0; i < officials.length; i++) {
            officials[i].start();
        }*/

        Election election = new Election();

        Thread ranker = new Thread(new Runnable() {
            @Override
            public void run() {
                //try {
                    election.ranker();
                /*} catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });

        Thread[] officials = new Thread[100];

        for (int i = 0; i < officials.length; i++) {
            int name = i;
            officials[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //try {
                        election.official(name);
                    /*} catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                }
            });
        }

        ranker.start();
        for (int i = 0; i < officials.length; i++) {
            officials[i].start();
        }

    }
}

class Election {
    private int currentName = 0;
    private int currentRank = Integer.MIN_VALUE;

    public void official(int name) {
        int rank = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        int leader = name;


        synchronized (this) {
            //try {
                System.out.println("\nNEW OFFICIAL!\nName: " + name + "\nRank: " + rank + "\nLeader: " + leader);
                currentName = name;
                currentRank = rank;
                notify();
            /*} catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }



    }

    public void ranker() {
        int currentLeader = 0;
        int maxRank = Integer.MIN_VALUE;

        synchronized (this) {
            while (true) {
                try {
                    wait();
                    if (currentRank > maxRank) {
                        currentLeader = currentName;
                        maxRank = currentRank;
                        System.out.println("\nNew Leader!!!\nName: " + currentLeader + "\nRank: " + maxRank);
                        notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}

/*class Official extends Thread {
    int name;
    int rank;
    int leader;
    Ranker ranker;
    Official(int name, Ranker ranker) {
        this.name = name;
        rank = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        leader = name;
        this.ranker = ranker;
        //ranker.notify();

    }

    @Override
    public void run() {
        System.out.println("\nNew Official!\nName: " + name + "\nRank: " + rank + "\nLeader: " + leader);

        sendMyInfo();

        while (true) {
            try {
                synchronized (ranker.lock) {
                    ranker.lock.wait();
                }
                //System.out.println("Interrupted!");
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        }
    }

    private synchronized void sendMyInfo() {
        ranker.getOfficialInfo(name, rank, this);
    }

    public synchronized void setNewInfo(int newLeader) {
        leader = newLeader;
    }
}

class Ranker extends Thread {
    Object lock = new Object();
    int leaderName;
    int leaderRank;

    Ranker() {
        leaderName = 0;
        leaderRank = Integer.MIN_VALUE;
    }

    public synchronized void getOfficialInfo(int name, int rank, Official official) {
        if (rank > leaderRank) {
            leaderName = name;
            leaderRank = rank;
            System.out.println("\nRANKER THREAD: New leader!\nName: " + leaderName + "\nRank: " + leaderRank);
            lock.notifyAll();
        } else {
            official.setNewInfo(leaderName);
        }
    }

    @Override
    public synchronized void run() {
      *//*  while (true) {
            try {
                this.wait();
            } catch (InterruptedException e) { }

            //a new official has notified the ranker

        }*//*
    }

}*/
