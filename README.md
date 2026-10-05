# DAA Assignment 2

NIYALOV YERKEBULAN, SE 2526

Simple int structures: DynamicArray, singly linked MyLinkedList with a tail,
and MinHeap. No bonus tasks.

## Run

Requirements: JDK 17 or newer and Maven 3.9 or newer.
Open a terminal in this folder.

Build and run the JUnit 5 tests:

```sh
mvn clean test
```

After building, run all benchmarks with one command:

```sh
java -cp target/classes Benchmark
```

This overwrites `results/results.csv`. Input uses `new Random(42)`.
There is a general warm-up, two discarded runs per case, then five measured
runs. The CSV stores the median time and the counters from that run.
Data filling is excluded from W1-W3. W4 measures both insertion and extraction.

The four PNG charts are already included. To regenerate them, use Python 3:

```sh
python -m pip install matplotlib
python plot.py
```

`REPORT.md` is the editable report; `REPORT.pdf` is its five-page reading copy.
The saved report describes the included CSV; times will vary on another run.

## Files

- `src/main/java`: three structures, common interface, counters, benchmark.
- `src/test/java`: 10 JUnit tests, including random reference checks and counters.
- `results/results.csv`: 36 cases, four sizes, both W3 variants.
- `results/plots`: one chart with four panels for each workload.

## Git and submission

The ZIP includes the local `.git` history, `main`, `feature/array`,
`feature/list`, `feature/heap`, `feature/metrics`, and the `v1.0` release tag.
The feature branches were merged into main.

GitHub URL: not added yet. A remote repository has not been provided.
Create an empty GitHub repository, then replace `YOUR_REPOSITORY_URL` below:

```sh
git remote add origin YOUR_REPOSITORY_URL
git push -u origin --all
git push origin v1.0
```

Include the real GitHub link in the Moodle submission along with
`DAA_Assignment2_Yerkebulan_Niyalov_SE2526.zip`.
