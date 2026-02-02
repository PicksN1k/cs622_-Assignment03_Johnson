import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Intent:
    The goal of this class is to support keyword searching within the merged JSON file
    and to maintain a history of all search terms used during execution. For each search,
    the program records the keyword and timestamp, outputs up to a limited number of
    matching campaign records, and provides a summary of search history statistics
    using Java Collections.
*/
public class KeywordSearch {

    private static final int MAX_RESULTS = 10;

    // === Assignment 3 memory ===
    private static Map<String, List<LocalDateTime>> searchHistory = new HashMap<>();

    public static void main(String[] args) throws IOException {

        // Hardcoded keywords for demonstration / grading output
        String[] keywords = { "robot", "fitness", "wearable", "health", "kids" };

        Path file = Paths.get("Datasets/Merged.json");

        for (String keyword : keywords) {
            System.out.println("\n=== Searching for keyword: \"" + keyword + "\" ===");
            searchAndPrint(file, keyword.toLowerCase());
        }

        // Print search statistics (Assignment 3 requirement)
        printSearchHistory();
    }

    private static void searchAndPrint(Path file, String keyword) throws IOException {

        // Record search term + timestamp
        recordSearch(keyword);

        int printed = 0;

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;

            while ((line = reader.readLine()) != null && printed < MAX_RESULTS) {

                // Restrict search to the "data" object portion
                int dataIndex = line.indexOf("\"data\":");
                if (dataIndex == -1) continue;

                String dataPart = line.substring(dataIndex);
                String lower = dataPart.toLowerCase();

                // Token-style match
                if (!containsToken(lower, keyword)) continue;

                String percent = extractJsonNumber(dataPart, "funds_raised_percent");
                String closeDate = extractJsonString(dataPart, "close_date");

                System.out.println(
                        "funds_raised_percent=" + percent +
                        " | close_date=" + closeDate
                );
                printed++;
            }
        }

        if (printed == 0) {
            System.out.println("No matches found.");
        }
    }

    // ================= Assignment 3 Methods =================

    private static void recordSearch(String keyword) {
        searchHistory
                .computeIfAbsent(keyword, k -> new ArrayList<>())
                .add(LocalDateTime.now());
    }

    private static void printSearchHistory() {
        System.out.println("\n=== Search History Summary ===");
        System.out.println("Total unique search terms: " + searchHistory.size());

        for (Map.Entry<String, List<LocalDateTime>> entry : searchHistory.entrySet()) {
            System.out.println("\nKeyword: " + entry.getKey());
            System.out.println("Times searched: " + entry.getValue().size());
            System.out.println("Timestamps:");
            for (LocalDateTime time : entry.getValue()) {
                System.out.println("  - " + time);
            }
        }
    }

    // ================= Helper Methods =================

    // token-ish match: "robot" matches "robot_id" and "robot", not "robotics"
    private static boolean containsToken(String text, String keyword) {
        int from = 0;
        while (true) {
            int idx = text.indexOf(keyword, from);
            if (idx == -1) return false;

            boolean leftOk = (idx == 0) || !Character.isLetterOrDigit(text.charAt(idx - 1));
            int end = idx + keyword.length();
            boolean rightOk = (end >= text.length()) || !Character.isLetterOrDigit(text.charAt(end));

            if (leftOk && rightOk) return true;
            from = idx + 1;
        }
    }

    private static String extractJsonString(String json, String key) {
        String k = "\"" + key + "\":";
        int i = json.indexOf(k);
        if (i == -1) return "null";

        i += k.length();
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;

        if (i < json.length() && json.startsWith("null", i)) return "null";
        if (i >= json.length() || json.charAt(i) != '"') return "null";

        i++;
        StringBuilder sb = new StringBuilder();
        boolean escaping = false;

        while (i < json.length()) {
            char c = json.charAt(i);
            if (escaping) {
                sb.append(c);
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
            i++;
        }
        return sb.toString();
    }

    private static String extractJsonNumber(String json, String key) {
        String k = "\"" + key + "\":";
        int i = json.indexOf(k);
        if (i == -1) return "0";

        i += k.length();
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;

        int start = i;
        while (i < json.length() && "0123456789.-".indexOf(json.charAt(i)) != -1) i++;

        String out = json.substring(start, i).trim();
        return out.isEmpty() ? "0" : out;
    }
}