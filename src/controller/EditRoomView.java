package controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Room;
import util.DataStore;

import java.util.Arrays;

public class EditRoomView {
    private BorderPane root = null;
    public BorderPane EditRoomView(Room room,TableView<Room> roomTable){
        root = new BorderPane();
        String[] days = room.getDaySchedule().split("");
        System.out.println(Arrays.toString(days));
        GridPane grid = new GridPane();
        CheckBox sunday = new CheckBox("Sunday");
        sunday.setSelected(days[0].equals("1"));
        CheckBox monday = new CheckBox("Monday");
        monday.setSelected(days[1].equals("1"));
        CheckBox tuesday = new CheckBox("Tuesday");
        tuesday.setSelected(days[2].equals("1"));
        CheckBox wednesday = new CheckBox("Wednesday");
        wednesday.setSelected(days[3].equals("1"));
        CheckBox thursday = new CheckBox("Thursday");
        thursday.setSelected(days[4].equals("1"));
        CheckBox friday = new CheckBox("Friday");
        friday.setSelected(days[5].equals("1"));
        CheckBox saturday = new CheckBox("Saturday");
        saturday.setSelected(days[6].equals("1"));
        VBox checkboxes = new VBox(sunday,monday,tuesday,wednesday,thursday, friday,saturday);
        checkboxes.setPadding(new Insets(10,10,10,10));
        checkboxes.setSpacing(10);
        TextField roomNameTextField = new TextField();
        roomNameTextField.setText(room.getName());
        TextField roomCapacityTextField = new TextField();
        roomCapacityTextField.setText(Integer.toString(room.getCapacity()));
        HBox roomName = new HBox(new Label("Room Name"), roomNameTextField);
        HBox roomCapacity= new HBox(new Label("Room Capacity"), roomCapacityTextField);
        VBox roomValues = new VBox(roomName, roomCapacity);
        roomName.setSpacing(10);
        grid.setPadding(new Insets(10,10,10,10));
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            room.setCapacity(Integer.parseInt(roomCapacityTextField.getText()));
            room.setName(roomNameTextField.getText());
            System.out.println(room.getName());
            String daySchedule="";
            for (int i = 0; i < checkboxes.getChildren().size(); i++) {
                CheckBox checkbox = (CheckBox) checkboxes.getChildren().get(i);
                String value = checkbox.isSelected() ? "1" : "0";
                daySchedule = daySchedule + value;
            }
            System.out.println(room.getCapacity());
            room.setDaySchedule(daySchedule);
            DataStore.saveAll();
            roomTable.refresh();
        });
        grid.add(checkboxes,0,0);
        grid.add(roomValues,1,0);
        root.setCenter(grid);
        root.setBottom(saveButton);
        return root;
    }
}
