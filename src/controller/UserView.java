package controller;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Reservation;
import model.Room;
import model.User;
import util.DataStore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserView {
    private TabPane root;

    public UserView(User user) {
        root = new TabPane();

        // Tab 1: New Reservation
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField startField = new TextField();
        startField.setPromptText("Start HH:mm");
        TextField endField = new TextField();
        endField.setPromptText("End HH:mm");
        ComboBox<Room> roomCombo = new ComboBox<>(
                FXCollections.observableArrayList(DataStore.rooms)
        );
        roomCombo.setPromptText("Select Room");
        Button reqBtn = new Button("Request Reservation");
        Label status = new Label();

        reqBtn.setOnAction(e -> {
            LocalDate date = datePicker.getValue();
            LocalTime st = LocalTime.parse(startField.getText());
            LocalTime et = LocalTime.parse(endField.getText());
            LocalDateTime start = LocalDateTime.of(date, st);
            LocalDateTime end = LocalDateTime.of(date, et);
            Reservation r = new Reservation(UUID.randomUUID().toString(), user,
                    roomCombo.getValue(), start, end);
            DataStore.addReservation(r);
            status.setText("Submitted: " + r.getId());
            refreshTable(user);
        });

        form.getChildren().addAll(
                new Label("Date:"), datePicker,
                new Label("Start:"), startField,
                new Label("End:"), endField,
                roomCombo, reqBtn, status
        );
        Tab newTab = new Tab("New Request", form);

        // Tab 2: My Requests
        TableView<Reservation> table = new TableView<>();
        TableColumn<Reservation, String> idCol = new TableColumn<>("ID");
        TableColumn<Reservation, String> roomCol = new TableColumn<>("Room");
        TableColumn<Reservation, String> startCol = new TableColumn<>("Start");
        TableColumn<Reservation, String> endCol = new TableColumn<>("End");
        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        TableColumn<Reservation, String> commentCol = new TableColumn<>("Comment");

        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        roomCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRoom().getName()));
        startCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStart().toString()));
        endCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEnd().toString()));
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
        commentCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getComment()));

        table.getColumns().addAll(idCol, roomCol, startCol, endCol, statusCol, commentCol);
        refreshTable(user, table);

        VBox tableBox = new VBox(table);
        tableBox.setPadding(new Insets(10));
        Tab viewTab = new Tab("My Requests", tableBox);

        root.getTabs().addAll(newTab, viewTab);
    }

    private void refreshTable(User user, TableView<Reservation> table) {
        table.getItems().setAll(
                DataStore.reservations.stream()
                        .filter(r -> r.getUser().getUsername().equals(user.getUsername()))
                        .collect(Collectors.toList())
        );
    }

    // Overloaded for submission event
    private void refreshTable(User user) {
        for (Tab tab : root.getTabs()) {
            if (tab.getText().equals("My Requests")) {
                @SuppressWarnings("unchecked")
                TableView<Reservation> table = (TableView<Reservation>) ((VBox) tab.getContent()).getChildren().get(0);
                refreshTable(user, table);
            }
        }
    }

    public TabPane getRoot() {
        return root;
    }
}
