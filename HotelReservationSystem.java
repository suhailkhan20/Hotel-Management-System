package com.Suhial.hotelmanagement;
import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.sql.DriverManager.getConnection;

public class HotelReservationSystem {
    public static void main(String[] args) {
         String url = "jdbc:mysql://localhost:3306/hotel_management";
         String username = "root";
         String password = "root@123";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = getConnection(url, username, password);

            while (true){
                System.out.println();
                System.out.println("Hotel Management System");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println();
                System.out.print("Choose an option : ");

                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        reservationRoom(connection , scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoom(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again....");
                        System.out.print("If you want to leave So, Press 0. : ");
                }
            }
     } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Reservation Id for Delete: ");
            int  reservationId = scanner.nextInt();
            scanner.nextLine();

            if(!reservationExists(connection,reservationId)){
                System.out.println("Reservation not found for the given Id.");
                return;
            }



            String sql = "DELETE FROM reservations WHERE reservation_id= " + reservationId;

            try (Statement statement = connection.createStatement();){
                int rawaffected = statement.executeUpdate(sql);
                if (rawaffected>0){
                    System.out.println("Reservation Delete successfully !!");
                }else{
                    System.out.println("Reservation Delete failed !!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner) {

        try {
            System.out.print("Enter Reservation Id for Update: ");
            int  reservationId = scanner.nextInt();
            scanner.nextLine();

            if(!reservationExists(connection,reservationId)){
                System.out.println("Reservation not found for the given Id.");
                return;
            }

            System.out.print("Enter new Guest Name: ");
            String Gname = scanner.nextLine();
            System.out.print("Enter new Room number: ");
            int Rno = scanner.nextInt();
            System.out.print("Enter your new Contact number: ");
            String Cno = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" +Gname+ "'," +
                    "room_number = " + Rno + "," +
                    "contact_number = '" + Cno + "'" +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();){


                int rawaffected = statement.executeUpdate(sql);
                if (rawaffected>0){
                    System.out.println("Reservation UPDATE successfully !!");
                }else{
                    System.out.println("Reservation UPDATE failed !!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
       try {
           String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;
           try (Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(sql);){
               return  resultSet.next(); // if there is a result the reservations exists.
           } catch (SQLException e) {
               e.printStackTrace();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
        return false;
    }

    private static void getRoom(Connection connection, Scanner scanner) {

        try {
            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationId ;
//                    " AND guest_name = '" + guestName + "'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection connection)throws SQLException {
        String sql = "SELECT * FROM reservations";

        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);){
            System.out.println("=======================================");
            while (resultSet.next()){
                int Reservation_Id = resultSet.getInt("reservation_id");
                String Guest_Name = resultSet.getString("guest_name");
                int Room_Number = resultSet.getInt("room_number");
                String Contact_Number = resultSet.getString("contact_number");
                String Reservation_Date = resultSet.getTimestamp("reservation_date").toString();

                System.out.println("Reservation Id: "+Reservation_Id);
                System.out.println("Guest Name: "+Guest_Name);
                System.out.println("Room Number: "+Room_Number);
                System.out.println("Contact Number: "+Contact_Number);
                System.out.println("Reservation Date: "+Reservation_Date);
            System.out.println("=======================================");
            }
        }
    }

    private static void reservationRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter Room No.: ");
            int roomNo = scanner.nextInt();
            System.out.print("Enter Your Contact Number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number)" +
                    "VALUES ('" + guestName + "', " + roomNo + ",'" + contactNumber + "')";

            try (Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);
                if (affectedRows > 0){
                    System.out.println("Reservation Successfully !!");
                }
                else {
                    System.out.println("Reservation Failed !!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            }catch (Exception e) {
                e.printStackTrace();
            }

    }
        public static void exit() throws InterruptedException {
            System.out.print("Existing System");
            int i=5;
            while (i!=0){
                System.out.print(".");
                Thread.sleep(450);
                i--;
            }
            System.out.println();
            System.out.println("Thankyou For Using Hotel Management System !!");
        }

}
