import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*
Intent:
    The goal of this class is to perform streaming keyword searches on the
    merged JSON dataset and present relevant campaign information to the user.
    Search history tracking is delegated to the SearchHistory class.
*/
public class KeywordSearch {

    private static final int MAX_RESULTS = 10;
    private static final SearchHistory searchHistory = new SearchHistory();

    public static void main(String[] args) throws IOException {

        Path file = Paths.get("Datasets/Merged.json");
        Scanner scanner = new Scanner(System.in);

        // Assignment 3: allow multiple searches in one session
        for (int i = 0; i < 5; i++) {
            System.out.print("\nEnter search keyword: ");
            String keyword = scanner.nextLine().toLowerCase();

            System.out.println("\n=== Searching for keyword: \"" + keyword + "\" ===");
            searchAndPrint(file, keyword);
        }

        // Print memory summary
        searchHistory.printSummary();
        scanner.close();
    }

    private static void searchAndPrint(Path file, String keyword) throws IOException {

        searchHistory.recordSearch(keyword);
        int printed = 0;

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;

            while ((line = reader.readLine()) != null && printed < MAX_RESULTS) {

                int dataIndex = line.indexOf("\"data\":");
                if (dataIndex == -1) continue;

                String dataPart = line.substring(dataIndex);
                String lower = dataPart.toLowerCase();

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

    // ================= Helper Methods =================

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