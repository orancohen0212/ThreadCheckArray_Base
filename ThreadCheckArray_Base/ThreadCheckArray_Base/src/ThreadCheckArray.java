import java.util.ArrayList;

/**
 * A {@code Runnable} task that recursively checks if a subset of an {@code ArrayList<Integer>}
 * sums up to a specific target value.
 * <p>
 * This class operates in a multithreaded environment, using a shared {@code SharedData}
 * object to coordinate execution and share results among different threads.
 * </p>
 */
public class ThreadCheckArray implements Runnable {

    /**
     * A flag to indicate whether a valid subset sum has been found by this thread.
     */
    private boolean flag;

    /**
     * A boolean array used to mark which elements from the primary list are included 
     * in the successful subset sum.
     */
    private boolean[] winArray;

    /**
     * The shared data instance used for thread synchronization and accessing shared variables.
     */
    SharedData sd;

    /**
     * The primary list of integers to be evaluated for the subset sum condition.
     */
    ArrayList<Integer> array;

    /**
     * The target sum to reach.
     */
    int b;

    /**
     * Constructs a new {@code ThreadCheckArray} instance.
     * <p>
     * Initializes the local variables from the provided {@code SharedData} object in a 
     * synchronized block to ensure thread safety. The {@code winArray} is initialized 
     * using the size of the retrieved {@code ArrayList<Integer>}.
     * </p>
     *
     * @param sd The shared data object containing the list and the target sum.
     */
    public ThreadCheckArray(SharedData sd) {
        this.sd = sd;
        synchronized (sd) {
            array = sd.getArray();
            b = sd.getB();
        }
        winArray = new boolean[array.size()];
    }

    /**
     * Recursively checks if a subset of the first {@code n} elements in the list sums to {@code b}.
     *
     * @param n The number of elements to consider from the start of the list.
     * @param b The target sum for the current recursive step.
     */
    void rec(int n, int b) {
        synchronized (sd) {
            if (sd.getFlag()) return;
        }
        if (n == 1) {
            if(b == 0 || b == array.get(n - 1)) {
                flag = true;
                synchronized (sd) { sd.setFlag(true); }
            }
            if (b == array.get(n - 1)) winArray[n - 1] = true;
            return;
        }

        rec(n - 1, b - array.get(n - 1));
        if (flag) winArray[n - 1] = true;
        synchronized (sd) {
            if (sd.getFlag()) return;
        }
        rec(n - 1, b);
    }

    /**
     * The entry point for thread execution.
     * <p>
     * Initiates the recursive check for the subset sum problem. The initial work is 
     * divided between the thread named "thread1" and other threads.
     * </p>
     */
    public void run() {
        if (array.size() != 1) {
            if (Thread.currentThread().getName().equals("thread1")) {
                rec(array.size() - 1, b - array.get(array.size() - 1));
            } else {
                rec(array.size() - 1, b);
            }
        }

        if (array.size() == 1) {
            if (b == array.get(0) && !flag) {
                winArray[0] = true;
                flag = true;
                synchronized (sd) { sd.setFlag(true); }
            }
        }

        if (flag) {
            if (Thread.currentThread().getName().equals("thread1")) {
                winArray[array.size() - 1] = true;
            }
            synchronized (sd) { sd.setWinArray(winArray); }
        }
    }
}