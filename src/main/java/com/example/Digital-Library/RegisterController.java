package com.example.projecto;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ImageView vynImageView;
    @FXML
    private ImageView movieImageView;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label conclusionMessage;
    @FXML
    private Label matchingMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File vinyls = new File("ImagesR/vinyls.jpg");
        Image vynImage = new Image(vinyls.toURI().toString());
        vynImageView.setImage(vynImage);

        File movies = new File("ImagesR/posters.jpg");
        Image moviesImage = new Image(movies.toURI().toString());
        movieImageView.setImage(moviesImage);

        File books = new File("ImagesR/collection.jpg");
        Image bookImage = new Image(books.toURI().toString());
        bookImageView.setImage(bookImage);
    }

    public void closeButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void registerButtonOnAction(ActionEvent e) {
        if (setPasswordField.getText().equals(confirmPasswordField.getText())) {
            registerUser();
            matchingMessage.setText("Matching");
            createAccountForm();
        } else {
            matchingMessage.setText("NOT MATCHING");
        }
    }

    public void registerUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String username = usernameTextField.getText();
        String password = setPasswordField.getText();
        String insertFields = "INSERT INTO users (usernames, passwords) VALUES ('";
        String insertValues = username + "', '" + password + "')";
        String insertToRegister = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
            conclusionMessage.setText("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void createAccountForm(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projecto/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage stage=new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}