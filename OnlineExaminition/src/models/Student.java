package models;

import java.io.Serializable;
import java.util.*;
import util.AnimatedText;

public class Student implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String studentId;  
    private String studentName;
    private String password;
    private String email;
    private String phoneNumber;
    private String department;
    private int sem;
    private HashMap<String, Integer> studentMarks; // exam ID and marks
    private final boolean isTeacher;
    // private Map<String, Integer> examMarks;
    
    //student 
    public Student(Scanner sc, AnimatedText animate){
        animate.animateText("Creating a new Student Acc", 25);
        animate.animateText("Enter Student name ", 25);
        this.studentName = sc.nextLine();
        boolean validPassword;
        do {
            animate.animateText("Enter Password ", 25);
            this.password = sc.nextLine();
            validPassword = this.password.length() >= 8 && this.password.matches(".*\\d.*");
            if (!validPassword) 
                animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
        } while (!validPassword);
        animate.animateText("Enter Student Email ", 25);
        this.email = sc.nextLine();
        animate.animateText("Enter Student Phone Number ", 25);
        this.phoneNumber = sc.nextLine();
        animate.animateText("Enter Department ", 25);
        this.department = sc.nextLine();
        animate.animateText("Enter Student ID Number ", 25);
        this.studentId = this.department.substring(0, 3).toUpperCase() + String.format("%03d", sc.nextInt());sc.nextLine();
        animate.animateText("Enter Semester ", 25);
        this.sem = sc.nextInt();sc.nextLine();
        this.studentMarks = new HashMap<>();
        this.isTeacher = false;
        animate.animateText("Account created Successfully ", 25);
    }
    // teacher 
    public Student(Scanner sc, AnimatedText animate, boolean isTeacher){
        animate.animateText("Creating a new Teacher Acc", 25);
        animate.animateText("Enter Teacher name ", 25);
        this.studentName = sc.nextLine();
        boolean validPassword;
        do {
            animate.animateText("Enter Password ", 25);
            this.password = sc.nextLine();
            validPassword = this.password.length() >= 8 && this.password.matches(".*\\d.*");
            if (!validPassword) 
            animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
        } while (!validPassword);
        animate.animateText("Enter Teacher Email ", 25);
        this.email = sc.nextLine();
        animate.animateText("Enter Teacher Phone Number ", 25);
        this.phoneNumber = sc.nextLine();
        animate.animateText("Enter Department ", 25);
        this.department = sc.nextLine();
        animate.animateText("Enter Teacher ID Number ", 25);
        this.studentId = this.department.substring(0, 3).toUpperCase() + String.format("%03d", sc.nextInt());sc.nextLine();
        this.isTeacher = true;
        animate.animateText("Account created Successfully ", 25);
    }

    public void addMarks(String subject, int marks){
        this.studentMarks.put(subject, marks);
    }

    public void displayResults(AnimatedText animate, List<Exam> exams) {
        if (studentMarks.isEmpty()) {
            animate.animateText("Student has not attended any exams", 25);
            return;
        }
        animate.animateText(this.studentId + "\t" + this.studentName + "\t" + this.department + "\t" + this.sem + " sem" + "\n", 25);
        animate.animateText(String.format("%-15s %-15s %-15s %-10s", "Subject", "Obtained Marks", "Total Marks", "Result"), 25);
        for (String examID : studentMarks.keySet()) {
            Exam filteredExam = exams.stream()
                .filter(exam -> exam.getExamID().equalsIgnoreCase(examID))
                .findFirst().orElse(null);
            animate.animateText(String.format("%-15s %-15d %-15d %-10s", 
            filteredExam.getSubject(), 
            studentMarks.get(examID),
            filteredExam.getTotalMarks(),
            studentMarks.get(examID) > 0.35 * filteredExam.getTotalMarks() ? "PASS" : "FAIL"), 25);
        }
    }

    public void displayStudentDetails(AnimatedText animate){
        animate.animateText("Student Name :"+this.studentName,25);
        animate.animateText("Student ID :"+this.studentId,25);
        animate.animateText("Student Email :"+this.email,25);
        animate.animateText("Student Phone Number :"+this.phoneNumber,25);
        animate.animateText("Student Department :"+this.department,25);
        animate.animateText("Student Semester :"+this.sem,25);
    }

    // setters 
    public void setName(String name){ this.studentName = name; }
    public void setPassword(String password){ this.password = password; }
    public void setEmail(String email){ this.email = email; }
    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }
    public void setStudentId(String studentId){ this.studentId = studentId; }
    public void setDept(String department){ this.department = department; }
    public void setSem(int sem){ this.sem = sem; }
    public void setMarks(String examID, int marks){ this.studentMarks.put(examID, marks); }

    // getters
    public String getName(){ return this.studentName; }
    public String getPassword(){ return this.password; }
    public String getEmail(){ return this.email; }
    public String getPhone(){ return this.phoneNumber; }
    public String getDept(){ return this.department; }
    public String getStudentID(){ return this.studentId; }
    public int getStudentSem(){ return this.sem; }
    public boolean isTeacher(){ return this.isTeacher; }
    public Integer getExamMark(String examId) { return this.studentMarks.get(examId) != null ? this.studentMarks.get(examId) : 0; }

    public void addExamMark(String examId, int marks) {
        studentMarks.put(examId, marks);
    }
}