package com.example.projecto;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class SongController implements Initializable {
    @FXML
    private Button addBookButton;
    @FXML
    private Button cancelBookButton;
    @FXML
    private TextField bookNameTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField genreBookTextField;
    @FXML
    private ImageView literatureImageView;
    @FXML
    private Label addBookLabel;

    private String loggedInUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File lit = new File("Literature/coll.jpg");
        Image litImage = new Image(lit.toURI().toString());
        literatureImageView.setImage(litImage);
    }

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    @FXML
    private void cancelBookButtonOnAction(ActionEvent e) {
        goback();
    }

    @FXML
    private void addBookButtonOnAction(ActionEvent e) {
        if (!bookNameTextField.getText().isBlank() && !authorTextField.getText().isBlank() && !genreBookTextField.getText().isBlank()) {
            validateBook();
        } else {
            addBookLabel.setText("Complete the info");
        }
    }

    private void validateBook() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String book = bookNameTextField.getText();
        String author = authorTextField.getText();
        String genrebook = genreBookTextField.getText();

        int user_id = getUserIdByUsername(connectDB, loggedInUsername);

        String insertFields = "INSERT INTO Books (name, author, genreb) VALUES (?, ?, ?)";
        String insertToBooks = insertFields;

        try {
            PreparedStatement statement = connectDB.prepareStatement(insertToBooks, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, book);
            statement.setString(2, author);
            statement.setString(3, genrebook);

            statement.executeUpdate();

            int book_id = -1;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book_id = generatedKeys.getInt(1);
            }

            String insertToLibrary = "INSERT INTO Library (user_id, item_type, bookk_id) VALUES (?, 'book', ?)";
            statement = connectDB.prepareStatement(insertToLibrary);
            statement.setInt(1, user_id);
            statement.setInt(2, book_id);

            statement.executeUpdate();

            addBookLabel.setText("Book Added to Library");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getUserIdByUsername(Connection connectDB, String username) {
        String selectQuery = "SELECT idusers FROM Users WHERE usernames = ?";
        int user_id = -1;

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user_id = resultSet.getInt("idusers");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user_id;
    }

    @FXML
    private void goback() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projecto/options.fxml"));
            Parent root = fxmlLoader.load();

            OptionsController optionsController = fxmlLoader.getController();
            optionsController.setLoggedInUsername(loggedInUsername);

            Scene scene = new Scene(root, 520, 400);
            Stage stage = new Stage();
            stage.setTitle("Options");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
