import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Reads text files to get quiz questions and answers. Processes into arrays.
 */
public class FileHandler {

    /**
     * Wrapper to handle IO exceptions
     *
     * Returns array from readFile method
     */
    public String[] getTextAsArray(String fileName) {

        String[] text = null;
        try {
            text = readFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Handles complexity of reading the questions text file and processing into array.
     * Uses stacks to allow for possibility of different number of questions in quiz.
     *
     * Returns an array where each line of text file is nth item in array
     */
    private static String[] readFile(String fileName) throws IOException {
        // Add text file lines into stack;
        String fileLine;
        Stack textStack = new Stack();
        Scanner fileScan;
        File questionFile = new File(fileName);
        fileScan = new Scanner(questionFile);
        while (fileScan.hasNext()) {
            fileLine = fileScan.nextLine();
            textStack.push(fileLine);
        }
        // Initialise output array and populate from stack;
        String[] outputArray = new String[textStack.size()];
        // Iterate backwards through the array to add items from the stack
        int i = (outputArray.length) - 1;
        while (!textStack.empty()) {
            outputArray[i] = (String) textStack.pop(); //Type case to string
            i--;
        }
        return outputArray;
    }

    /**
     * Processes file array into a 2D array,
     * comma separating each line of the text file
     */
    public String[][] getTextAs2DArray(String fileName) {
        // Get input array
        String[] inputArray = getTextAsArray(fileName);
        // find max length of text in rows to correctly initialise 2D array for any number of items
        int maxLength = 0;
        for (int k = 0; k < inputArray.length; k++) {
            String[] splitText = inputArray[k].split(",");
            if (splitText.length > maxLength) {
                maxLength = splitText.length;
            }
        }
        // Initialise and populate output array
        String[][] outputArray = new String[inputArray.length][maxLength];
        for (int i = 0; i < inputArray.length; i ++) {
            String[] splitText = inputArray[i].split(",");
            for (int j = 0; j < splitText.length; j++) {
                outputArray[i][j] = splitText[j];
            }
        }
        return outputArray;
    }
}
