import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;


/**
 * Handles asking of questions and process answers.
 * Includes UI
 * Also facilitates skipping of questions and will report wrong answers at the end of the quiz
 */
public class QuizMaster implements ActionListener{

    /**
     * Attributes
     */
    public int score;
    public int questionCounter;                 // Question index
    public boolean skippingQuestions;

    public String currentAnswer = null;         // Current selected answer (radio buttons)

    public String[] questions;
    public String[] correctAnswers;
    public String[][] wrongAnswers;

    int maxLenghtWrongAnswersSubArray = 0;     // Used in quiz reset to handle jagged 2D array of wrong answers

    public String mistakesReport = "";
    public Stack skipped = new Stack();

    public JFrame frame = new JFrame();

    /**
     * Constructor intialises essential attributes
     */
    public QuizMaster(String quizTitle,
                      String[] questionInput,
                      String[] correctAnswerInput,
                      String[][] wrongAnswersInput){

        frame.setTitle(quizTitle);
        skippingQuestions = true;
        questionCounter = 0;
        questions = questionInput;
        correctAnswers = correctAnswerInput;
        wrongAnswers = wrongAnswersInput;
        // Ask first question
        askQuestion(questions[questionCounter], correctAnswers[questionCounter], wrongAnswers[questionCounter]);

    }

    /**
     * Builds the question asking UI for each question
     */
    public void askQuestion(String questionText, String correctAnswer, String[] wrongAnswers) {

        // Setup panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(0,1));

        JPanel answersPanel = new JPanel();
        answersPanel.setLayout(new GridLayout(0,1));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());

        // Create question text label
        JLabel questionLabel = new JLabel();
        questionLabel.setText(questionText);

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

        // Create "Quit" button
        JButton restartButton  = new JButton();
        restartButton.setText("Restart");
        restartButton.setActionCommand("restart");
        restartButton.addActionListener(this);

        // Create radio buttons for answers
        String[] allAnswers = combineAnswers(correctAnswer, wrongAnswers);
        ButtonGroup answerGroup = new ButtonGroup();
        JRadioButton[] answerButtons = new JRadioButton[allAnswers.length];
        for(int i = 0; i < allAnswers.length; i++) {
            answerButtons[i] = new JRadioButton();
            answerButtons[i].setText(allAnswers[i]);
            answerButtons[i].addActionListener(this);
            answerGroup.add(answerButtons[i]);
            answersPanel.add(answerButtons[i]);
        }

        // Add "OK", "Skip" and "Quit" buttons
        controlsPanel.add(submitButton);
        // Handle GUI for skipping
        if (skippingQuestions) {
            // Add skip button, if skipping allowed
            controlsPanel.add(skipButton);
        } else {
            // If answering skipped questions tell user
            JLabel skipLabel = new JLabel();
            skipLabel.setText("Skipped Question " + (questionCounter + 1) + "/" + questions.length);
            questionPanel.add(skipLabel);
        }
        controlsPanel.add(restartButton);

