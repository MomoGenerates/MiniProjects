package main;

import java.util.*;
import java.util.stream.Collectors;
import models.*;
import util.*;

public class App {
    private static DatabaseManager databaseManager;
    private static boolean userExist = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AnimatedText animate = new AnimatedText();
        databaseManager = new DatabaseManager();
        
        while (true){
            List<Student> students = databaseManager.loadStudents();
            List<Teacher> teachers = databaseManager.loadTeachers();
            System.out.println("\033[H\033[2J");
            animate.animateText("Welcome to Online Examination System ", 25);
            
            while (!userExist){
                animate.animateText("Logging in ", 25);
                animate.animateText("Are you a student or a Teacher ", 25);
                String initialInput = sc.nextLine();
                
                if (initialInput.equalsIgnoreCase("student")) {
                    animate.animateText("Enter your username: ", 25);
                    String tempUser = sc.nextLine();
                    
                    Optional<Student> existingStudent = students.stream()
                        .filter(student -> student.getName().equalsIgnoreCase(tempUser))
                        .findFirst();
                        
                    existingStudent.ifPresentOrElse(
                        student -> { 
                            boolean validateUser = false;
                        while (!validateUser) {
                            animate.animateText("Enter your Password: ", 25);
                            final String userPassword = sc.nextLine();
                            
                            if (student.authenticate(tempUser, userPassword)) {
                                animate.animateText("Welcome " + tempUser, 25);
                                validateUser = true;
                                studentMenu(sc, animate, student);
                            } else animate.animateText("Invalid Password ", 25);
                        }
                    },
                    () -> {
                        animate.animateText("Username not found. Would you like to create a new account?", 25);
                        if (sc.nextLine().equalsIgnoreCase("Yes")) {
                            Student newStudent = new Student(sc, animate);
                            students.add(newStudent);
                            databaseManager.saveStudents(students);
                            studentMenu(sc, animate, newStudent);  
                        }
                    }); 
                } else if (initialInput.equalsIgnoreCase("teacher")){
                    animate.animateText("Enter your username: ", 25);
                    String tempUser = sc.nextLine();
                    
                    Optional<Teacher> existingTeacher = teachers.stream()
                        .filter(teacher -> teacher.getName().equalsIgnoreCase(tempUser))
                        .findFirst();
                        
                    existingTeacher.ifPresentOrElse(
                    teacher -> {
                        boolean validateUser = false;
                        while (!validateUser) {
                            animate.animateText("Enter your Password: ", 25);
                            final String userPassword = sc.nextLine();
                            
                            if (teacher.authenticate(tempUser, userPassword)) {
                                animate.animateText("Welcome " + tempUser, 25);
                                validateUser = true;
                                teacherMenu(sc, animate, teacher);
                            } else animate.animateText("Invalid Password ", 25);
                        }
                    },
                    () -> {
                        animate.animateText("Username not found. Would you like to create a new account?", 25);
                        if (sc.nextLine().equalsIgnoreCase("Yes")) {
                            Teacher newTeacher = new Teacher(sc, animate);
                            teachers.add(newTeacher);
                            databaseManager.saveTeachers(teachers);
                            teacherMenu(sc, animate, newTeacher);
                        }
                    });

                } else if (initialInput.equalsIgnoreCase("exit") || initialInput.equalsIgnoreCase("log out")) {
                    databaseManager.saveStudents(students);
                    databaseManager.saveExams(databaseManager.loadExams());
                    System.exit(0);
                }
            }
        }
    }

    public static void studentMenu(Scanner sc, AnimatedText animate, Student currentStudent){
        List<Exam> exams = databaseManager.loadExams();
        List<Student> students = databaseManager.loadStudents();

        animate.animateText("""
                            ===== Student Menu =====
                            \u2192 Update Profile
                            \u2192 Take Exam
                            \u2192 View Previous Results
                            \u2192 Change Password
                            \u2192 Logout""", 25);

        while(true){
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" ->{
                    currentStudent.displayStudentDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Update Department
                            \u2192 Update Semester
                            \u2192 Back to Main Menu""", 25);
                    while(true){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentStudent.setName(sc.nextLine());
                                databaseManager.saveStudents(students);
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentStudent.setEmail(sc.nextLine());  
                                databaseManager.saveStudents(students);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentStudent.setPhone(sc.nextLine());  
                                databaseManager.saveStudents(students);
                            }
                            case "dept" ->{
                                animate.animateText("Enter the new Department ", 25);
                                currentStudent.setStudentDept(sc.nextLine());  
                                databaseManager.saveStudents(students);
                            }
                            case "sem" ->{
                                animate.animateText("Enter the new Semester ", 25);
                                currentStudent.setStudentSem(sc.nextInt());sc.nextLine();
                                databaseManager.saveStudents(students);
                            }
                            case "exit", "back" ->{
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "take exam", "exam" ->{
                    List<Exam> availableExams = exams.stream()
                    .filter(exam -> !exam.getAttemptTracker().contains(currentStudent.getStudentID()) &&
                    exam.getExamSem() == currentStudent.getStudentSem())
                    .collect(Collectors.toList());

                    if (availableExams.isEmpty()) animate.animateText("No available exams", 25);
                    else {
                        animate.animateText("Available exams to take ~", 25);
                        animate.animateText(String.format("%-10s %-15s %-15s %-20s %-6s",
                            "Exam ID", "Subject", "Total Marks", "Teacher", "Time"), 25);

                        availableExams.forEach(exam -> 
                            animate.animateText(String.format("%-10s %-15s %-15s %-20s %-6s",
                                            exam.getExamID(), exam.getSubject(), exam.getTotalMarks(), 
                                            exam.getCreatedBy(), exam.getExamTime() + " min"), 25));

                        animate.animateText("Enter the ID of exam you'd like to take ", 25);
                        availableExams.stream()
                            .filter(exam -> exam.getExamID().equalsIgnoreCase(sc.nextLine()))
                            .findFirst()
                            .ifPresentOrElse(
                                exam -> exam.startExam(sc, animate, currentStudent),
                                () -> animate.animateText("Wrong Exam ID", 25)
                            );
                    }
                }
                case "results", "view results" ->{
                    currentStudent.displayResults(animate,exams);
                }
                case "change passwrod" ->{
                    boolean correctPassword = false;
                    int PasswordLimit = 0;
                    while(!correctPassword && PasswordLimit <4){
                        animate.animateText("Enter old Password", 25);
                        if(currentStudent.getPassword().equals(sc.nextLine())){
                            correctPassword = true;
                            boolean validPassword;
                            String newPassword;
                            do{
                                animate.animateText("Enter New Password", 25);
                                newPassword = sc.nextLine();
                                validPassword = newPassword.length() < 8 || !newPassword.matches(".*\\d.*");
                                animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
                                } while(validPassword);
                            animate.animateText("Confirm New Password", 25);
                            if(newPassword.equals(newPassword)){
                                currentStudent.setPassword(newPassword);
                                databaseManager.saveStudents(students);
                            }
                        } else {
                            animate.animateText("Wrong Password", 25);
                            PasswordLimit++;
                            if(PasswordLimit > 3) animate.animateText("Too many Attempts",25);
                        }
                    }
                }
                case "log out", "exit" ->{
                    databaseManager.saveStudents(students);
                    databaseManager.saveExams(exams);
                    animate.animateText("Logging out ", 25);
                    userExist = false;
                    System.out.println("\033[H\033[2J");
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }

    public static void teacherMenu(Scanner sc, AnimatedText animate, Teacher currentTeacher){
        List<Teacher> teachers = databaseManager.loadTeachers();
        List<Student> students = databaseManager.loadStudents();
        List<Exam> exams = databaseManager.loadExams();

        animate.animateText("""
                            ===== Teacher Menu =====
                             \u2192 Update Profile
                             \u2192 Create New Exam
                             \u2192 Manage Questions
                             \u2192 View Student Results
                             \u2192 Set Exam Timer
                             \u2192 Generate Result Reports
                             \u2192 Change Password
                             \u2192 Logout""", 25);
        while (true) { 
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" ->{
                    currentTeacher.displayTeacherDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Update Department
                            \u2192 Back to Main Menu""", 25);
                    while(true){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentTeacher.setName(sc.nextLine());
                                databaseManager.saveTeachers(teachers);  
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentTeacher.setEmail(sc.nextLine());  
                                databaseManager.saveTeachers(teachers);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentTeacher.setPhone(sc.nextLine());  
                                databaseManager.saveTeachers(teachers);
                            }
                            case "dept" ->{
                                animate.animateText("Enter the new Department ", 25);
                                currentTeacher.setTeacherDept(sc.nextLine());  
                                databaseManager.saveTeachers(teachers);
                            }
                            case "exit", "back" ->{
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "new exam", "create exam" ->{
                    Exam newExam = new Exam(sc, animate, currentTeacher);
                    newExam.createExam(sc, animate);
                    exams.add(newExam);
                    databaseManager.saveExams(exams);
                }
                case "manage", "manage questions" ->{
                    List<Exam> filteredExams = exams.stream()
                        .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(currentTeacher.getName()))
                        .collect(Collectors.toList());

                    animate.animateText("Available Exams ", 25);
                    filteredExams.forEach(exam -> animate.animateText(exam.getSubject(), 25));
                    animate.animateText("Enter the Exam to manage ", 25);
                    filteredExams.stream()
                        .filter(exam -> exam.getSubject().equalsIgnoreCase(sc.nextLine()))
                        .findFirst()
                        .ifPresentOrElse(exam -> {
                            exam.displayQuestions(animate);  
                            animate.animateText(""" 
                                        Would you like to     
                                         \u2192 Add Questions
                                         \u2192 Update Questions
                                         \u2192 Delete Questions
                                         \u2192 Exit
                                        """, 25);
                            while(true) {
                                animate.animateText("Enter your choice ", 25);
                                switch(sc.nextLine()){
                                    case "add" ->{
                                        exam.addQuestion(sc, animate);
                                        animate.animateText("Successfully added Question ", 25);
                                        databaseManager.saveExams(exams); 
                                    }
                                    case "update" ->{
                                        exam.updateQuestion(sc, animate);
                                        animate.animateText("Successfully updated Question ", 25);
                                        databaseManager.saveExams(exams);  
                                    }
                                    case "delete" ->{
                                        exam.deleteQuestion(sc, animate);
                                        animate.animateText("Successfully deleted Question ", 25);
                                        databaseManager.saveExams(exams); 
                                    }
                                    case "exit", "back" ->{
                                        break;
                                    }
                                    default -> animate.animateText("Invalid input", 25);
                                }
                                exam.displayQuestions(animate);
                            }
                        },
                        () -> animate.animateText("Wrong Exam ", 25)
                        );
                }
                case "results report", "generate reulsts report" ->{
                    List<Exam> filteredExams = exams.stream()
                        .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(currentTeacher.getName()))
                        .collect(Collectors.toList());
                    if(filteredExams.isEmpty())  animate.animateText("No avaliable exams", 25);
                    else {
                        filteredExams.forEach(exam -> 
                            animate.animateText(String.format("%-10s %-20s", 
                                exam.getExamID(), 
                                exam.getSubject()), 25)
                        );
                        animate.animateText("Enter Exam ID", 25);
                        exams.stream()
                            .filter(exam -> exam.getExamID().equalsIgnoreCase(sc.nextLine()))
                            .findFirst()
                            .ifPresentOrElse(exam -> exam.resultsReport(animate),
                            () -> animate.animateText("Exam not found!", 25)
                        );
                    }
                }

                case "student result", "view student results" ->{
                    animate.animateText(String.format("%-12s  %-20s  %-12s  %-10s", "Student ID", "Name", "Department", "Semester"), 25);
                    students.forEach(student -> animate.animateText(String.format("%-12s  %-20s  %-12s  %-10s",
                            student.getStudentID(), 
                            student.getName(), 
                            student.getStudentDept(), 
                            student.getStudentSem()), 25));
                    animate.animateText("Enter the Student ID to view results", 25);
                    students.stream()
                        .filter(student -> student.getStudentID().equalsIgnoreCase(sc.nextLine()))
                        .findFirst()
                        .ifPresentOrElse(exam -> exam.displayResults(animate, exams),
                        () -> animate.animateText("Student not found!", 25)
                        );
                }
                case "set timer", "timer" ->{
                    List<Exam> filteredExams = exams.stream()
                        .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(currentTeacher.getName()))
                        .collect(Collectors.toList());
                        if(filteredExams.isEmpty()){
                            animate.animateText("No avaliable exams", 25);
                            break;
                        }
                    animate.animateText(String.format("%-15s %-10s", "Exam Subject", "Exam time"), 25);
                    filteredExams.forEach(exam -> animate.animateText(String.format("%-15s %-10s",
                        exam.getSubject(), exam.getExamTime()), 25));
                    animate.animateText("Enter the Exam to change Time ", 25);
                    filteredExams.stream()
                        .filter(exam -> exam.getSubject().equalsIgnoreCase(sc.nextLine()))
                        .findFirst()
                        .ifPresent(exam -> {
                            animate.animateText("Enter new exam time in minutes: ", 25);
                            exam.setExamTime(sc.nextInt());sc.nextLine(); 
                            databaseManager.saveExams(exams);
                        });
                    }
                case "change passwrod", "password" ->{
                    boolean correctPassword = false;
                    int PasswordLimit = 0;
                    while(!correctPassword && PasswordLimit <4){
                        animate.animateText("Enter old Password", 25);
                        if(currentTeacher.getPassword().equals(sc.nextLine())){
                            correctPassword = true;
                            boolean validPassword;
                            String newPassword;
                            do{
                                animate.animateText("Enter New Password", 25);
                                newPassword = sc.nextLine();
                                validPassword = newPassword.length() < 8 || !newPassword.matches(".*\\d.*");
                                animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
                                } while(validPassword);
                            animate.animateText("Confirm New Password", 25);
                            if(newPassword.equals(newPassword)) {
                                currentTeacher.setPassword(newPassword);
                                databaseManager.saveStudents(students);
                            } 
                        } else {
                            animate.animateText("Wrong Password", 25);
                            PasswordLimit++;
                            if(PasswordLimit > 3) animate.animateText("Too many Attempts",25);
                        }
                    }
                }
                case "log out", "exit" ->{
                    databaseManager.saveStudents(students);
                    databaseManager.saveExams(exams);
                    animate.animateText("Logging out ", 25);
                    userExist = false;
                    System.out.println("\033[H\033[2J");
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }
}