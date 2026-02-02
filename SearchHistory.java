import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Intent:
    The goal of this class is to maintain search memory for Assignment 3.
    It records each search term along with the time it was requested and
    provides a readable summary of search statistics using Java Collections.
*/
public class SearchHistory {

    private final Map<String, List<LocalDateTime>> history = new HashMap<>();

    // Record a keyword search with the current timestamp
    public void recordSearch(String keyword) {
        history
            .computeIfAbsent(keyword, k -> new ArrayList<>())
            .add(LocalDateTime.now());
    }

    // Print search statistics
    public void printSummary() {
        System.out.println("\n=== Search History Summary ===");
        System.out.println("Total unique search terms: " + history.size());

        for (Map.Entry<String, List<LocalDateTime>> entry : history.entrySet()) {
            System.out.println("\nKeyword: " + entry.getKey());
            System.out.println("Times searched: " + entry.getValue().size());
            System.out.println("Timestamps:");
            for (LocalDateTime time : entry.getValue()) {
                System.out.println("  - " + time);
            }
        }
    }
}