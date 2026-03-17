import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void display() {
        System.out.println("\n=== Current Inventory ===");
        for (String type : availability.keySet()) {
            System.out.println(type + ": " + availability.get(type));
        }
    }
}

class BookingService {
    private InventoryService inventory;

    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> allRoomIds = new HashSet<>();

    private int idCounter = 1;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processRequests(BookingRequestQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();

            String type = r.getRoomType();
            int available = inventory.getAvailability(type);

            if (available > 0) {
                String roomId = generateRoomId(type);

                allRoomIds.add(roomId);

                allocatedRooms
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrement(type);

                System.out.println("Booking Confirmed:");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Room ID: " + roomId + "\n");
            } else {
                System.out.println("Booking Failed for " + r.getGuestName() +
                        " (No " + type + " rooms available)\n");
            }
        }
    }

    private String generateRoomId(String type) {
        String id;
        do {
            id = type.substring(0, 1).toUpperCase() + idCounter++;
        } while (allRoomIds.contains(id));
        return id;
    }

    public void displayAllocations() {
        System.out.println("\n=== Allocated Rooms ===");
        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}

public class RoomAllocationService {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Arun", "Single"));
        queue.addRequest(new Reservation("Priya", "Double"));
        queue.addRequest(new Reservation("Rahul", "Single"));
        queue.addRequest(new Reservation("Sneha", "Suite"));
        queue.addRequest(new Reservation("Kiran", "Single"));

        BookingService bookingService = new BookingService(inventory);

        bookingService.processRequests(queue);

        bookingService.displayAllocations();
        inventory.display();
    }
}