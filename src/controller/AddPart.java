package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import javafx.scene.control.Alert.AlertType;
import model.OutSourced;
import model.Part;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPart implements Initializable {

    public RadioButton inHouseButton;
    public Label inhouseOrOut;
    public RadioButton outSourcedButton;
    public ToggleGroup radioGroup;
    public TextField partnameText;
    public TextField partStockText;
    public TextField partPriceText;
    public TextField partMaxText;
    public TextField partMinText;
    public TextField companyOrMachine;
    public TextField partIdText;
    private int Id;

    /**
     * Changes label for inhouse to machine ID
     * @param event
     */
    public void InhouseRadioButton(ActionEvent event) {
        inhouseOrOut.setText("Machine ID");
    }

    /**
     * Changes label for outScoured to Company Name
     * @param event
     */
    public void outSourceButton(ActionEvent event) {
        inhouseOrOut.setText("Company Name");
    }

    /**
     *
     * @param actionEvent
     * @throws IOException      Grabs the values inside the text field then saves
     *                          the String and adds values into the Parts table
     *                          Alerts user if they want to save all values
     */
    public void ToSavePart(ActionEvent actionEvent) throws IOException {
        try {

            String name = partnameText.getText();
            double price = Double.parseDouble(partPriceText.getText());
            int stock = Integer.parseInt(partStockText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int machineId;
            String companyName;
            boolean addPartSuccess = false;

            if (!inHouseButton.isSelected() && !outSourcedButton.isSelected()) {
                displayAlert(6);
            }
                if (name.isEmpty()) {
                    displayAlert(5);
                } else {
                    if (minValid(min, max) && inventoryValid(min, max, stock)) {
                        if (inHouseButton.isSelected()) {
                            try {
                                int partid = Inventory.getNewPartID();
                                machineId = Integer.parseInt(companyOrMachine.getText());
                                InHouse newInHousePart = new InHouse(partid, name, price, stock, min, max, machineId);
                                Inventory.addPart(newInHousePart);
                                addPartSuccess = true;
                            } catch (Exception e) {
                                displayAlert(2);
                            }
                        }

                        if (outSourcedButton.isSelected()) {
                            int partId = Inventory.getNewPartID();
                            companyName = companyOrMachine.getText();
                            OutSourced newOutSourcePart = new OutSourced(partId, name, price, stock, min, max, companyName);
                            newOutSourcePart.setCompnayName(companyOrMachine.getText());
                            Inventory.addPart(newOutSourcePart);
                            addPartSuccess = true;
                        }
                        if (addPartSuccess) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Save");
                            alert.setContentText("Are you sure you want to Save?");

                            Optional<ButtonType> result = alert.showAndWait();
                            if(result.isPresent() && result.get() == ButtonType.OK) {
                                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root, 950, 450);
                                stage.setTitle("Back to main Screen");
                                stage.setScene(scene);
                                stage.show();
                            }
                        }

                    }
                }
            }catch(Exception e){
                displayAlert(1);
            }


    }

    /**
     *
     * @param actionEvent
     * @throws IOException Alerts the user if they would like to cancel the modified part.
     * If the Ok button is pressed it will not save the current text fields adn return to the
     * main screen
     */
    public void ToCancelAdd(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to Cancel");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 950, 450);
            stage.setTitle("Back to main Screen");
            stage.setScene(scene);
            stage.show();
        }

    }

    private boolean minValid(int min, int max) {

        boolean isValid = true;

        if (min <= 0 || min > max) {
            isValid = false;
            displayAlert(3);
        }

        return isValid;
    }
    private boolean inventoryValid(int min, int max, int stock) {

        boolean isValid = true;

        if (stock < min || stock > max) {
            isValid = false;
            displayAlert(4);
        }

        return isValid;
    }
public void displayAlert(int alertType){
        Alert alert = new Alert(AlertType.ERROR);
       switch (alertType){
           case 1:
               alert.setTitle("Error");
               alert.setHeaderText("Error Adding Part");
               alert.setContentText("Text Field empty or invaild values.");
               alert.showAndWait();
               break;
           case 2:
               alert.setTitle("Error");
               alert.setHeaderText("Invaild machine ID value");
               alert.setContentText("Machine ID must only contain numbers.");
               alert.showAndWait();
               break;
           case 3:
               alert.setTitle("Error");
               alert.setHeaderText("Invaild Min value");
               alert.setContentText("Min must only contain numbers and be greater than 0.");
               alert.showAndWait();
               break;
           case 4:
               alert.setTitle("Error");
               alert.setHeaderText("Invaild Inventory value");
               alert.setContentText("Inventory must be between Max and Min.");
               alert.showAndWait();
               break;
           case 5:
               alert.setTitle("Error");
               alert.setHeaderText("Empty");
               alert.setContentText("Name can not be Empty.");
               alert.showAndWait();
               break;
           case 6:
               alert.setTitle("Error");
               alert.setHeaderText("In House or Out Sourced");
               alert.setContentText("Must choose inHouse or OutSourced");
               alert.showAndWait();
               break;


       }
}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
