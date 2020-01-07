package gui;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnName;

    @FXML
    private Button btNew;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;

    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
        if (result.get() == ButtonType.OK) {
            if (null == service) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }

    }

    private ObservableList<Seller> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Seller();
        createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNode();
    }

    private void initializeNode() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (null == service) {
            throw new IllegalStateException("Service was null");
        }
        List<Seller> list = service.findAll();
        obsList = FXCollections.observableList(list);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
    /*    try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setSeller(obj);
            controller.setSellerService(new SellerService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException ex) {
            Alerts.showAlert("IOException", "Error loading view", ex.getMessage(), Alert.AlertType.ERROR);
        }*/
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
