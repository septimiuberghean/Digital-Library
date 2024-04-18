package com.example.projecto;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class OptionsController implements Initializable {
    @FXML
    private Button addBookButton;
    @FXML
    private Button addSongButton;
    @FXML
    private Button addMovieButton;
    @FXML
    private Button cancellationButton;
    @FXML
    private ImageView tvImageView;
    @FXML
    private ImageView bokImageView;
    @FXML
    private ImageView listeningImageView;
    @FXML
    private Label ddSong;
    @FXML
    private Label ddBook;
    @FXML
    private Label ddMo;

    private String loggedInUsername;

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File television = new File("Simpson/TV.jpeg");
        Image televisionImage = new Image(television.toURI().toString());
        tvImageView.setImage(televisionImage);

        File read = new File("Simpson/bok.jpg");
        Image readImage = new Image(read.toURI().toString());
        bokImageView.setImage(readImage);

        File listening = new File("Simpson/listening.jpg");
        Image listeningImage = new Image(listening.toURI().toString());
        listeningImageView.setImage(listeningImage);
    }

    @FXML
    private void addBookButtonOnAction(ActionEvent event) {
        ddBook.setText("u're adding book");
        toBooks();
    }

    @FXML
    private void addSongButtonOnAction(ActionEvent event) {
        ddSong.setText("u're adding song");
        toSongs();
    }

    @FXML
    private void addMovieButtonOnAction(ActionEvent event) {
        ddMo.setText("u're adding movie");
        toFilms();
    }

    public void cancellationButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancellationButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void toSongs() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projecto/book.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            BookController bookController = fxmlLoader.getController();
            bookController.setLoggedInUsername(loggedInUsername);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toBooks() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projecto/song.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);


            SongController songController = fxmlLoader.getController();
            songController.setLoggedInUsername(loggedInUsername);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toFilms() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projecto/movie.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            // Pass the logged-in username to MovieController
            MovieController movieController = fxmlLoader.getController();
            movieController.setLoggedInUsername(loggedInUsername);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

