package model;

public class Room {
    private String id;
    private String name;
    private int capacity;

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
    public String getId() { return id; }

    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    @Override public String toString() { return name + " (" + capacity + ")"; }
}
