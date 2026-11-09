import java.util.ArrayList;

/**
 * Represents shared data that can be used in a multithreaded environment.
 * It holds a list of integers, a boolean array to track statuses (like wins),
 * a boolean flag, and a constant integer value.
 */
public class SharedData {

    /**
     * The primary data structure holding integer values.
     */
    private ArrayList<Integer> array;

    /**
     * A boolean array used to track the status of each corresponding element in the {@code array}.
     */
    private boolean[] winArray;

    /**
     * A boolean flag generally used for signaling conditions across threads.
     */
    private boolean flag;

    /**
     * A constant integer value.
     */
    private final int b;

    /**
     * Constructs a new {@code SharedData} instance.
     * <p>
     * Initializes the integer list and the constant value {@code b}.
     * The {@code winArray} is also initialized here to match the exact size of the provided list.
     * </p>
     *
     * @param array The list of integers of type {@code ArrayList<Integer>}.
     * @param b     The constant integer value.
     */
    public SharedData(ArrayList<Integer> array, int b) {
        this.array = array;
        this.b = b;

        // Properly initializing the boolean array using the size() of the ArrayList
        if (array != null) {
            this.winArray = new boolean[array.size()];
        }
    }

    /**
     * Retrieves the boolean array tracking statuses.
     *
     * @return A boolean array representing win/status conditions.
     */
    public boolean[] getWinArray() {
        return winArray;
    }

    /**
     * Sets the boolean array tracking statuses.
     *
     * @param winArray The new boolean array to set.
     */
    public void setWinArray(boolean[] winArray) {
        this.winArray = winArray;
    }

    /**
     * Retrieves the primary list of integers.
     *
     * @return The integer list of type {@code ArrayList<Integer>}.
     */
    public ArrayList<Integer> getArray() {
        return array;
    }

    /**
     * Retrieves the constant integer value.
     *
     * @return The constant integer {@code b}.
     */
    public int getB() {
        return b;
    }

    /**
     * Retrieves the current state of the boolean flag.
     *
     * @return {@code true} if the flag is set, {@code false} otherwise.
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * Sets the boolean flag.
     *
     * @param flag The new state of the boolean flag.
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}