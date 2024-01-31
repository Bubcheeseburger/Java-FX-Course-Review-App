package reviews;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseService {
    public void addCourses (String subject, int number, String title){
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.addCourse(subject, number, title);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCourses (String rating, int courseID){
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.updateCourse(rating, courseID);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getAllCourses(){
        List<Course> courses;
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            courses = databaseDriver.getAllCourses();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }

    public List<Course> getCourses(String subject, int number, String title){
        List<Course> courses;
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            courses = databaseDriver.getCourses(subject, number, title);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }

    public Optional<Course> getCourseByID (int id){
        Optional<Course> course;
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            course = databaseDriver.getCourseByID(id);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return course;
    }
}
