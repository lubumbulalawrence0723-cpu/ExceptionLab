
import java.io.*;
import java.util.*;
import java.util.logging.*;

// Custom exception for duplicate student IDs
class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String message) {
        super(message);
    }
}

public class MarksImporter {

    private static final Logger logger =
            Logger.getLogger(MarksImporter.class.getName());

    static void importMarks(String filename) throws IOException {

        Set<String> seen = new HashSet<>();

        // try-with-resources automatically closes the reader,
        // even if an exception occurs.
        try (BufferedReader br = new BufferedReader(
                new FileReader(filename))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                try {
                    // TODO 1: Check that there are exactly 2 fields
                    String[] parts = line.split(",", -1);

                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                                "Record must contain student ID and mark");
                    }

                    String studentID = parts[0].trim();
                    String markText = parts[1].trim();

                    if (studentID.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Student ID cannot be empty");
                    }

                    // TODO 2: Convert mark and validate its range
                    int mark = Integer.parseInt(markText);

                    if (mark < 0 || mark > 100) {
                        throw new IllegalArgumentException(
                                "Mark must be between 0 and 100");
                    }

                    // TODO 3: Detect duplicate student IDs
                    if (seen.contains(studentID)) {
                        throw new DuplicateStudentException(
                                "Duplicate student ID: " + studentID);
                    }

                    seen.add(studentID);

                    // TODO 4: Display accepted record
                    System.out.println(
                            "Accepted: " + studentID + " = " + mark);

                } catch (NumberFormatException e) {

                    System.out.println("Invalid numeric mark on line "
                            + lineNumber + ": " + line);

                    logger.log(Level.WARNING,
                            "Number format error on line " + lineNumber, e);

                } catch (DuplicateStudentException e) {

                    System.out.println(e.getMessage());

                    logger.log(Level.WARNING,
                            "Duplicate record on line " + lineNumber, e);

                } catch (IllegalArgumentException e) {

                    System.out.println("Invalid record on line "
                            + lineNumber + ": " + e.getMessage());

                    logger.log(Level.WARNING,
                            "Invalid data on line " + lineNumber, e);
                }
            }
        }
    }

    public static void main(String[] args) {

        try {
            importMarks("marks.txt");

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Marks file not found; check the file name");

            logger.log(Level.SEVERE,
                    "File not found", e);

        } catch (DuplicateStudentException e) {

            System.out.println("Duplicate student error: "
                    + e.getMessage());

            logger.log(Level.SEVERE,
                    "Duplicate student exception", e);

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid data: " + e.getMessage());

            logger.log(Level.SEVERE,
                    "Invalid data exception", e);

        } catch (IOException e) {

            System.out.println(
                    "An error occurred while reading the marks file");

            logger.log(Level.SEVERE,
                    "File reading error", e);

        } finally {

            System.out.println("Import attempt finished");
        }
    }
}