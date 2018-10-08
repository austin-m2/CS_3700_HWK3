package CS_3700;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {

    enum Color { RED, GREEN, BLUE, ORANGE; }
    static LinkedList<Color> q1 = new LinkedList<>();
    static Semaphore q1lock = new Semaphore(1, true);
    static Semaphore q1size = new Semaphore(0, true);
    static LinkedList<Color> q2 = new LinkedList<>();
    static Semaphore q2lock = new Semaphore(1, true);
    static Semaphore q2size = new Semaphore(0, true);


    public static void main(String[] args) {
        Sock_Producer red_producer = new Sock_Producer(Color.RED);
        Sock_Producer green_producer = new Sock_Producer(Color.GREEN);
        Sock_Producer blue_producer = new Sock_Producer(Color.BLUE);
        Sock_Producer orange_producer = new Sock_Producer(Color.ORANGE);
        Sock_Matcher matcher = new Sock_Matcher(red_producer, green_producer, blue_producer, orange_producer);
        Sock_Washer washer = new Sock_Washer();

        red_producer.start();
        green_producer.start();
        blue_producer.start();
        orange_producer.start();
        matcher.start();
        washer.start();
    }


    static class Sock_Producer extends Thread {
        Color color;
        final int MAX_SOCKS;
        int sock_num = 0;

        Sock_Producer(Color color /*, Semaphore lock, Semaphore size*/) {
            this.color = color;
            MAX_SOCKS = (int) (Math.random() * 100 + 1);
            //this.lock = lock;
            //this.size = size;
        }

        @Override
        public void run() {
            while (sock_num < MAX_SOCKS) {
                try {
                    sock_num += 1;
                    q1lock.acquire();
                    System.out.println(color + " SOCK: Produced " + sock_num + " of " + MAX_SOCKS + " " + color + " SOCKS");
                    q1.add(color);
                    q1lock.release();
                    q1size.release();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            return;
        }
    }

    static class Sock_Matcher extends Thread {
        final int MAX_SOCKS;
        int num_socks_matched;
        LinkedList<Color> mySocks;

        Sock_Matcher(Sock_Producer p1, Sock_Producer p2, Sock_Producer p3, Sock_Producer p4) {
            this.MAX_SOCKS = p1.MAX_SOCKS + p2.MAX_SOCKS + p3.MAX_SOCKS + p4.MAX_SOCKS;
            this.num_socks_matched = 0;
            mySocks = new LinkedList<>();
        }

        @Override
        public void run() {
            Color current_sock_color;
            while (num_socks_matched < MAX_SOCKS) {
                try {
                    //q1size.acquire();
                    if(! q1size.tryAcquire(1, TimeUnit.SECONDS)) {
                        return;
                    }
                    q1lock.acquire();
                    current_sock_color = q1.pop();
                    q1lock.release();

                    if (mySocks.contains(current_sock_color)) {
                        q2lock.acquire();
                        mySocks.remove(current_sock_color);
                        q2.add(current_sock_color);
                        q2.add(current_sock_color);
                        num_socks_matched += 2;
                        System.out.println("Matching Thread: Send " + current_sock_color + " SOCKS to Washer. Total socks " + num_socks_matched + ". Total inside queue " + mySocks.size());
                        q2lock.release();
                        q2size.release();
                    } else {
                        mySocks.add(current_sock_color);
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

            }
        }
    }

    static class Sock_Washer extends Thread {

        @Override
        public void run() {
            Color c;
            while (true) {
                try {
                    if(! q2size.tryAcquire(1, TimeUnit.SECONDS)) {
                        return;
                    }
                    q2lock.acquire();
                    c = q2.removeFirst();
                    q2.removeLastOccurrence(c);
                    System.out.println("Washer Thread: Destroyed " + c + " SOCKS");
                    q2lock.release();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}



