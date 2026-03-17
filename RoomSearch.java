import java.util.HashMap;
import java.util.Map;

abstract class Room {
    private int beds;
    private double size;
    private double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 150, 2000);
    }

    public String getRoomType() {
        return "Single";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 250, 3500);
    }

    public String getRoomType() {
        return "Double";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 500, 7000);
    }

    public String getRoomType() {
        return "Suite";
    }
}

class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return availability;
    }
}

class SearchService {
    public void searchAvailableRooms(Inventory inventory, Map<String, Room> rooms) {
        System.out.println("=== Available Rooms ===\n");

        for (String type : rooms.keySet()) {
            int count = inventory.getAvailability(type);

            if (count > 0) {
                Room room = rooms.get(type);
                room.displayDetails();
                System.out.println("Available: " + count + "\n");
            }
        }
    }
}

public class RoomSearch {
    public static void main(String[] args) {
        Map<String, Room> rooms = new HashMap<>();
        rooms.put("Single", new SingleRoom());
        rooms.put("Double", new DoubleRoom());
        rooms.put("Suite", new SuiteRoom());

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, rooms);
    }
}