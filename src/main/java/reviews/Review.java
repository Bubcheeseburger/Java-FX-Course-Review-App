package reviews;

public class Review {
    private int score;
    private String comment;
    private String date;
    private int courseID;
    private int userID;

    public Review(int score, String comment, String date, int courseID, int userID) {
        this.score = score;
        this.comment = comment;
        this.date = date;
        this.courseID = courseID;
        this.userID = userID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Course getCourse(){
        CourseService courseService = new CourseService();
        return courseService.getCourseByID(courseID).get();
    }
}
