import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/*Intent:
    The goal of this file is to not so much completely merge the files but rather to write all of the fcontents of both files
    into a new file. The files I used were Json files so the output file is also a Json file.
    The implementation reads each line of both input files and writes them sequentially to the output file.
    This approach ensures that all data from both files is preserved in the merged output file, as opposed to alternating line by line
    Or any other complex merging strategy.
*/
public class FileMerger {

    // Goal is to merge two files into one
    public static void main(String[] args) {
        Path leftPath = Paths.get("Datasets/Left.json");
        Path rightPath = Paths.get("Datasets/Right.json");
        Path outputPath = Paths.get("Datasets/Merged.json");

        try {
            mergeTwoJsonFiles(leftPath, rightPath, outputPath);
            System.out.println("Files merged successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred during file merging, Please try running again."+ e.getMessage());
        }
    }

    //Goal is to subsequently read and then right both files contents into the new file.

    public static void mergeTwoJsonFiles(Path leftPath, Path rightPath, Path outputPath) throws IOException {
        // Implementation for merging two JSON files)
        try (
            BufferedReader leftReader = Files.newBufferedReader(leftPath);
            BufferedReader rightReader = Files.newBufferedReader(rightPath);
            BufferedWriter outwriter = Files.newBufferedWriter(outputPath);
        ) {
            String leftLine, rightLine;
            while ((leftLine = leftReader.readLine()) != null) {
                    outwriter.write(leftLine);
                    outwriter.newLine();
            }
            while ((rightLine= rightReader.readLine()) != null) {
                    outwriter.write(rightLine);
                    outwriter.newLine();
            }
        }
    }
}
