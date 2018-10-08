package CS_3700;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        Ranker ranker = new Ranker();
	    Official official = new Official(1, ranker);


    }
}

class Official extends Thread {
    int name;
    int rank;
    int leader;
    Ranker ranker;
    Official(int name, Ranker ranker) {
        this.name = name;
        rank = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        leader = name;
        this.ranker = ranker;
        System.out.println("New Official!");
        System.out.println("Name: " + name);
        System.out.println("Rank: " + rank);
        System.out.println("Leader: " + leader);
        ranker.notify();

    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                ranker.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Ranker extends Thread {
    Object lock = new Object();
    int leaderName;
    int leaderRank;


    @Override
    public synchronized void run() {
        while (true) {
            try {
                this.wait();
            } catch (InterruptedException e) { }

            //a new official has notified the ranker
            this.
        }
    }

}
