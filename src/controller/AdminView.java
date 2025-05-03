package controller;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Reservation;
import model.Room;
import model.User;
import util.DataStore;

public class AdminView {
    private TabPane root;

    public AdminView() {
        root = new TabPane();

        // Reservations Tab
        TableView<Reservation> rt = new TableView<>(FXCollections.observableArrayList(DataStore.reservations));
        TableColumn<Reservation,String> resIdCol = new TableColumn<>("ID");
        resIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Reservation,String> resUserCol = new TableColumn<>("User");
        resUserCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUser().getUsername()));
        TableColumn<Reservation,String> resRoomCol = new TableColumn<>("Room");
        resRoomCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRoom().getName()));
        TableColumn<Reservation,String> resStatusCol = new TableColumn<>("Status");
        resStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        rt.getColumns().addAll(resIdCol, resUserCol, resRoomCol, resStatusCol);
        Button approve = new Button("Approve"), reject = new Button("Reject");
        approve.setOnAction(e -> {
            Reservation r = rt.getSelectionModel().getSelectedItem();
            if (r != null) {
                r.setStatus("APPROVED");
                DataStore.saveAll();
                rt.refresh();
            }
        });
        reject.setOnAction(e -> {
            Reservation r = rt.getSelectionModel().getSelectedItem();
            if (r != null) {
                r.setStatus("REJECTED");
                DataStore.saveAll();
                rt.refresh();
            }
        });
        HBox resButtons = new HBox(10, approve, reject);
        VBox resBox = new VBox(10, rt, resButtons);
        resBox.setPadding(new Insets(10));
        Tab resTab = new Tab("Reservations", resBox);

        // User Management Tab
        TableView<User> ut = new TableView<>(FXCollections.observableArrayList(DataStore.users));
        TableColumn<User,String> usrNameCol = new TableColumn<>("Username");
        usrNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User,Boolean> usrAdminCol = new TableColumn<>("Admin");
        usrAdminCol.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(c.getValue().isAdmin()));
        ut.getColumns().addAll(usrNameCol, usrAdminCol);
        Button toggleAdmin = new Button("Toggle Admin");
        toggleAdmin.setOnAction(e -> {
            User u = ut.getSelectionModel().getSelectedItem();
            if (u != null) {
                u.setAdmin(!u.isAdmin());
                DataStore.saveAll();
                ut.refresh();
            }
        });
        VBox userBox = new VBox(10, ut, toggleAdmin);
        userBox.setPadding(new Insets(10));
        Tab userTab = new Tab("Users", userBox);
        userTab.setClosable(false);

        // Room Management Tab
        TableView<Room> roomTable = new TableView<>(FXCollections.observableArrayList(DataStore.rooms));
        TableColumn<Room,String> roomIdCol = new TableColumn<>("ID");
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Room,String> roomNameCol = new TableColumn<>("Name");
        roomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Room,Integer> roomCapCol = new TableColumn<>("Capacity");
        roomCapCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        roomTable.getColumns().addAll(roomIdCol, roomNameCol, roomCapCol);

        TextField newId = new TextField(); newId.setPromptText("Room ID");
        TextField newName = new TextField(); newName.setPromptText("Name");
        TextField newCap = new TextField(); newCap.setPromptText("Capacity");
        Button addRoom = new Button("Add Room");
        addRoom.setOnAction(e -> {
            Room r = new Room(newId.getText(), newName.getText(), Integer.parseInt(newCap.getText()),"1111111");
            DataStore.rooms.add(r);
            DataStore.saveAll();
            roomTable.getItems().add(r);
        });
        Button editRoom = new Button("Edit Room");
        editRoom.setOnAction(e -> {
            Room selected = roomTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Open the EditRoomView in a new window
                EditRoomView erv = new EditRoomView();
                BorderPane editPane = erv.EditRoomView(selected,roomTable);
                Stage editStage = new Stage();
                editStage.setTitle("Edit Room: " + selected.getId());
                editStage.setScene(new Scene(editPane, 400, 300));
                // When the edit window closes, refresh rooms from DataStore and update the table
                editStage.setOnHiding(ev -> {
                    // Reload from persistence
                    DataStore.loadAll();
                    roomTable.getItems().setAll(DataStore.rooms);
                });
                editStage.show();
            }
        });
        Button deleteRoom = new Button("Delete Room");
        deleteRoom.setOnAction(e -> {
            Room r = roomTable.getSelectionModel().getSelectedItem();
            if (r != null) {
                DataStore.rooms.remove(r);
                DataStore.saveAll();
                roomTable.getItems().remove(r);
            }
        });
        HBox roomForm = new HBox(10, newId, newName, newCap, addRoom, editRoom, deleteRoom);
        roomForm.setPadding(new Insets(10));

        // Availability Schedule Placeholder
        VBox schedBox = new VBox(10, new Label("Define availability schedules in Edit view."));
        schedBox.setPadding(new Insets(10));

        BorderPane roomPane = new BorderPane();
        roomPane.setTop(roomForm);
        roomPane.setCenter(roomTable);
        roomPane.setBottom(schedBox);
        Tab roomTab = new Tab("Rooms", roomPane);

        root.getTabs().addAll(resTab, userTab, roomTab);
    }

    public TabPane getRoot() {
        return root;
    }
}