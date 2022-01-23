package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.ComboBoxDB;
import utils.JDBC;
import utils.UserDB;

/**
 * The main class sets the stage for the application.  It also opens
 * a database connection and leaves this open the entire duration of the application.
 *
 * @author Aaron Kephart
 */
import java.sql.*;

public class Main extends Application {

    /**
     * Starts the application and displays a login form to begin.
     * @param stage the primary display
     * @throws Exception if there is any problem with displaying, an exception will occur.
     */
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Main method to start the connection to the database.  Once application is no longer launched,
     * the database connection will close.
     * @param args arguments to pass to the main method
     * @throws SQLException if there is any problem with starting the SQL connection, an exception will occur.
     */
    public static void main(String[] args) throws SQLException {
        JDBC.makeConnection();
        UserDB.selectUser();
        ComboBoxDB.selectCountry();
        ComboBoxDB.selectDivision();
        ComboBoxDB.selectContact();
        launch(args);
        JDBC.closeConnection();
    }
}
