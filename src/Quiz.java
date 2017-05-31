import javax.swing.*;

/**
 * Created by j.halloran on 26/05/2017.
 */
public class Quiz {

    public static void main(String[] args) {
        Quiz quiz = new Quiz();
    }

    public Quiz() {
        JFrame frame = new JFrame();

        Object[] options = {"Easy", "Hard", "Quit"};

        int selection = -1;
        String questionText = "Select the difficulty of your quiz";

        while (selection < 0 || selection > 2) {
            selection = JOptionPane.showOptionDialog(
                    frame, // frame
                    questionText, // Question text
                    "Select difficulty", // Dialogue title
                    JOptionPane.YES_NO_OPTION, // option type
                    JOptionPane.QUESTION_MESSAGE, // message behaviour
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title
            if (selection == 2) {
                die();
            } else if (selection < 0 || selection > 2) {
                // If user does not make a valid choice
                questionText = "Please make a valid selection. \n Select the difficulty of your quiz";
            }
        }

        runQuiz(selection);
    }

    public void runQuiz(int difficulty) {
        String[] questions;
        String[] correctAnswers;
        String[][] wrongAnswers;
        FileHandler fileHandler = new FileHandler();

        if (difficulty == 0) {
            // Easy
            questions = fileHandler.getTextAsArray("questions.txt");
            correctAnswers = fileHandler.getTextAsArray("correct-answer.txt");
            wrongAnswers = fileHandler.getTextAs2DArray("wrong-answers.txt");
        } else {
            // Hard
            questions = fileHandler.getTextAsArray("questions-hard.txt");
            correctAnswers = fileHandler.getTextAsArray("correct-answer-hard.txt");
            wrongAnswers = fileHandler.getTextAs2DArray("wrong-answers-hard.txt");
        }

        QuizMaster quizMaster = new QuizMaster(
                "Joe's Quiz", //title
                questions, // question text array
                correctAnswers, // correct answers array
                wrongAnswers); // wrong answers 2D array
    }

    public static void die() {
        JOptionPane.showMessageDialog(null, "Thanks for playing");
        System.exit(0);
    }




}
