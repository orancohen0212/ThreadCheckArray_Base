# ThreadCheckArray

A small Java console program that searches for a subset of integer values whose sum equals a target. The search is recursive and is started concurrently by two Java threads. The project is organized as a simple source-only IntelliJ/Java module, with source files in `src/`.

## How the algorithm works

For a list `A` and target `b`, the recursive search considers each value in turn and explores the two subset-sum choices:

1. **Include** the current value by subtracting it from the remaining target.
2. **Exclude** the current value and leave the remaining target unchanged.

The recursion reaches its one-element base case and signals success when that final value can satisfy the remaining target (the implementation also treats a remaining target of zero as success). A per-task boolean array records which list positions belong to the successful path. The console prints this array as `C`, aligned with the input indices and values.

`TestThreadCheckArray` starts two `ThreadCheckArray` tasks. The thread named `thread1` searches the branch that includes the last list element first; the other thread searches the branch that excludes it first. Each task then applies the same include-first recursive search to its assigned branch. This is a parallel search of the two top-level choices, not a thread for every recursive call. In the worst case, subset-sum search remains exponential (`O(2^n)` work); recursion depth is proportional to the number of values.

### Thread coordination

Both tasks share a `SharedData` instance containing the input list, target, result flag, and result bitmap. A successful task sets the shared flag and publishes its bitmap while synchronized on that shared object. Recursive calls check the shared flag under the same monitor and stop exploring once a solution has been reported. The monitor coordinates access to the shared result state; it does not make `ArrayList` itself thread-safe. The input list is only read while the workers run and must not be modified concurrently.

The main method waits for both workers with `join()` before reading and printing the result. The current implementation identifies the initial branches by the thread name `thread1`, so keep that name when changing how the worker threads are created. The input list must contain at least one value; an empty list is not handled by the search.

## `ArrayList<Integer>` refactoring

The integer input is represented by `ArrayList<Integer>` rather than a primitive `int[]`. `SharedData` accepts and exposes that list, and `ThreadCheckArray` accesses elements with `get(index)` and the list length with `size()`. The console driver builds the list by adding each entered number. The separate `boolean[]` is retained to mark the selected subset positions.

Because `ArrayList<Integer>` stores boxed `Integer` values, element reads are unboxed for arithmetic. The list is shared read-only among the search tasks; each task maintains its own result bitmap until publishing a successful result.

## Build and run

Requires a JDK. From the project root, compile the Java sources and run the console program:

```powershell
New-Item -ItemType Directory -Force out
javac -d out src\SharedData.java src\ThreadCheckArray.java src\TestThreadCheckArray.java
java -cp out TestThreadCheckArray
```

Enter the list size, each integer, and then the target sum when prompted. The program prints `Sorry` if no solution is found; otherwise it prints the target, input indices and values, and a `0`/`1` inclusion row.

## Source documentation

`SharedData.java` and `ThreadCheckArray.java` include Javadoc comments for their classes and documented fields, constructors, and methods. Generate HTML API documentation from the project root with:

```powershell
javadoc -d docs src\SharedData.java src\ThreadCheckArray.java
```

The generated `docs/` directory is ignored by Git. The console driver is an example entry point and does not currently have equivalent Javadoc coverage.

## Authors and Git collaboration

The available commit history records the following project contributor:

| Contributor | Recorded contribution |
|---|---|
| `orancohen0212` | Repository author and implementation contributor; the history includes the initial threading project and the `ArrayList<Integer>`/Javadoc refactor. |

No separate reviewer or additional collaborator is recorded in the available history. For future collaboration, contributors can work on topic branches and submit pull requests; the author can review and maintain the changes before they are merged. Do not infer review or approval from commit authorship alone.

## License

This project is intended to be distributed under the **Braude College Academic License**. The repository currently does not include the text of that license or define its terms. Add the official, institution-approved license text before relying on or redistributing the project under it; the name in this README is not a substitute for the license terms.