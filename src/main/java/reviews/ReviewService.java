package reviews;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {
    public void addReviews (int score, String comment, String date, int courseID, int userID){
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.addReview(score, comment, date, courseID, userID);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateReviews (int score, String comment, String date, int courseID, int userID){
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.updateReview(score, comment, date, courseID, userID);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReviews (int courseID, int userID){
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            databaseDriver.deleteReview(courseID, userID);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Review> getReviews(int score, String comment, String date, int courseID, int userID){
        List<Review> reviews;
        DatabaseDriver databaseDriver = new DatabaseDriver();
        try {
            databaseDriver.connect();
            reviews = databaseDriver.getReviews(score, comment, date, courseID, userID);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reviews;
    }
}
