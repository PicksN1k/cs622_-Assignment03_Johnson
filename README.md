# CS622_Assignment03_Johnson
# Author: Cole Johnson
# Met CS 622

Overview

For this assignment I copied assignment 2 and simply edited the KeywordSearch.java class with the addition of SearchHistory.java

1. FileMerger.java
    - Merges two JSON dataset files into a single output file using a streaming approach.
    - Not required for Assignment 3 execution, but included for completeness.

2. KeywordSearch.java
    - Performs a streaming keyword search on the merged JSON file and prints selected fields
    - Outputs selected campaign fields for matching records.
    - Delegates all search memory tracking to the SearchHistory class.

3. SearchHistory.java
    - Maintains search statistics using Java collections.
    - Tracks unique search terms, search frequency, and timestamps.
    - Outputs a readable summary of all searches performed during the session.

Both programs operate line-by-line using buffered I/O and do not load entire files into memory.

# Part 1. : File Merger
Purpose
- FileMerger.java combines the contents of two JSON files (Left.json and Right.json) into a
single output file called merged.json.

Key Characteristics
- Uses BufferedReader and BufferedWriter
- Reads each input file sequentially
- Writes all lines from the left file, followed by all lines from the right file
- Preserves all original data without modification

How It Works
- Open both input files and the output file
- Read Left.json line by line and write to merged.json
- Read Right.json line by line and append to merged.json
- Close all streams automatically using try-with-resources

# Part 2: Keyword Search
Purpose
- KeywordSearch.java performs streaming keyword searches on the merged JSON dataset and prints relevant campaign information for matching records.
It allows multiple searches per program session and records search statistics.

Search Behavior
- The user is prompted to enter search keywords dynamically
- Searches continue until the user enters q to quit
- Each search is recorded in memory for later reporting

# Part 3: Search History (Assignment 3 Memory Requirement)
Purpose
- SearchHistory.java fulfills the Assignment 3 memory requirement by tracking all searches performed during a session.

Implementation Details
- Uses the Java Collections Framework: Map<String, List<LocalDateTime>>

Stores:
- Each unique search term
- Number of times each term was searched
- Timestamp of each search occurrence
- Prints a labeled and readable summary at the end of execution

Example Summary Output
- Total unique search terms
- Search frequency per keyword
- Timestamps of all searches

# Output Fields

For each keyword match, the program prints:
- funds_raised_percent
- close_date

# Implementation Details
- Processes the file line by line (streaming)
- Manual JSON field extraction (no external libraries)
- Token-style keyword matching:
- Clear separation of responsibilities:
- Asks any number of searches until the user ends the session

# How to Run

1. Ensure the dataset files exist in the Datasets/ directory:
    - Left.json
    - Right.json

2. Compile the programs:
    - javac FileMerger.java
    - KeywordSearch.java

3. Run the file merger:
    - FileMerger.java


4. Run the keyword search:
    - java KeywordSearch

# Notes
- Search memory persists only for the duration of the program session, as specified.
- No graphical user interface is implemented or required.
- The program design follows Java naming conventions and object-oriented best practices.
- The code is structured to be easily extensible for additional search statistics or output fields.