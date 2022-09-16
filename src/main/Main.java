package main;

import controller.MainScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * @author Annalyse Carson
 */

public class Main extends Application {
    /**
     *
     */

    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(new Scene(root, 920, 450));
        primaryStage.show();

    }

    /**
     *
     * @param args
     */
    public static void main(String[]args ){




        launch(args);
    }
}

