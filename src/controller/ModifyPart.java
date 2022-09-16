package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPart implements Initializable {
    public Label inHouseOrOut;
    public ToggleGroup RGroup;
    public RadioButton inHouseButton;
    public RadioButton outSourcedButton;
    public TextField partNameText;
    public TextField partStockText;
    public TextField partPriceText;
    public TextField partMaxText;
    public TextField partCompanyOrMachine;
    public TextField partMinText;
    public TextField partIdText;
    private Part selectedPart;

    /**
     *
     * @param actionEvent Grabs the selected row and puts the values into the appropriate
     *                    text fields. Saves any changes and deletes the last selected row and replaces
     *                    it with the modified version.
     *                    Alerts user if they want to save all values
     */
    public void ToSaveModify(ActionEvent actionEvent) {
        try {

            String name = partNameText.getText();
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
                            int partid = selectedPart.getId();
                            machineId = Integer.parseInt(partCompanyOrMachine.getText());
                            InHouse newInHousePart = new InHouse(partid, name, price, stock, min, max, machineId);
                            Inventory.addPart(newInHousePart);
                            addPartSuccess = true;
                        } catch (Exception e) {
                            displayAlert(2);
                        }
                    }

                    if (outSourcedButton.isSelected()) {
                        int partId = selectedPart.getId();
                        companyName = partCompanyOrMachine.getText();
                        OutSourced newOutSourcePart = new OutSourced(partId, name, price, stock, min, max, companyName);
                        Inventory.addPart(newOutSourcePart);
                        addPartSuccess = true;
                    }
                    if (addPartSuccess) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Save");
                        alert.setContentText("Are you sure you want to Save?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if(result.isPresent() && result.get() == ButtonType.OK) {
                            Inventory.deletedPart(selectedPart);
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
    public void ToCancelModify(ActionEvent actionEvent) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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

    /**
     * Changes the label of inhouse to machine ID
     * @param event
     */
    public void InHouseModify(ActionEvent event) {
            inHouseOrOut.setText("Machine ID");
    }

    /**
     * Changes the label of OutScoured to company Name
     * @param event
     */
    public void outSorucedModify(ActionEvent event) {
            inHouseOrOut.setText("Company Name");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
    /**
     * Grabs the String vules from the selceted row and places them into the appropriate text fields
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedPart = MainScreen.getPartToModify();

        if (selectedPart instanceof InHouse) {
            inHouseButton.setSelected(true);
            inHouseOrOut.setText("Machine ID");
            partCompanyOrMachine.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        if (selectedPart instanceof OutSourced){
            outSourcedButton.setSelected(true);
            inHouseOrOut.setText("Company Name");
            partCompanyOrMachine.setText(((OutSourced) selectedPart).getCompanyName());
        }
        partIdText.setText(String.valueOf(selectedPart.getId()));
        partNameText.setText(selectedPart.getName());
        partStockText.setText(String.valueOf(selectedPart.getStock()));
        partPriceText.setText(String.valueOf(selectedPart.getPrice()));
        partMinText.setText(String.valueOf(selectedPart.getMin()));
        partMaxText.setText(String.valueOf(selectedPart.getMax()));

    }
}
