package model;
import java.time.LocalDateTime;

public class Room {
    private String id;
    private String name;
    private int capacity;
    private String daySchedule;
    private String timeSchedule;
    public Room(String id, String name, int capacity,String daySchedule) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.daySchedule = daySchedule;
    }
    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String n) { name=n; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int c) { capacity=c; }
    public String getDaySchedule() { return daySchedule; }
    public void setDaySchedule(String s){ daySchedule=s; }
    @Override public String toString() { return name + " (" + capacity + ")"; }
}
