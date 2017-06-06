# Java Quiz Coursework
### Joe Halloran

## 1       Project overview

### 1.1	    Object Oriented Structure

To give the project a logical structure it divides functionality into 3 classes:

#### 1.1.1   Quiz

A higher level class that oversees the entire quiz process.

#### 1.1.2	FileHandler

Manages all file interaction. Get questions and answers from text files and process them into correct data structures.

#### 1.1.3	QuizMaster

Handles the mechanics of asking questions and accepting answers. Includes an option to skip questions. Does most of the heavy lifting of the UI, using Java Swing.

### 1.2 	Additional features

The project implements all basic and advanced requirements in the specification.

It also includes some additional code to allow for:

#### 1.2.1	Any number of questions or answers

The quiz is made of 10 questions in each difficulty mode, but could accommodate:

* Any number of questions, not just 10
* Any number of possible answers, including a different number of possible answers in different questions.

These additional features required some additional code to process variable data sizes into arrays. Examples of this additional code can be found in these methods:

* `FileHandler.readFile()`
    * Uses a `Stack` to check number of questions before initialising and populating a fixed length `String[]` array.
* `FileHandler.getTextAs2DArray()`
    * Requires two loops. One to check maximum length before initialising the array, and then another loop to populate the array.
* `QuizMaster.combineAnswers()`
    * Uses a `Stack` to check number of answers before initialising and populating a fixed length `String[]` array.

#### 1.2.2 Randomises answers for display
Answer options are displayed in a random order each time a question is asked. This is implemented by:

* `QuizMaster.jumbleAnswers()`

## 2       Critical evaluation

The project could be improved in the following ways.

### 2.1     Alternative strategy for processing question and answer data

The additional code to allow for any number of questions or answers obfuscates some of the code and uses additional data structures in a clunky way (e.g. `Stack` in `FileHandler.readFile()` method).

This could have been avoided if question and answer data was handled in a more sophisticated way. This could include.
* Use of an `List` or `ArrayList`.
    * These dynamic data structures may be better able to cope with varying data sizes.
    * These was beyond the scope of the course and I am not sure exactly how to implement these.
* Use of additional classes / objects (e.g. `Question` or `WrongAnswer` class)
    * This could allow more clarity, by making data structure a part of the object oriented structure.

### 2.2     User interface

The Java Swing interface lacks elegance and gives little scope to adjust design (layout, colours, etc…)

An alternative approach could be to use a markup language e.g. HTML and CSS to allow for a more precise design.

### 2.3     Refactoring longer methods

Some methods are quite long, and could benefit from refactoring or splitting into separate methods.

Some of the worst offenders are:
* `QuizMaster.askQuestion()`
* `QuizMaster.resetQuizForSkippedQuestions()`

## 3.       Test plan

|Num    |Test       |Method         |Expected Result        |Actual Result          |Evidence       |
|-------|-----------|---------------|-----------------------|-----------------------|---------------|
|1    |"Easy" button returns easy questions  |Click on "Easy" in home screen         |Q1 is "What is cabin fever?"        |As expected          |Img       |
|2    |"Hard" button returns hard questions  |Click on "Hard" in home screen         |Q1 is "Who likes cheese the most?"        |As expected          |Img       |
|3    |"Quit" buttons exits gracefully       |Click "Quit" in home screen   |"Thanks for playing" dialogue displayed        |As expected          |Img       |
|4    |"OK" submits answer when question selected  |Choose all correct answers         |100% final score        |As expected          |Img       |
|5    |User prompted if "OK" clicked and no answer selected |Click "OK" without answer selected         |Dialogue prompt appears       |As expected          |Img      |
|6    |"Restart" button returns to home screen       |Click on "Restart" button         |Home screen dialogue appears        |As expected          |None       |
|7    |"Skip" button saves skipped questions until the end of the quiz       |Skip Q1 only         |Q1 appears end of quiz as "Skipped Question 1/1"        |As expected          |None       |
|8    |"Skipping" for skipped questions at end of quiz       |Skip Q1 and review at end of quiz         |No "Skip" button in UI        |As expected          |Img       |
|9   |Correct calculates final score      |Answer all questions incorrectly         |Final Score 0% and all questions in mistake report        |As expected          |Img       |
|10   |Correct calculates final score      |Answer all questions correctly         |Final Score 100%        |As expected          |Img       |
|11   |Mistake report generates for all incorrect answers       |Answer all questions incorrectly and check report         |All questions in the report        |As expected          |Img       |
|12   |Pressing OK at end of test goes back to the home screen       |Press "OK" in final report screen         |Returns to home screen        |As expected          |None       |
|13   |Radio buttons for answers are in a random order       |Run the test twice and compare Q1 answer order         |Answers are in a different order        |As expected          |Img       |
