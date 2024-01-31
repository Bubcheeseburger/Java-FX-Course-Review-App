package reviews;

import java.util.List;

public class Course {
    private int id;
    private String subject;
    private int number;
    private String title;
    private String rating;
    private List<Review> courseReviews;
    public Course(int id, String subject, int number, String title, String rating) {
        this.id = id;
        this.subject = subject;
        this.number = number;
        this.title = title;
        this.rating = rating;
    }
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    public List<Review> getCourseReviews() {
        return courseReviews;
    }

    public void setCourseReviews(List<Review> courseReviews) {
        this.courseReviews = courseReviews;
    }


}
