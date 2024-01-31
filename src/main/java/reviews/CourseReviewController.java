package reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;


import java.time.LocalDate;
import java.util.List;

public class CourseReviewController {
    private ReviewService reviewService;
    @FXML
    public Label courseName;
    @FXML
    public ToggleGroup ratings;
    @FXML
    public TableView<Review> reviews;
    @FXML
    public TextArea comment;
    @FXML
    public Label required;
    @FXML
    public Label message;
    public void initialize(){
        message.setText("Your Review");
        message.setTextFill(Color.BLACK);

        String subject = SelectedCourse.getSelectedCourse().getSubject();
        int number = SelectedCourse.getSelectedCourse().getNumber();
        String title = SelectedCourse.getSelectedCourse().getTitle();
        courseName.setText(subject + " " + number + ": " + title);
        reviewService = new ReviewService();
        getCourseReviews();
        calculateRating();
    }

    private void getCourseReviews(){
        int id  = SelectedCourse.getSelectedCourse().getID();
        List<Review> reviewList = reviewService.getReviews(0, null, null, id, 0);
        ObservableList<Review> observableList = FXCollections.observableList(reviewList);
        reviews.getItems().clear();
        reviews.getItems().addAll(observableList);
    }

    private void calculateRating(){
        int currentCourseID = SelectedCourse.getSelectedCourse().getID();
        List<Review> reviews = reviewService.getReviews(0, null, null,currentCourseID, 0 );
        CourseService courseService = new CourseService();
        if (reviews.isEmpty()){
            //This is to ensure that if all the reviews are deleted, it won't display zero
            courseService.updateCourses(null, currentCourseID);
            return;
        }

        int total = 0;
        for (Review review : reviews){
            total += review.getScore();
        }
        float average = (float) total / (reviews.size());
        String twoDecimalPoint = String.format("%.2f", average);
        courseService.updateCourses(twoDecimalPoint, currentCourseID);
    }

    @FXML
    protected void handleSaveButton(){
        if (ratings.getSelectedToggle() == null){
            required.setTextFill(Color.RED);
        }
        else{
            required.setTextFill(Color.BLACK);
            LocalDate currentDate = LocalDate.now();
            RadioButton selectedButton = (RadioButton) ratings.getSelectedToggle();

            int score = Integer.parseInt(selectedButton.getText());
            String comments = comment.getText();
            String date = currentDate.toString();
            int courseID = SelectedCourse.getSelectedCourse().getID();
            int userID = CurrentUser.getLoggedInUser().getId();
            if (!reviewService.getReviews(0, null, null, courseID, userID).isEmpty()){
                //If we already have a comment on this course by this user, update it
                reviewService.updateReviews(score, comments, date, courseID, userID);
                message.setText("Review updated");
                message.setTextFill(Color.GREEN);
            }else { //Else, make a new comment
                message.setText("Review created");
                message.setTextFill(Color.GREEN);
                reviewService.addReviews(score, comments, date, courseID, userID);
            }
        }
        calculateRating();
        getCourseReviews();
    }

    @FXML
    protected void handleDeleteButton(){
        int courseID = SelectedCourse.getSelectedCourse().getID();
        int userID = CurrentUser.getLoggedInUser().getId();
        if (reviewService.getReviews(0, null, null, courseID, userID).isEmpty()){
            //If we don't have a review, show an error message
            message.setText("Must have a review before deleting");
            message.setTextFill(Color.RED);
        }else {
            message.setText("Review deleted successfully");
            message.setTextFill(Color.GREEN);
            reviewService.deleteReviews(courseID, userID);
        }
        calculateRating();
        getCourseReviews();
    }


    @FXML
    protected void handleBackButton(){
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.courseSearch();
    }
}
