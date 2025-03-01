# Online Examination System

A console-based Java examination system that enables teachers to create and manage exams while students can take tests in a secure, timed environment.

## Features

### For Teachers
- Create and manage multiple-choice exams
- Add questions with options and correct answers
- Set exam duration and marks per question
- View student results and performance statistics

### For Students
- Take timed exams with real-time countdown
- Answer multiple-choice questions (a, b, c, d)
- Receive instant results after submission
- View performance history

## Technical Implementation

- Console-based user interface with enhanced text display
- Timed exams with automatic submission
- Secure user authentication system
- Data persistence using Java serialization
- Thread-safe operations

## Project Structure

```
├── .vscode/           # VS Code configuration
├── bin/               # Compiled classes
├── database/          # Serialized data storage
├── src/               # Source code
│   ├── main/
│   │   ├── App.java   # Application entry point
│   │   ├── models/    # Data models
│   │   │   ├── Exam.java
│   │   │   ├── Question.java
│   │   │   └── Student.java
│   │   └── util/      # Utility classes
│   │       ├── AnimatedText.java
│   │       ├── ConsolePrinter.java
│   │       ├── DatabaseManager.java
│   │       └── Timer.java
└── README.md          # Project documentation
```

## Getting Started

1. Ensure you have Java JDK 8 or higher installed
2. Clone the repository
3. Compile the project using your Java compiler
4. Run the application with `java -cp bin main.App`

## Usage

The application provides a text-based menu system for:
- User login (student/teacher)
- Exam creation and management
- Taking exams with a countdown timer
- Viewing results and statistics

## Technical Notes

- `AnimatedText.java`: Provides text animations for the console interface
- `ConsolePrinter.java`: Handles formatted console output
- `DatabaseManager.java`: Manages data persistence through serialization
- `Timer.java`: Implements countdown functionality for timed exams
- `Exam.java`: Represents exam structure with questions and settings
- `Question.java`: Models multiple-choice questions with options
- `Student.java`: Stores student information and exam results