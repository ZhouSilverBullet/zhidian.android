package com.sdxxtop.zhidian.course;

/**
 *
 */
public class CourseModel {

    private int id; //课程ID
    private String courseName;
    private int section; //从第几节课开始
    private int sectionSpan; //跨几节课
    private int week; //周几
    private String classRoom; //教室
    private String className; //教室
    private String courseFlag; //课程背景颜色

    public CourseModel() {
    }

    public CourseModel(int id, String courseName, int section, int sectionSpan, int week, String className, String courseFlag) {
        this.id = id;
        this.courseName = courseName;
        this.section = section;
        this.sectionSpan = sectionSpan;
        this.week = week;
        this.className = className;
        this.courseFlag = courseFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getSectionSpan() {
        return sectionSpan;
    }

    public void setSectionSpan(int sectionSpan) {
        this.sectionSpan = sectionSpan;
    }

    public String getCourseFlag() {
        return courseFlag;
    }

    public void setCourseFlag(String courseFlag) {
        this.courseFlag = courseFlag;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
