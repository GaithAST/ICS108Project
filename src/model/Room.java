package model;

public class Room {
    private String id;
    private String name;
    private int capacity;
    private String daySchedule;
    private String dayScheduleLetters;
    public Room(String id, String name, int capacity,String daySchedule) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.daySchedule = daySchedule;
        this.dayScheduleLetters = getDayScheduleLetters();
    }
    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String n) { name=n; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int c) { capacity=c; }
    public String getDaySchedule() { return daySchedule; }
    public void setDaySchedule(String s){ daySchedule=s; }
    public String getDayScheduleLetters() {
        dayScheduleLetters="";
        for(int i=0; i<daySchedule.length(); i++) {
            Character x = daySchedule.charAt(i);
            if (x.equals('1')) dayScheduleLetters += "UMTWRFS".charAt(i);
        }
        return dayScheduleLetters;
    }
    @Override public String toString() { return name + " (" + capacity + ")"; }
}
