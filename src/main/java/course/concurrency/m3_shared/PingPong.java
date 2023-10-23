package course.concurrency.m3_shared;

public class PingPong {

    private final static Object LOCK = new Object();

    private static boolean pong;

    public static void ping() throws InterruptedException {
        while (true) {
            synchronized (LOCK) {
                while (pong) {
                    LOCK.wait();
                }
                System.out.println("Ping");
                pong = true;
                LOCK.notify();
            }
        }
    }

    public static void pong() throws InterruptedException {
        while (true) {
            synchronized (LOCK) {
                while (!pong) {
                    LOCK.wait();
                }
                System.out.println("Pong");
                pong = false;
                LOCK.notify();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                ping();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                pong();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();
    }
}
