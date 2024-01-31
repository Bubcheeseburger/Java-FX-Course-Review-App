package reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;

public class CourseSearchController {
    private CourseService courseService;
    @FXML
    private Label message;
    @FXML
    private TextField subjectBar;
    @FXML
    private TextField numberBar;
    @FXML
    private TextField titleBar;
    @FXML
    private TextField subjectAdd;
    @FXML
    private TextField numberAdd;
    @FXML
    private TextField titleAdd;
    @FXML
    private TableView<Course> courses;
    public void initialize(){
        courseService = new CourseService();
        getCourses();
        welcome();
    }
    private void welcome(){
        message.setText("Welcome " + CurrentUser.getLoggedInUser().getUsername());
        message.setTextFill(Color.BLACK);
    }
    private void getCourses(){
        List<Course> courseList = courseService.getAllCourses();
        ObservableList<Course> observableList = FXCollections.observableList(courseList);
        courses.getItems().clear();
        courses.getItems().addAll(observableList);
    }
    public boolean containsOnlyLetters(String str) {
        return str != null && str.matches("[A-Z]+");
    }
    public boolean containsOnlyNumbers(String input) {
        return input.matches("[0-9]+");
    }
    private boolean textFieldConstraints (TextField subject, TextField number, TextField title){
        if ((!containsOnlyLetters(subject.getText()) || (subject.getText().length() > 4) || (subject.getText().length() < 2)) & !subject.getText().isEmpty()){
            message.setText("Invalid course format: Subject (2-4  Capital Letters)");
            message.setTextFill(Color.RED);
            return false;
        }
        if ((!containsOnlyNumbers(number.getText()) || number.getText().length() != 4) & !number.getText().isEmpty()){
            message.setText("Invalid course format: Number (4 Digits)");
            message.setTextFill(Color.RED);
            return false;
        }
        if ((title.getText().length() > 50) & !number.getText().isEmpty()){
            message.setText("Invalid course format: Number Title (1-50 Characters)");
            message.setTextFill(Color.RED);
            return false;
        }
        return true;
    }
    @FXML
    protected void handleSearchButton(){
        /* To-Do */
        try {
            if (!textFieldConstraints(subjectBar, numberBar, titleBar)) {
                return;
            }
            String subjectPass = null;
            int numberPass = 0;
            String titlePass = null;
            if (!subjectBar.getText().isEmpty()) {
                subjectPass = subjectBar.getText();
            }
            if (!numberBar.getText().isEmpty()) {
                numberPass = Integer.parseInt(numberBar.getText());
            }
            if (!titleBar.getText().isEmpty()) {
                titlePass = titleBar.getText();
            }
            List<Course> courseList = courseService.getCourses(subjectPass, numberPass, titlePass);
            ObservableList<Course> observableList = FXCollections.observableList(courseList);
            courses.getItems().clear();
            courses.getItems().addAll(observableList);
            //add changing back to welcome
            welcome();
        }
        catch (RuntimeException e){
            message.setText("Course search failed, please report the bug");
            message.setTextFill(Color.RED);
        }
    }
    @FXML
    protected void handleAddCourseButton(){
        /* To-Do */
        try {
            if (subjectAdd.getText().isEmpty() || numberAdd.getText().isEmpty() || titleAdd.getText().isEmpty()){
                message.setText("Please fill in all fields before attempting to add a course");
                message.setTextFill(Color.RED);
                return;
            }
            if (!textFieldConstraints(subjectAdd, numberAdd, titleAdd)){
                return;
            }
            courseService.addCourses(subjectAdd.getText(), Integer.parseInt(numberAdd.getText()), titleAdd.getText());
            message.setText("Course Successfully Added");
            message.setTextFill(Color.GREEN);
            getCourses();
        }catch (RuntimeException e){
            message.setText("Failed to add course, it may already be present");
            message.setTextFill(Color.RED);
        }
    }

    @FXML
    protected void handleLogOutButton(){
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.login();
    }
    @FXML
    protected void handleMyReviewsButton(){
        SceneChanger sceneChanger = new SceneChanger();
        sceneChanger.myReviews();
    }

    @FXML
    protected void handleRowClicked(){
        // Get the selected item from the TableView
        Course selectedCourse = courses.getSelectionModel().getSelectedItem();

        // Handle the selected course or perform desired action
        if (selectedCourse != null) {
            SelectedCourse.setSelectedCourse(selectedCourse);
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.courseReview();
        }
    }

}
