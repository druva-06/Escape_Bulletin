package com.example.escapebulletin;

public class Faculty {
    private String facultyKey,facultyName,facultyEmail,facultyMobile;

    public  Faculty(){}

    public Faculty(String facultyKey, String facultyName, String facultyEmail, String facultyMobile) {
        this.facultyKey = facultyKey;
        this.facultyName = facultyName;
        this.facultyEmail = facultyEmail;
        this.facultyMobile = facultyMobile;
    }

    public String getFacultyKey() {
        return facultyKey;
    }

    public void setFacultyKey(String facultyKey) {
        this.facultyKey = facultyKey;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getFacultyEmail() {
        return facultyEmail;
    }

    public void setFacultyEmail(String facultyEmail) {
        this.facultyEmail = facultyEmail;
    }

    public String getFacultyMobile() {
        return facultyMobile;
    }

    public void setFacultyMobile(String facultyMobile) {
        this.facultyMobile = facultyMobile;
    }
}
