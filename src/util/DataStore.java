package util;

import model.User;
import model.Room;
import model.Reservation;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private static final String USER_FILE = "data/users.txt";
    private static final String ROOM_FILE = "data/rooms.txt";
    private static final String RES_FILE = "data/reservations.txt";

    public static List<User> users = new ArrayList<>();
    public static List<Room> rooms = new ArrayList<>();
    public static List<Reservation> reservations = new ArrayList<>();

    public static void loadAll() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        try {
            new File(USER_FILE).createNewFile();
            new File(ROOM_FILE).createNewFile();
            new File(RES_FILE).createNewFile();
        } catch (IOException ignored) {}
        users = loadUsers();
        rooms = loadRooms();
        reservations = loadReservations();
        if (users.isEmpty()) users.add(new User("admin", "admin", true));
        if (rooms.isEmpty()) {
            rooms.add(new Room("R1", "Conference A", 10));
            rooms.add(new Room("R2", "Meeting B", 5));
        }
    }

    private static List<User> loadUsers() {
        List<User> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(USER_FILE))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(",");
                list.add(new User(p[0], p[1], Boolean.parseBoolean(p[2])));
            }
        } catch (IOException ignored) {}
        return list;
    }

    private static List<Room> loadRooms() {
        List<Room> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(ROOM_FILE))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(",");
                list.add(new Room(p[0], p[1], Integer.parseInt(p[2])));
            }
        } catch (IOException ignored) {}
        return list;
    }

    private static List<Reservation> loadReservations() {
        List<Reservation> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(RES_FILE))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(",");
                String resId = p[0];
                String username = p[1];
                String roomId = p[2];
                LocalDateTime start = LocalDateTime.parse(p[3]);
                LocalDateTime end = LocalDateTime.parse(p[4]);
                String status = p[5];
                String comment = p.length > 6 ? p[6] : "";
                // find existing user and room objects
                User u = users.stream()
                        .filter(x -> x.getUsername().equals(username))
                        .findFirst()
                        .orElse(new User(username, "", false));
                Room r = rooms.stream()
                        .filter(x -> x.getId().equals(roomId))
                        .findFirst()
                        .orElse(new Room(roomId, roomId, 0));
                Reservation res = new Reservation(resId, u, r, start, end);
                res.setStatus(status);
                res.setComment(comment);
                list.add(res);
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveAll() {
        saveUsers();
        saveRooms();
        saveReservations();
    }

    private static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                pw.println(String.join(",", u.getUsername(), u.getPassword(), Boolean.toString(u.isAdmin())));
            }
        } catch (IOException ignored) {}
    }

    private static void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOM_FILE))) {
            for (Room r : rooms) {
                pw.println(String.join(",", r.getId(), r.getName(), Integer.toString(r.getCapacity())));
            }
        } catch (IOException ignored) {}
    }

    private static void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RES_FILE))) {
            for (Reservation r : reservations) {
                pw.println(String.join(",",
                        r.getId(), r.getUser().getUsername(), r.getRoom().getId(),
                        r.getStart().toString(), r.getEnd().toString(),
                        r.getStatus(), r.getComment() != null ? r.getComment() : ""
                ));
            }
        } catch (IOException ignored) {}
    }

    public static Optional<User> authenticate(String u, String p) {
        return users.stream()
                .filter(x -> x.getUsername().equals(u) && x.getPassword().equals(p))
                .findFirst();
    }

    public static Optional<User> register(String u, String p) {
        if (users.stream().anyMatch(x -> x.getUsername().equals(u))) return Optional.empty();
        User nu = new User(u, p, false);
        users.add(nu);
        saveUsers();
        return Optional.of(nu);
    }

    public static List<Room> findAvailable(LocalDateTime s, LocalDateTime e) {
        return rooms.stream()
                .filter(r ->
                        reservations.stream().noneMatch(z ->
                                z.getRoom().equals(r) && !(e.isBefore(z.getStart()) || s.isAfter(z.getEnd()))
                        )
                )
                .collect(Collectors.toList());
    }

    public static void addReservation(Reservation r) {
        reservations.add(r);
        saveReservations();
    }
}