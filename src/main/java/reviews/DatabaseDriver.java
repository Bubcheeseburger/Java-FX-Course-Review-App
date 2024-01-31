package reviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseDriver {
    private Connection connection;


    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + "course_reviews.sqlite");
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void createTables() throws SQLException {
        if (connection == null || connection.isClosed()){
            throw new IllegalStateException("Must have a connection before making tables");
        }

        String createCoursesTable = "CREATE TABLE IF NOT EXISTS Courses (" +
                "ID INTEGER PRIMARY KEY," +
                "Subject TEXT NOT NULL," +
                "Number INTEGER NOT NULL," +
                "Title TEXT UNIQUE NOT NULL," +
                "Rating TEXT" +
                ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "ID INTEGER PRIMARY KEY," +
                "Username TEXT UNIQUE NOT NULL," +
                "Password TEXT NOT NULL" +
                ");";

        String createReviewsTable = "CREATE TABLE IF NOT EXISTS Reviews (" +
                "ID INTEGER PRIMARY KEY," +
                "Score INTEGER NOT NULL," +
                "Comment TEXT," +
                "Date TEXT NOT NULL," +
                "CourseID INTEGER NOT NULL," +
                "UserID INTEGER NOT NULL," +
                "FOREIGN KEY(CourseID) REFERENCES Courses(ID) ON DELETE CASCADE," +
                "FOREIGN KEY(UserID) REFERENCES Users(ID) ON DELETE CASCADE" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCoursesTable);
            stmt.execute(createUsersTable);
            stmt.execute(createReviewsTable);
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void addCourse(String subject, int number, String title) throws SQLException {
        String insertUserSQL = "INSERT INTO Courses (Subject, Number, Title ) VALUES (?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)){
            preparedStatement.setString(1, subject);
            preparedStatement.setInt(2, number);
            preparedStatement.setString(3, title);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void updateCourse(String rating, int id) throws SQLException {
        String insertUserSQL = "UPDATE Courses SET Rating = ? WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)){
            preparedStatement.setString(1, rating);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public List<Course> getCourses (String subject, int number, String title) throws SQLException{
        List<Course> courses = new ArrayList<>();
        StringBuilder selectCourseBuilder = new StringBuilder("SELECT * FROM Courses");
        boolean hasPreviousClause = false;
        boolean isFirstClause = true;

        if (subject != null) {
            selectCourseBuilder.append(" WHERE");
            isFirstClause = false;
            selectCourseBuilder.append(" Subject = '").append(subject).append("'");
            hasPreviousClause = true;
        }

        if (number != 0) {
            if (isFirstClause){
                selectCourseBuilder.append(" WHERE");
                isFirstClause = false;
            }
            if (hasPreviousClause) {
                selectCourseBuilder.append(" AND");
            }
            selectCourseBuilder.append(" Number = ").append(number);
            hasPreviousClause = true;
        }

        if (title != null) {
            if (isFirstClause){
                selectCourseBuilder.append(" WHERE");
            }
            if (hasPreviousClause) {
                selectCourseBuilder.append(" AND");
            }
            selectCourseBuilder.append(" UPPER(Title) LIKE '%").append(title).append("%'");
        }

        String selectCourse = selectCourseBuilder.toString();

        System.out.println(selectCourse);

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectCourse)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String subjectVar = resultSet.getString("Subject");
                int numberVar = resultSet.getInt("Number");
                String titleVar = resultSet.getString("Title");
                String ratingVar = resultSet.getString("Rating");
                Course course = new Course(id, subjectVar, numberVar, titleVar, ratingVar);
                courses.add(course);
            }
        }catch (SQLException e){
            rollback();
            throw e;
        }
        return courses;

    }

    public Optional<Course> getCourseByID (int id) throws SQLException {
        String selectCourseByIDSQL = "SELECT * FROM Courses WHERE ID = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectCourseByIDSQL)){
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idVar = resultSet.getInt("ID");
                    String subjectVar = resultSet.getString("Subject");
                    int numberVar = resultSet.getInt("Number");
                    String titleVar = resultSet.getString("Title");
                    String ratingVar = resultSet.getString("Rating");
                    Course course = new Course(idVar, subjectVar, numberVar, titleVar, ratingVar);
                    return Optional.of(course);
                }
            }
        } catch(SQLException e){
            rollback();
            throw e;
        }
        return Optional.empty();
    }
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String selectAllCoursesSQL = "SELECT * FROM Courses;";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectAllCoursesSQL)){
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String subject = resultSet.getString("Subject");
                int number = resultSet.getInt("Number");
                String title = resultSet.getString("Title");
                String rating = resultSet.getString("Rating");
                courses.add(new Course(id, subject, number, title, rating));
            }
        } catch (SQLException e) {
            rollback();
            throw e;
        }

        return courses;
    }

    public void addUser(String username, String password) throws SQLException {
        String insertUserSQL = "INSERT INTO Users (Username, Password) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }

    }

    public Optional<User> getUserByUsername(String username) throws SQLException {
        String selectUserByUsername = "SELECT * FROM Users WHERE Username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserByUsername)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int id = resultSet.getInt("ID");
                String usernameVar = resultSet.getString("Username");
                String passwordVar = resultSet.getString("Password");
                User user = new User(id, usernameVar, passwordVar);
                return Optional.of(user);
            }
        }catch (SQLException e){
            rollback();
            throw e;
        }
        return Optional.empty();
    }

    public void addReview(int score, String comment, String date, int courseID, int userID) throws SQLException {
        String insertReviewSQL = "INSERT INTO Reviews (Score, Comment, Date, CourseID, UserID ) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertReviewSQL)){
            preparedStatement.setInt(1,score);
            preparedStatement.setString(2, comment);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, courseID);
            preparedStatement.setInt(5, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void updateReview(int score, String comment, String date, int courseID, int userID) throws SQLException {
        String updateReviewSQL = "UPDATE Reviews SET Score = ?, Comment = ?, Date = ? WHERE CourseID = ? AND UserID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateReviewSQL)) {
            preparedStatement.setInt(1, score);
            preparedStatement.setString(2, comment);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, courseID);
            preparedStatement.setInt(5, userID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void deleteReview(int courseID, int userID) throws SQLException {
        String updateReviewSQL = "DELETE FROM Reviews WHERE CourseID = ? AND UserID = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateReviewSQL)) {
            preparedStatement.setInt(1, courseID);
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public List<Review> getReviews (int score, String comment, String date, int courseID, int userID) throws SQLException{
        List<Review> reviews = new ArrayList<>();
        StringBuilder selectReviewBuilder = new StringBuilder("SELECT * FROM Reviews");
        boolean hasPreviousClause = false;
        boolean isFirstClause = true;

        if (score != 0) {
            selectReviewBuilder.append(" WHERE");
            isFirstClause = false;
            selectReviewBuilder.append(" Score = '").append(score).append("'");
            hasPreviousClause = true;
        }

        if (comment != null) {
            if (isFirstClause){
                selectReviewBuilder.append(" WHERE");
                isFirstClause = false;
            }
            if (hasPreviousClause) {
                selectReviewBuilder.append(" AND");
            }
            selectReviewBuilder.append(" UPPER(Comment) LIKE '%").append(comment).append("%'");
            hasPreviousClause = true;
        }

        if (date != null) {
            if (isFirstClause){
                selectReviewBuilder.append(" WHERE");
                isFirstClause = false;
            }
            if (hasPreviousClause) {
                selectReviewBuilder.append(" AND");
            }
            selectReviewBuilder.append(" Date = ").append(date);
            hasPreviousClause = true;
        }

        if (courseID != 0) {
            if (isFirstClause){
                selectReviewBuilder.append(" WHERE");
                isFirstClause = false;
            }
            if (hasPreviousClause) {
                selectReviewBuilder.append(" AND");
            }
            selectReviewBuilder.append(" CourseID = ").append(courseID);
            hasPreviousClause = true;
        }

        if (userID != 0) {
            if (isFirstClause){
                selectReviewBuilder.append(" WHERE");
            }
            if (hasPreviousClause) {
                selectReviewBuilder.append(" AND");
            }
            selectReviewBuilder.append(" UserID = ").append(userID);
        }

        String selectReview = selectReviewBuilder.toString();

        System.out.println(selectReview);

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectReview)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int scoreVar = resultSet.getInt("Score");
                String commentVar = resultSet.getString("Comment");
                String dateVar = resultSet.getString("Date");
                int courseIDVar = resultSet.getInt("CourseID");
                int userIDVar = resultSet.getInt("UserID");
                Review review = new Review(scoreVar, commentVar, dateVar, courseIDVar, userIDVar);
                reviews.add(review);
            }
        }catch (SQLException e){
            rollback();
            throw e;
        }
        return reviews;

    }

    public void clearTables() throws SQLException {
        String deleteCoursesSQL = "DELETE FROM Courses;";
        String deleteUsersSQL = "DELETE FROM Users;";
        String deleteReviewsSQL = "DELETE FROM Reviews;";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteCoursesSQL);
            stmt.execute(deleteUsersSQL);
            stmt.execute(deleteReviewsSQL);
        } catch (SQLException e){
            rollback();
            throw e;
        }
    }
}
