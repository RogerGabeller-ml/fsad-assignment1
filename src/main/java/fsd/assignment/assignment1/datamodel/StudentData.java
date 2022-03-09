package fsd.assignment.assignment1.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class StudentData {
    private static StudentData instance;
    private static String filename = "student-data.txt";

    private ObservableList<Student> students;

    public static StudentData getInstance() {
        if (instance == null) {
            instance = new StudentData();
        }
        return instance;
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void loadStudentData() throws IOException {
        students = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] studentItem = input.split("\t", 5);
                String studId = studentItem[0];
                String yearStudy = studentItem[1];
                String mod1 = studentItem[2];
                String mod2 = studentItem[3];
                String mod3 = studentItem[4];
                Student studDataItem = new Student(studId, yearStudy, mod1, mod2, mod3);
                students.add(studDataItem);
                //>include the statement here
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void storeStudentData() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<Student> it = getStudents().iterator();
            while (it.hasNext()) {
                Student item = it.next();
                bw.write(String.format("%s\t%s\t%s\t%s\t%s\t", item, item.getYearOfStudy(),
                        item.getModule1(), item.getModule2(), item.getModule3()));
                bw.newLine();
                //>insert statement here
            }
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public void addStudentData(Student studentToAdd){
        students.add(studentToAdd);
    }
    public void deleteStudent(Student stu){
        students.remove(stu);
    }
}
