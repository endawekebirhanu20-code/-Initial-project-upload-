
package I3.Classes;

import java.util.*;

public class HotelSystem {
    private List<AbstractRoom> availableRooms;
    private List<Booking> bookings;
    private List<Payment> payments;

    public HotelSystem() {
        availableRooms = new ArrayList<>();
        bookings = new ArrayList<>();
        payments = new ArrayList<>();
        seedRooms();
    }

    private void seedRooms() {
        Room singleRoom = new Room("S101");
        RoomFare singleFare = new RoomFare();
        singleFare.setRoom_type("Single");
        singleFare.setPricePerDay(1000);
        singleRoom.setRoom_class(singleFare);
        availableRooms.add(singleRoom);

        Room suiteRoom = new Room("SU202");
        RoomFare suiteFare = new RoomFare();
        suiteFare.setRoom_type("Suite");
        suiteFare.setPricePerDay(2500);
        suiteRoom.setRoom_class(suiteFare);
        availableRooms.add(suiteRoom);
    }

    public void displayAvailableRooms() {
        System.out.println("Available Rooms:");
        for (AbstractRoom room : availableRooms) {
            if (!room.isBooked()) {
                System.out.println("Room No: " + room.getRoomNo() + ", Price: " + room.getPrice());
            }
        }
    }

    public Booking createBooking(String customerName, String phone, long checkIn, long checkOut) {
        Booking booking = new Booking();
        UserInfo user = new UserInfo();
        user.setName(customerName);
        user.setPhone_no(phone);
        booking.setCustomer(user);
        booking.setCheckInDateTime(checkIn);
        booking.setCheckOutDateTime(checkOut);
        bookings.add(booking);
        return booking;
    }

    public void bookRoomToBooking(Booking booking, String roomNo) {
        for (AbstractRoom room : availableRooms) {
            if (room.getRoomNo().equals(roomNo) && !room.isBooked()) {
                booking.addRoom(room);
                room.bookRoom();
                System.out.println("Room " + roomNo + " booked successfully.");
                return;
            }
        }
        System.out.println("Room not available.");
    }

    public Payment checkout(Booking booking, float discountPercent, boolean applyDiscount) {
        Payment payment = new Payment(booking);
        payment.setDiscount(discountPercent);
        payment.setHasDiscount(applyDiscount);
        int total = payment.calculateTotalBill();
        payments.add(payment);
        System.out.println("Total Bill: " + total);
        return payment;
    }

    public void cancelBooking(int bookingId) {
        for (Booking b : bookings) {
            if (b.getBooking_id() == bookingId) {
                b.cancelBooking();
                System.out.println("Booking " + bookingId + " cancelled.");
                return;
            }
        }
        System.out.println("Booking not found.");
    }

    public static void main(String[] args) {
        HotelSystem system = new HotelSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Hotel Reservation System");

        while (true) {
            System.out.println("\n1. Show Available Rooms");
            System.out.println("2. Create Booking");
            System.out.println("3. Book Room");
            System.out.println("4. Checkout");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    system.displayAvailableRooms();
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.print("Enter Customer Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Check-in (ms since epoch): ");
                    long in = scanner.nextLong();
                    System.out.print("Enter Check-out (ms since epoch): ");
                    long out = scanner.nextLong();
                    Booking booking = system.createBooking(name, phone, in, out);
                    booking.setBooking_id(system.bookings.size());
                    System.out.println("Booking created with ID: " + booking.getBooking_id());
                    break;
                case 3:
                    System.out.print("Enter Booking ID: ");
                    int bid = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Room No to book: ");
                    String rno = scanner.nextLine();
                    if (bid < system.bookings.size()) {
                        system.bookRoomToBooking(system.bookings.get(bid), rno);
                    } else {
                        System.out.println("Invalid Booking ID");
                    }
                    break;
                case 4:
                    System.out.print("Enter Booking ID: ");
                    int b2 = scanner.nextInt();
                    System.out.print("Apply Discount? (true/false): ");
                    boolean disc = scanner.nextBoolean();
                    System.out.print("Discount %: ");
                    float d = scanner.nextFloat();
                    if (b2 < system.bookings.size()) {
                        system.checkout(system.bookings.get(b2), d, disc);
                    } else {
                        System.out.println("Invalid Booking ID");
                    }
                    break;
                case 5:
                    System.out.print("Enter Booking ID to Cancel: ");
                    int b3 = scanner.nextInt();
                    system.cancelBooking(b3);
                    break;
                case 6:
                    System.out.println("Thank you for using Hotel Reservation System.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
