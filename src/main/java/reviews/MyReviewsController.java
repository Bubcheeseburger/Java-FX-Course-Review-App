package reviews;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class MyReviewsController {
    @FXML
    private TableView<Review> myReviews;
    @FXML
    private TableColumn<Review, String> subject;
    @FXML
    private TableColumn<Review, String> number;
    @FXML
    void initialize(){
        getMyReviews();
    }
    @FXML
    protected void getMyReviews(){
        subject.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourse().getSubject()));
        number.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCourse().getNumber())));
        ReviewService reviewService = new ReviewService();
        List<Review> reviews = reviewService.getReviews(0, null, null, 0, CurrentUser.getLoggedInUser().getId());
        ObservableList<Review> observableList = FXCollections.observableList(reviews);
        myReviews.getItems().clear();
        myReviews.getItems().addAll(observableList);
    }
    @FXML
    protected void handleBackButton(){
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.courseSearch();
    }

    @FXML
    protected void handleReviewClicked(){
        Review selectedReview = myReviews.getSelectionModel().getSelectedItem();

        if (selectedReview != null){
            SelectedCourse.setSelectedCourse(selectedReview.getCourse());
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.courseReview();
        }
    }
}