        questionPanel.add(questionLabel);
        // Load frame
        mainPanel.add(questionPanel, BorderLayout.NORTH);
        mainPanel.add(answersPanel, BorderLayout.CENTER);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);  // Centre frame in window
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * Handles all button clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("submit")) {
            if (currentAnswer == null) {     // No answer given, prompt user to answer
                String userPrompt = "You have not answered the current question.";
                userPrompt = userPrompt + "\nYou may skip the question and return to it later on.";
                JOptionPane.showMessageDialog(null, userPrompt );
            } else {                         // Answer supplied, process and move on
                checkAnswer();
                nextQuestion();
            }
        } else if (command.equalsIgnoreCase("skip")) {      // User presses skip
            // Used to handle jagged 2D in case of quiz reset
            if (wrongAnswers[questionCounter].length > maxLenghtWrongAnswersSubArray) {
                maxLenghtWrongAnswersSubArray = wrongAnswers[questionCounter].length;
            }
            // Add to skipped stack
            skipped.push(questionCounter);
            nextQuestion();
        } else if (command.equalsIgnoreCase("restart")) {       // Reset. Back to start screen
            frame.dispose();
            Quiz quiz = new Quiz();

        } else {            // The user has not pressed submit, skip, or quit, so must have selected a new answer.
            currentAnswer = command;
        }
    }

    /**
     * Ask next question or end quiz if no questions left
     */
    public void nextQuestion(){
        frame.dispose();
        currentAnswer = null;       // Nulled to allow prompt if no answer is selected
        questionCounter++;
        if (questionCounter < correctAnswers.length) {              // Next question
            frame = new JFrame("Joe's quiz");
            askQuestion(questions[questionCounter], correctAnswers[questionCounter], wrongAnswers[questionCounter]);
        } else if (skippingQuestions && !(skipped.empty())) {       // Begin answering the skipping questions
            resetQuizForSkippedQuestions();
        } else {                                                    // End quiz
            endQuiz();
        }
    }

    /**
     * Handles reset of quiz to re-ask skipped questions
     */
    private void resetQuizForSkippedQuestions() {
        skippingQuestions = false;      // Turn off skipping

        // Create new data structures for skipped Questions
        int stackSize = skipped.size();         // Cache for array lengths
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
        questionCounter = -1;       // Reset to -1 as nextQuestion() will increment to 0
        nextQuestion();
    }

    /**
     * Generates a report for users at the end of the quiz
     */
    public void endQuiz(){
        // Add final score to message
        String finalMessage =  "Your have scored = "+score+" points.\n This is equivalent to " + (score * 10) + "% \n";
        // Show mistakes (if any made)
        if (mistakesReport.length() > 0) {
            finalMessage = finalMessage + "\nMISTAKES:\n" + mistakesReport;
        }
        finalMessage = finalMessage +  "\n Well Done!";
        JOptionPane.showMessageDialog(null, finalMessage);

        // Start again
        frame.dispose();
        Quiz quiz = new Quiz();
    }

    /**
     * Check answers and handle correct and incorrect answers
     */
    private void checkAnswer() {
        if (currentAnswer == correctAnswers[questionCounter]) {     // Correct
            score = score + 1;
        } else {                                                    // Wrong
            updateMistakeReport();
        }
    }

    /**
     * Combines the correct answer and wrong answers into a single array
     * to allow creation of radio buttons
     */
    private String[] combineAnswers(String correctAnswer, String[] wrongAnswers) {
        // Use a stack to allow for variable lengths of wrong answer arrays
        Stack answerStack = new Stack();
        for (int i = 0; i < wrongAnswers.length; i++) {
            if (wrongAnswers[i] != null) {      // check for null values which may exist in jagged arrays
                answerStack.push(wrongAnswers[i]);
            }
        }
        answerStack.push(correctAnswer);        // Add correct answer to stack

        // Unpack stack into an array
        String[] answerList = new String[answerStack.size()];
        int counter = 0;
        while (!answerStack.empty()) {
            answerList[counter] = (String)answerStack.pop(); // Typecast to string
            counter++;
        }
        jumbleAnswers(answerList);          // Randomise order of answers
        return answerList;
    }

    /**
     * Randomises the answer array so it appears differently for each users
     * Behaves like a shuffle cards (swaps two randomly selected items)
     */
    private void jumbleAnswers(String[] answers) {
        // Shuffle the answers 50 times
        for(int i = 0; i < 50; i++){
            int swapItemOne = (int) Math.floor(Math.random() * answers.length);
            int swapItemTwo = (int) Math.floor(Math.random() * answers.length);
            String temp = answers[swapItemOne];
            answers[swapItemOne] = answers[swapItemTwo];
            answers[swapItemTwo] = temp;
        }
    }

    /**
     * Add incorrect answers to the mistakeReport for display at end of quiz
     */
    private void updateMistakeReport() {
        mistakesReport = mistakesReport + "Q: " + questions[questionCounter];
        mistakesReport = mistakesReport + "\nCorrect answer: " + correctAnswers[questionCounter] + "\n";
    }

}
