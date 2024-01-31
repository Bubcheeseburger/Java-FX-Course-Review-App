package reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {
    public void login(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("login.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage primaryStage = CourseReviewsApplication.getPrimaryStage(); //This gets the stage we are in
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void courseSearch(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("course_search.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage primaryStage = CourseReviewsApplication.getPrimaryStage(); //This gets the stage we are in
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void courseReview(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("course_review.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage primaryStage = CourseReviewsApplication.getPrimaryStage(); //This gets the stage we are in
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void myReviews(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("my_reviews.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage primaryStage = CourseReviewsApplication.getPrimaryStage(); //This gets the stage we are in
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
