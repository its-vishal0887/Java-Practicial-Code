class ChatUser extends Thread {

    private boolean suspended = false;

    ChatUser(String name, int priority) {

        super(name);

        setPriority(priority);
    }

    public void run() {

        try {

            for (int i = 1; i <= 5; i++) {

                synchronized (this) {

                    while (suspended) {
                        wait();
                    }
                }

                System.out.println(getName() + " : Sending Message " + i);

                Thread.sleep(1000);
            }
        }

        catch (InterruptedException e) {

            System.out.println(getName() + " interrupted");
        }

        System.out.println(getName() + " chat ended");
    }

    // Suspend Thread
    synchronized void suspendUser() {

        suspended = true;
    }

    // Resume Thread
    synchronized void resumeUser() {

        suspended = false;

        notify();
    }

    // Stop Thread
    void stopUser() {

        interrupt();
    }
}

public class ChatSystem {

    public static void main(String[] args) {

        ChatUser user1 = new ChatUser("User-1", Thread.NORM_PRIORITY);

        ChatUser user2 = new ChatUser("HighPriorityUser", Thread.MAX_PRIORITY);

        // Start Threads
        user1.start();
        user2.start();

        // Check Thread Status
        System.out.println("Is User-1 alive? " + user1.isAlive());

        System.out.println("Is HighPriorityUser alive? " + user2.isAlive());

        try {

            // Suspend user1
            Thread.sleep(2000);

            System.out.println("\nSuspending User-1...\n");

            user1.suspendUser();

            // Resume user1
            Thread.sleep(3000);

            System.out.println("\nResuming User-1...\n");

            user1.resumeUser();

            // Stop high priority thread
            Thread.sleep(2000);

            System.out.println("\nStopping HighPriorityUser...\n");

            user2.stopUser();

            // Synchronization using join()
            user1.join();

            user2.join();
        }

        catch (InterruptedException e) {

            System.out.println("Main Thread Interrupted");
        }

        System.out.println("\nAll chats completed");
    }
}