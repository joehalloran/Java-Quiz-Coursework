import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;


/**
 * Created by j.halloran on 30/05/2017.
 */
public class QuizMaster implements ActionListener{

    public int score;
    public int questionCounter; // Question index
    public boolean skippingQuestions; // Is skipping allowed - on/off

    public String currentAnswer; // Current selected answer (radio button)

    // Data structures for questions and answers
    public String[] questions;
    public String[] correctAnswers;
    public String[][] wrongAnswers;

    // A place holder array to handle quiz reset for cases where a different number of answers are supplied for different questions
    int maxLenghtWrongAnswersSubArray = 0;

    // Data structures for skipped and incorrectly answered questions
    public Stack mistakes = new Stack();
    public Stack skipped = new Stack();

    // Display Frame for quiz
    public JFrame frame = new JFrame("Joe's quiz");

    public QuizMaster(String quizTitle, String[] questionInput, String[] correctAnswerInput, String[][] wrongAnswersInput){

        frame.setTitle(quizTitle);

        // Initialise variables with contructor parameters
        skippingQuestions = true;
        questionCounter = 0;
        questions = questionInput;
        correctAnswers = correctAnswerInput;
        wrongAnswers = wrongAnswersInput;
        // Ask first question
        askQuestion(questions[questionCounter], correctAnswers[questionCounter], wrongAnswers[questionCounter]);

    }

    public void askQuestion(String questionText, String correctAnswer, String[] wrongAnswers) {

        // Setup panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        JLabel label = new JLabel();

        // Add question text
        label.setText(questionText);
        panel.add(label);

        // Create "OK" button
        JButton submitButton  = new JButton();
        submitButton.setText("OK");
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(this);

        // Create "Skip" button
        JButton skipButton  = new JButton();
        skipButton.setText("Skip");
        skipButton.setActionCommand("skip");
        skipButton.addActionListener(this);

        // Create "Skip" button
        JButton quitButton  = new JButton();
        quitButton.setText("Quit");
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(this);

        // Create radio buttons for answers
        String[] allAnswers = combineAnswers(correctAnswer, wrongAnswers);
        ButtonGroup answerGroup = new ButtonGroup();
        JRadioButton[] answerButtons = new JRadioButton[allAnswers.length];
        for(int i = 0; i < allAnswers.length; i++) {
            answerButtons[i] = new JRadioButton();
            answerButtons[i].setText(allAnswers[i]);
            answerButtons[i].addActionListener(this);
            answerGroup.add(answerButtons[i]);
            panel.add(answerButtons[i]);
        }

        // Add "OK", "Skip" and "Quit" buttons
        panel.add(submitButton);
        // Add skip button, if skipping allowed
        if (skippingQuestions) {
            panel.add(skipButton);
        }
        panel.add(quitButton);

        // Load frame
        frame.add(panel);
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Handle all button clicks
        if (command.equalsIgnoreCase("submit")) {
            checkAnswer();
            nextQuestion();
        } else if (command.equalsIgnoreCase("skip")) {
            // Update max sub array length for quiz reset later
            if (wrongAnswers[questionCounter].length > maxLenghtWrongAnswersSubArray) {
                maxLenghtWrongAnswersSubArray = wrongAnswers[questionCounter].length;
            }
            skipped.push(questionCounter);
            nextQuestion();
        } else if (command.equalsIgnoreCase("quit")) {
            frame.dispose();
            endQuiz();
        } else {
            // The user has not pressed submit, skip, or quit, so must have selected a new answer.
            currentAnswer = command;
        }
    }

    // Ask next question or end quiz if no questions left
    public void nextQuestion(){
        frame.dispose();
        questionCounter++;
        System.out.println(questionCounter);
        if (questionCounter < correctAnswers.length) {
            frame = new JFrame("Joe's quiz");
            askQuestion(questions[questionCounter], correctAnswers[questionCounter], wrongAnswers[questionCounter]);
        } else if (skippingQuestions && !(skipped.empty())) {
            // Begin answering the skipping quesions
            resetQuizForSkippedQuestions();
        } else {
            endQuiz();
        }
    }

    private void resetQuizForSkippedQuestions() {

        skippingQuestions = false; // Turn off skipping

        // Create new data structures for skipped Questions
        int stackSize = skipped.size(); // Cache variable
        String[] skippedQuestions = new String[stackSize];
        String[] skippedCorrectAnswers = new String[stackSize];
        String[][] skippedWrongAnswers = new String[stackSize][maxLenghtWrongAnswersSubArray];

        // Process data into data structures
        int counter = stackSize - 1;
        while (!skipped.empty()) {
            int skippedQuestionNumber = (int) skipped.pop(); // Typecast to int
            skippedQuestions[counter] = questions[skippedQuestionNumber];
            skippedCorrectAnswers[counter] = correctAnswers[skippedQuestionNumber];
            for (int i = 0; i < wrongAnswers[skippedQuestionNumber].length; i++) {
                skippedWrongAnswers[counter][i] = wrongAnswers[skippedQuestionNumber][i];
            }
            counter--;
        }

        // Reset global variables and begin asking skipped questions
        questions = skippedQuestions;
        correctAnswers = skippedCorrectAnswers;
        wrongAnswers = skippedWrongAnswers;
        questionCounter = -1; // Reset to -1 as nextQuestion() will increment to 0
        nextQuestion();

    }

    public void endQuiz(){
        // Generate mistakes report
        String mistakesReport = "";
        if (!mistakes.empty()) {
            mistakesReport = "\nMISTAKE REPORT\n";
            int mistake;
            while (mistakes.size() > 0) {
                mistake = (int)mistakes.pop(); // Typecast to int
                System.out.println(mistake);
                mistakesReport = mistakesReport + mistake + ": " + questions[mistake] + "\n Correct answer: " + correctAnswers[mistake] + "\n";
                System.out.println(mistakesReport);
            }
        }
        // Output final message
        JOptionPane.showMessageDialog(null, "Your have scored = " + score + " points.\n This is equivalent to " + (score * 10) + "% \n" + mistakesReport + "\n Well Done!");
        System.exit(0);
    }

    // Check answers and handle correct and incorrect answers
    private void checkAnswer() {
        if (currentAnswer == correctAnswers[questionCounter]) {
            // Correct
            score = score + 1;
        } else {
            // Wrong
            mistakes.push(questionCounter);
        }
    }

    private String[] combineAnswers(String correctAnswer, String[] wrongAnswers) {

        Stack answerStack = new Stack();
        for (int i = 0; i < wrongAnswers.length; i++) {
            // check for null values which may exist in jagged arrays
            if (wrongAnswers[i] != null) {
                answerStack.push(wrongAnswers[i]);
            }
        }
        // Add correct answer to stack
        answerStack.push(correctAnswer);

        // Unpack stack into an array
        String[] answerList = new String[answerStack.size()];
        int counter = 0;
        while (!answerStack.empty()) {
            answerList[counter] = (String)answerStack.pop(); // Typecast to string
            counter++;
        }
        return answerList;
    }

}
