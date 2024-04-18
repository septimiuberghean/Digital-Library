package com.example.projecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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


public class HelloController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField enterUsername;
    @FXML
    private PasswordField enterPassword;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile= new File("Images/libt.jpg");
        Image brandingImage= new Image(brandingFile.toURI().toString());

        brandingImageView.setImage(brandingImage);

        File lockFile= new File("Images/loc.jpg");
        Image lockImage= new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void loginButtonOnAction() {
        if (!enterUsername.getText().isBlank() && !enterPassword.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Complete the username and password");
        }
    }

    public void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "select count(1), usernames from users where usernames='" + enterUsername.getText() + "' and passwords='" + enterPassword.getText() + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(verifyLogin);

            while (result.next()) {
                if (result.getInt(1) == 1) {
                    loginMessageLabel.setText("correct authentication");
                    createOptionsWindow(result.getString("usernames"));
                } else {
                    loginMessageLabel.setText("wrong authentication");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOptionsWindow(String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projecto/options.fxml"));
            Parent root = fxmlLoader.load();

            OptionsController optionsController = fxmlLoader.getController();
            optionsController.setLoggedInUsername(username);

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
