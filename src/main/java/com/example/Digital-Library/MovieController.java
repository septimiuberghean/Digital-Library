package com.example.projecto;

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

public class MovieController implements Initializable {
    @FXML
    private Button addFilmButton;
    @FXML
    private Button cancelFilmButton;
    @FXML
    private TextField filmTextField;
    @FXML
    private TextField directorTextField;
    @FXML
    private TextField genreFilmTextField;
    @FXML
    private Label addFilmLabel;
    @FXML
    private ImageView filmImageView;

    private String loggedInUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File film = new File("Film/film.jpg");
        Image filmImage = new Image(film.toURI().toString());
        filmImageView.setImage(filmImage);
    }


    public void setLoggedInUsername(String username) {

        this.loggedInUsername = username;
    }

    @FXML
    private void cancelFilmButtonOnAction(ActionEvent e) {
        gobacknow();
    }

    @FXML
    private void addFilmButtonOnAction(ActionEvent e) {
        if (!filmTextField.getText().isBlank() && !directorTextField.getText().isBlank() && !genreFilmTextField.getText().isBlank()) {
            validateFilm();
        } else {
            addFilmLabel.setText("Complete the info");
        }
    }

    private void validateFilm() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String film = filmTextField.getText();
        String director = directorTextField.getText();
        String genrefilm = genreFilmTextField.getText();

        int user_id = getUserIdByUsername(connectDB, loggedInUsername);

        String insertFields = "INSERT INTO Movies (name, director, genrem) VALUES (?, ?, ?)";
        String insertToMovies = insertFields;

        try {
            PreparedStatement statement = connectDB.prepareStatement(insertToMovies, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film);
            statement.setString(2, director);
            statement.setString(3, genrefilm);

            statement.executeUpdate();

            int movie_id = -1;
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                movie_id = generatedKeys.getInt(1);
            }

            String insertToLibrary = "INSERT INTO Library (user_id, item_type, moviee_id) VALUES (?, 'movie', ?)";
            statement = connectDB.prepareStatement(insertToLibrary);
            statement.setInt(1, user_id);
            statement.setInt(2, movie_id);

            statement.executeUpdate();

            addFilmLabel.setText("Movie Added to Library");
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
    private void gobacknow() {
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
