import javax.swing.*;
import javax.swing.border.Border;
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

    public String currentAnswer = null; // Current selected answer (radio button)

    // Data structures for questions and answers
    public String[] questions;
    public String[] correctAnswers;
    public String[][] wrongAnswers;

    // A place holder array to handle quiz reset for cases where a different number of answers are supplied for different questions
    int maxLenghtWrongAnswersSubArray = 0;

    // Data structures for skipped and incorrectly answered questions
    public String mistakesReport = "";
    public Stack skipped = new Stack();

    // Display Frame for quiz
    public JFrame frame = new JFrame();

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
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);  // Centre frame in window
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Handle all button clicks
        if (command.equalsIgnoreCase("submit")) {
            if (currentAnswer == null) {
                // No answer given
                // Tell user and reask question
                JOptionPane.showMessageDialog(null, "Please pick an answer or press skip");
                questionCounter--;
                nextQuestion();
            } else {
                // Process answer and move on
                checkAnswer();
                nextQuestion();
            }
        } else if (command.equalsIgnoreCase("skip")) {
            // Update max sub array length for quiz reset later
            if (wrongAnswers[questionCounter].length > maxLenghtWrongAnswersSubArray) {
                maxLenghtWrongAnswersSubArray = wrongAnswers[questionCounter].length;
            }
            skipped.push(questionCounter);
            nextQuestion();
        } else if (command.equalsIgnoreCase("restart")) {
            // Start again;
            frame.dispose();
            Quiz quiz = new Quiz();

        } else {
            // The user has not pressed submit, skip, or quit, so must have selected a new answer.
            currentAnswer = command;
        }
    }

    // Ask next question or end quiz if no questions left
    public void nextQuestion(){
        frame.dispose();
        currentAnswer = null;
        questionCounter++;
        System.out.println(questionCounter);
        if (questionCounter < correctAnswers.length) {
            frame = new JFrame("Joe's quiz");
            askQuestion(questions[questionCounter], correctAnswers[questionCounter], wrongAnswers[questionCounter]);
        } else if (skippingQuestions && !(skipped.empty())) {
            // Begin answering the skipping questions
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
        // Output final message
        String finalMessage =  "Your have scored = " + score + " points.\n This is equivalent to " + (score * 10) + "% \n";
        if (mistakesReport.length() > 0) {
            finalMessage = finalMessage + "\nMISTAKES:\n" + mistakesReport;
        }
        finalMessage = finalMessage +  "\n Well Done!";
        JOptionPane.showMessageDialog(null, finalMessage);
        System.exit(0);
    }

    // Check answers and handle correct and incorrect answers
    private void checkAnswer() {
        if (currentAnswer == correctAnswers[questionCounter]) {
            // Correct
            score = score + 1;
        } else {
            // Wrong
            updateMistakeReport();
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

    private void updateMistakeReport() {
        mistakesReport = mistakesReport + "Q: " + questions[questionCounter] + "\n Correct answer: " + correctAnswers[questionCounter] + "\n";
    }

}
