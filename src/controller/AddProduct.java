package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.getThePart;

public class AddProduct implements Initializable {
    public TableView productsTable;
    public TextField proIdText;
    public TextField proNametext;
    public TextField proStockText;
    public TextField proPriceText;
    public TextField ProMaxText;
    public TextField ProMinText;
    public TableView partsTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partStockCol;
    public TableColumn partPriceCol;
    public TableView associatedPartTable;
    public TableColumn associatedPartID;
    public TableColumn associatedPartName;
    public TableColumn associatedPartStock;
    public TableColumn associatedPartCost;
    public TextField searchPart;
    private int productId;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();



    public void updatePartTable(){
        partsTable.setItems(getThePart());
    }
    public void updateAssociatedPartTable(){
        associatedPartTable.setItems(associatedParts);
    }

    /**
     *
     * @param event
     * @throws IOException Adds selected row from the Parts table and places a copy into
     *                      the associated parts table
     */
    public void ToAddAssociatedPro(ActionEvent event) throws IOException {
        Part part = (Part) partsTable.getSelectionModel().getSelectedItem();
        if(part == null){
            displayAlert(5);
        }else{
            associatedParts.addAll(part);
            updatePartTable();
            updateAssociatedPartTable();
        }
    }

    /**
     *
     * @param actionEvent Grabs the selected row from the associated parts table
     *                    and removes it
     */
    public void ToRemoveProduct(ActionEvent actionEvent) {
        Part selectedPart = (Part) associatedPartTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to remove the associated part?");
        alert.showAndWait();

        associatedParts.remove(selectedPart);
        associatedPartTable.setItems(associatedParts);
    }

    /**
     *
     * @param actionEvent
     * @throws IOException      Grabs the values inside the text field then saves
     *                          the String and adds values into the Products table
     *                          Alerts user if they want to save all values
     */
    public void ToSaveProduct(ActionEvent actionEvent) {
        try {

            String name = proNametext.getText();
            double price = Double.parseDouble(proPriceText.getText());
            int stock = Integer.parseInt(proStockText.getText());
            int max = Integer.parseInt(ProMaxText.getText());
            int min = Integer.parseInt(ProMinText.getText());
            boolean addProductSuccess = false;


            if (name.isEmpty()) {
                displayAlert(5);
            } else {
                if (minValid(min, max) && inventoryValid(min, max, stock)) {
                    if(!associatedParts.isEmpty()) {
                        try {
                            int productId = Inventory.getnewProductID();
                            Product newProduct = new Product(productId, name, price, stock, min, max);
                            Inventory.addProduct(newProduct);
                            addProductSuccess = true;


                            for (Part part : associatedParts) {
                                newProduct.addAssociatedPart(part);
                            }
                        } catch (Exception e) {
                            //displayAlert(2);
                        }
                    }else{
                        displayAlert(2);
                        addProductSuccess = false;
                    }

                    if (addProductSuccess) {
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
    public void ToCancelProduct(ActionEvent actionEvent) throws IOException {
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

    @Override
    /**
     * Sets up the values for the associated parts table and Parts table
     * updates the associated parts table and parts table
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getThePart());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartTable.setItems(associatedParts);
        associatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        updateAssociatedPartTable();
        updatePartTable();
    }
    /**
     *
     * @param partName
     * @return Part that contains the text in Search text field
     */
    private ObservableList<Part> searchByPart (String partName){
        ObservableList<Part> namedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getThePart();

        for(Part prt :allParts){
            if(prt.getName().contains(partName)){
                namedPart.add(prt);
            }
        }

        return  namedPart;
    }
    /**
     *
     * @param keyEvent Once enter is pressed, Parts table changes to match the search text field
     */
    public void partSearchKeyPressed(KeyEvent keyEvent) {
        String q = searchPart.getText();

        ObservableList<Part> parts = searchByPart(q);

        if(parts.size() == 0) {

            try {
                int id = Integer.parseInt(q);
                Part prt = getPartID(id);
                if (prt != null) {
                    parts.add(prt);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        partsTable.setItems(parts);
    }
    /**
     *
     * @param id
     * @return Part with the id entered in the search text field
     */
    public static Part getPartID(int id){

        ObservableList<Part> allParts = Inventory.getThePart();
        for(int i =0; i < allParts.size(); i++){
            Part prt = allParts.get(i);

            if(prt.getId() == id){
                return prt;
            }
        }
        return null;
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
    public void displayAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText("Text Field empty or invaild values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText("Must choose one or more associated Parts");
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



}
