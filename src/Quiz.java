import javax.swing.*;

/**
 * Runs a new quiz.
 */
public class Quiz {

    public static void main(String[] args) {
        Quiz quiz = new Quiz();
    }

    /**
     * Asks user to select difficulty or quits application.
     */
    public Quiz() {
        JFrame frame = new JFrame();
        Object[] options = {"Easy", "Hard", "Quit"};
        int selection = -1;             // Initialise selection to be out of valid range
        String questionText = "Select the difficulty of your quiz";
        while (selection < 0 || selection > 2) {                            // Keep asking until valid selection made
            selection = JOptionPane.showOptionDialog(
                    frame,                              // frame
                    questionText,                       // Question text
                    "Select difficulty",                // Dialogue title
                    JOptionPane.YES_NO_OPTION,          // option type
                    JOptionPane.QUESTION_MESSAGE,       // message behaviour
                    null,                               // do not use a custom Icon
                    options,                            // Object holding button text
                    options[0]);                        // default button
            if (selection == 2) {                                    // Quit selected - end quiz
                die();
            } else if (selection < 0 || selection > 2) {             // Prompt if user does not make a valid selection
                questionText = "Please make a valid selection. \n Select the difficulty of your quiz";
            }
        }
        runQuiz(selection);         // Run quiz based on selection
    }

    /**
     * Run quiz at specified difficulty level
     */
    public void runQuiz(int difficulty) {
        String[] questions;
        String[] correctAnswers;
        String[][] wrongAnswers;
        FileHandler fileHandler = new FileHandler();    // File handler process txt files into correct data structures

        if (difficulty == 0) {
            // Easy
            questions = fileHandler.getTextAsArray("questions-easy.txt");
            correctAnswers = fileHandler.getTextAsArray("correct-answer-easy.txt");
            wrongAnswers = fileHandler.getTextAs2DArray("wrong-answers-easy.txt");
        } else {
            // Hard
            questions = fileHandler.getTextAsArray("questions-hard.txt");
            correctAnswers = fileHandler.getTextAsArray("correct-answer-hard.txt");
            wrongAnswers = fileHandler.getTextAs2DArray("wrong-answers-hard.txt");
        }

        QuizMaster quizMaster = new QuizMaster(
                "Joe's Quiz",               // quiz title
                questions,                  // question text array
                correctAnswers,             // correct answers array
                wrongAnswers);              // wrong answers 2D array
    }

    /**
     * Quit quiz gracefully.
     */
    public static void die() {
        JOptionPane.showMessageDialog(null, "Thanks for playing");
        System.exit(0);
    }
}
