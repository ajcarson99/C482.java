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


public class ModifyProduct implements Initializable {


    public TableView partsTable;
    public TableColumn PartIdCol;
    public TableColumn PartNameCol;
    public TableColumn partStockCol;
    public TableColumn partPriceCol;

    public TextField proIdMod;
    public TextField proNameModify;
    public TextField proStockTextMod;
    public TextField proPriceMod;
    public TextField proMaxMod;
    public TextField proMinMod;
    public TextField searchPart;

    public TableView associatedPartTable;
    public TableColumn assocPartIdCol;
    public TableColumn assocPartNameCol;
    public TableColumn assocPartStockCol;
    public TableColumn assocPartPriceCol;



    private  Product selectedProduct;
     private int proID;
    int selectedIndex;
    private ObservableList<Part> associatedPart = FXCollections.observableArrayList();

    @Override
    /**
     * Grabs the selected row from the Products table puts the values in the appropriate
     * text fields.
     * creates the Parts table and rows
     * Creates the associated parts table and updates both tables
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedProduct = MainScreen.getProductToModify();



        partsTable.setItems(getThePart());
        PartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));


        assocPartIdCol.setCellValueFactory((new PropertyValueFactory<>("id")));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));


        associatedPart = selectedProduct.getAssociatedParts();
        associatedPartTable.setItems(associatedPart);
        proIdMod.setText(String.valueOf(selectedProduct.getId()));
        proNameModify.setText(String.valueOf(selectedProduct.getName()));
        proStockTextMod.setText(String.valueOf(selectedProduct.getStock()));
        proMaxMod.setText(String.valueOf(selectedProduct.getMax()));
        proMinMod.setText(String.valueOf(selectedProduct.getMin()));
        proPriceMod.setText(String.valueOf(selectedProduct.getPrice()));



        updatePartTable();
        updateAssociatedPartTable();
    }


    public void updatePartTable(){
        partsTable.setItems(getThePart());
    }
    public void updateAssociatedPartTable(){
        associatedPartTable.setItems(associatedPart);
    }
    /**
     *
     * @param event
     * @throws IOException Adds selected row from the Parts table and places a copy into
     *                      the associated parts table
     */
    public void ToAdd(ActionEvent event) {
        Part part = (Part) partsTable.getSelectionModel().getSelectedItem();
        if(part == null){
            displayAlert(5);
        }else{
            associatedPart.addAll(part);
            updateAssociatedPartTable();
            updatePartTable();
        }
    }

    /**
     *
     * @param event Grabs the selected row from the associated parts table
     *                    and removes it
     */
    public void removeAssociatedPart(ActionEvent event) {
        Part selectedPart = (Part) associatedPartTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to remove the associated part?");
        alert.showAndWait();

        associatedPart.remove(selectedPart);
        associatedPartTable.setItems(associatedPart);
    }
    /**
     *
     * @param event Grabs the selected row and puts the values into the appropriate
     *                    text fields. Saves any changes and deletes the last selected row and replaces
     *                    it with the modified version.
     *                    Alerts user if they want to save all values
     */
    public void ToSaveModify(ActionEvent event) throws IOException {
    try {
        int id = Integer.parseInt(proIdMod.getText());
        String name = proNameModify.getText();
        double price = Double.parseDouble(proPriceMod.getText());
        int stock = Integer.parseInt(proStockTextMod.getText());
        int max = Integer.parseInt(proMaxMod.getText());
        int min = Integer.parseInt(proMinMod.getText());
        boolean productSuccessful = false;


            if (min > max) {
                displayAlert(3);
            }
            if (stock > max || stock < min) {
                displayAlert(4);
            }
            Product modProduct = new Product(id, name, price, stock, min, max);
            Inventory.addProduct(modProduct);
            Inventory.deletedProduct(selectedProduct);
            productSuccessful = true;
            for (Part part : associatedPart) {
                modProduct.addAssociatedPart(part);
            }

        if(productSuccessful){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure you want to Save?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 950, 450);
                stage.setTitle("Back to main Screen");
                stage.setScene(scene);
                stage.show();
            }
        }
        }catch (Exception e){
        displayAlert(1);
    }


    }
    /**
     *
     * @param event
     * @throws IOException Alerts the user if they would like to cancel the modified part.
     * If the Ok button is pressed it will not save the current text fields adn return to the
     * main screen
     */
    public void ToCancelModify(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene (root, 950,450);
        stage.setTitle("Back to main Screen");
        stage.setScene(scene);
        stage.show();
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
    public void displayAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error modifying Product");
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



        }
    }


}
