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

* FileHandler.readFile()
    * Uses a ```Stack``` to check number of questions before initialising and populating a fixed length ```String[]``` array.
* FileHandler.getTextAs2DArray()
    * Requires two loops. One to check maximum length before initialising the array, and then another loop to populate the array.
* QuizMaster.combineAnswers()
    * Uses a Stack to check number of answers before initialising and populating a fixed length String[] array.

#### 1.2.2 Randomises answers for display
Answer options are displayed in a random order each time a question is asked. This is implemented by:

QuizMaster.jumbleAnswers()

## 2       Critical evaluation
The project could be improved in the following ways.

### 2.1     Alternative strategy for processing question and answer data
The additional code to allow for any number of questions or answers obfuscates some of the code and uses additional data structures in a clunky way (e.g. Stack in FileHandler.readFile() method).

This could have been avoided if question and answer data was handled in a more sophisticated way. This could include.
* Use of an List or ArrayList.
    * These dynamic data structures may be better able to cope with varying data sizes.
    * These was beyond the scope of the course and I am not sure exactly how to implement these.
* Use of additional classes / objects (e.g. Question or WrongAnswer class)
    * This could allow more clarity, by making data structure a part of of the object oriented structure.

### 2.2     User interface
The Java Swing interface lacks elegance and gives little scope to adjust design (layout, colours, etc…)

An alternative approach could be to use a markup language e.g. HTML and CSS to allow for a more precise design.

### 2.3     Refactoring longer methods
Some methods are quite long, and could benefit from refactoring or splitting into separate methods.

Some of the worst offenders are:
* QuizMaster.askQuestion()
* QuizMaster.resetQuizForSkippedQuestions()

## 3.      Test plan