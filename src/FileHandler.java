import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by j.halloran on 26/05/2017.
 */
public class FileHandler {


    // Get text as array and split each line with commas
    public String[][] getTextAs2DArray(String fileName) {

        String[] inputArray = getTextAsArray(fileName);

        // find max length of text to split into to correctly initialise 2D return array
        int maxLength = 0;
        for (int k = 0; k < inputArray.length; k++) {
            String[] splitText = inputArray[k].split(",");
            if (splitText.length > maxLength) {
                maxLength = splitText.length;
            }
        }

        String[][] outputArray = new String[inputArray.length][maxLength];

        Stack textStack = new Stack();

        for (int i = 0; i < inputArray.length; i ++) {
            String[] splitText = inputArray[i].split(",");

            for (int j = 0; j < splitText.length; j++) {
                textStack.push(splitText[j]);
            }

            // Iterate backwards through stack to process array
            int counter = textStack.size()-1;
            while (!textStack.empty()) {
                outputArray[i][counter] = (String)textStack.pop(); // Typecast to string
                counter--;
            }

        }

        return outputArray;
    }

    // Meta method to handle IOExceptions.
    public String[] getTextAsArray(String fileName) {

        String[] text = null;
        try {
            text = readFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    // Handles complexity of reading the qusetions text file.
    public static String[] readFile(String fileName) throws IOException {
        String fileLine;
        Stack textStack = new Stack();

        Scanner fileScan;
        File questionFile = new File(fileName);

        fileScan = new Scanner(questionFile);

        while (fileScan.hasNext()) {
            fileLine = fileScan.nextLine();
            textStack.push(fileLine);
        }

        String[] textArray = new String[textStack.size()];

        // Iterate backwards through the array to add items from the stack
        int i = (textArray.length) - 1;
        while (!textStack.empty()) {
            textArray[i] = (String) textStack.pop(); //Type case to string
            i--;
        }

        return textArray;

    }

    // Help method for debugging.
    public static void printArray(String [] inputArray){
         for(int i = 0; i < inputArray.length; i++){
            System.out.println(inputArray[i]);
        }
    }
}
