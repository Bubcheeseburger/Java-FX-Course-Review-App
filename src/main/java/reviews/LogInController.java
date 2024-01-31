package reviews;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.Optional;


public class LogInController {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    protected void handleCloseButton(){
        Platform.exit();
    }
    @FXML
    protected void handleLogInButton(){
        /* To-Do */
        try {
            DatabaseDriver databaseDriver = new DatabaseDriver();
            try{
                databaseDriver.connect();
                Optional<User> user = databaseDriver.getUserByUsername(username.getText());
                databaseDriver.disconnect();
                if (user.isEmpty() || !user.get().isPassword(password.getText())){
                    errorLabel.setText("Incorrect username or password");
                    errorLabel.setTextFill(Color.RED);
                }else {
                    int id = user.get().getId();
                    CurrentUser.setLoggedInUser(new User(id, username.getText(), password.getText()));
                    //This should be good
                    SceneChanger sceneChanger = new SceneChanger();
                    sceneChanger.courseSearch();
                }
            }catch (SQLException e){
                throw new RuntimeException();
            }

        }
        catch (RuntimeException e) {
            errorLabel.setText("Incorrect username or password");
            errorLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    protected void handleCreateAccountButton(){
        /* To-Do */
        try {
            if (password.getText().contains(" ")){
                errorLabel.setText("Password cannot include a space");
                errorLabel.setTextFill(Color.RED);
            }
            else if (password.getText().length() < 8){
                errorLabel.setText("Password must be at least 8 characters");
                errorLabel.setTextFill(Color.RED);
            }
            else {
                //Change potentially?
                DatabaseDriver databaseDriver = new DatabaseDriver();
                try {
                    databaseDriver.connect();
                    databaseDriver.createTables();
                    databaseDriver.addUser(username.getText(), password.getText());
                    databaseDriver.commit();
                    databaseDriver.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                errorLabel.setText("Account created successfully");
                errorLabel.setTextFill(Color.GREEN);
            }
        }catch (RuntimeException e) {
            errorLabel.setText("An account with that username already exists");
            errorLabel.setTextFill(Color.RED);
        }
    }
}
