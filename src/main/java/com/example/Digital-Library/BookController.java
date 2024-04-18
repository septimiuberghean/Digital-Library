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

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class BookController implements Initializable {
    @FXML
    private Button addSongButton;
    @FXML
    private Button cancelThisButton;
    @FXML
    private TextField artistTextField;
    @FXML
    private TextField songTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private ImageView artImageView;
    @FXML
    private Label addSongLabel;

    private String loggedInUsername;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File songg = new File("Music/artt.jpg");
        Image songgImage = new Image(songg.toURI().toString());
        artImageView.setImage(songgImage);
    }

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    @FXML
    private void cancelThisButtonOnAction(ActionEvent e) {
        gobackplease();
    }

    @FXML
    private void addSongButtonOnAction(ActionEvent e) {
        if (!artistTextField.getText().isBlank() && !songTextField.getText().isBlank() && !genreTextField.getText().isBlank()) {
            validateSong();
        } else {
            addSongLabel.setText("Complete the info");
        }
    }

    private void validateSong() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String song = songTextField.getText();
        String artist = artistTextField.getText();
        String genre = genreTextField.getText();

        int user_id = getUserIdByUsername(connectDB, loggedInUsername);

        String insertFields = "INSERT INTO Songs (name, artist, genres) VALUES (?, ?, ?)";
        String insertToSongs = insertFields;

        try {
            PreparedStatement statement = connectDB.prepareStatement(insertToSongs, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, song);
            statement.setString(2, artist);
            statement.setString(3, genre);

            statement.executeUpdate();

            int song_id = -1;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                song_id = generatedKeys.getInt(1);
            }

            String insertToLibrary = "INSERT INTO Library (user_id, item_type, songg_id) VALUES (?, 'song', ?)";
            statement = connectDB.prepareStatement(insertToLibrary);
            statement.setInt(1, user_id);
            statement.setInt(2, song_id);

            statement.executeUpdate();

            addSongLabel.setText("Song Added to Library");
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
    private void gobackplease() {
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
