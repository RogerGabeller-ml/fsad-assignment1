package fsd.assignment.assignment1.datamodel;

import javax.security.auth.callback.TextOutputCallback;

public class Student {
    private String studId;
    private String yearOfStudy;
    private String module1;
    private String module2;
    private String module3;

    public Student(String _studId, String _yearOfStudy, String _module1, String _module2, String _module3) {
        this.studId = _studId;
        this.yearOfStudy = _yearOfStudy;
        this.module1 = _module1;
        this.module2 = _module2;
        this.module3 = _module3;
    }

    public String getModule1() {
        return module1;
    }

    public String getModule2() {
        return module2;
    }

    public String getModule3() {
        return module3;
    }

    public String getStudId() {
        return studId;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    @Override
    public String toString() {
        return studId;
    }
}
