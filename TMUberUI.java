import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(); 
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
          tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        try {
          String name = "";
          System.out.print("Name: ");
          if (scanner.hasNextLine())
          {
            name = scanner.nextLine();
          }
          String carModel = "";
          System.out.print("Car Model: ");
          if (scanner.hasNextLine())
          {
            carModel = scanner.nextLine();
          }
          String license = "";
          System.out.print("Car License: ");
          if (scanner.hasNextLine())
          {
            license = scanner.nextLine();
          }
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine())
          {
            address = scanner.nextLine();
          }
          tmuber.registerNewDriver(name, carModel, license, address, CityMap.getCityZone(address));
          System.out.printf("Driver: %s Car Model: %s License Plate: %s, Address: %s", name, carModel, license, address);
        }catch(BadAddressException e){
          System.out.println(e.getMessage()); 
        }catch(InvalidCarModelException e){
          System.out.println(e.getMessage()); 
        }catch(InvalidLicensePlateExecption e){
          System.out.println(e.getMessage()); 
        }catch(ExistingDriverExecption e){
          System.out.println(e.getMessage()); 
        }catch(InvalidInputException e){
          System.out.println(e.getMessage()); 
        }
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        try{
          String name = "";
          System.out.print("Name: ");
          if (scanner.hasNextLine())
          {
            name = scanner.nextLine();
          }
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine())
          {
            address = scanner.nextLine();
          }
          
          double wallet = 0.0;
          System.out.print("Wallet: ");
          if (scanner.hasNextDouble())
          {
            try {
              wallet = scanner.nextDouble();
              scanner.nextLine(); // consume nl
            }catch (NumberFormatException e) {
              System.out.println("Invalid Type Of Wallet Amount");
              continue; 
            }
          }
          tmuber.registerNewUser(name, address, wallet);
          System.out.printf("User: %s Address: %s Wallet: %2.2f", name, address, wallet);
        }catch(BadAddressException e){
          System.out.println(e.getMessage());
        }catch(ExistingUserException e){
          System.out.println(e.getMessage());
        }catch(InsufficientFundsException e){
          System.out.println(e.getMessage());
        }catch(InvalidInputException e){
          System.out.println(e.getMessage());
        }   
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        try{
          String account = "";
          System.out.print("User Account Id: ");
          if (scanner.hasNextLine())
          {
            account = scanner.nextLine();
          }
          String from = "";
          System.out.print("From Address: ");
          if (scanner.hasNextLine())
          {
            from = scanner.nextLine();
          }
          String to = "";
          System.out.print("To Address: ");
          if (scanner.hasNextLine())
          {
            to = scanner.nextLine();
          }
          tmuber.requestRide(account, from, to);
          User user = tmuber.getUser(account);
          System.out.printf("\nRIDE for: %-15s From: %-15s To: %-15s", user.getName(), from, to);
        }catch(InvalidInputException e){
          System.out.println(e.getMessage());
        }catch(UserNotFoundException e){
          System.out.println(e.getMessage());
        }catch(InsufficientTravelDistanceException e){
          System.out.println(e.getMessage());
        }catch(InsufficientFundsException e){
          System.out.println(e.getMessage());
        }catch(BadAddressException e){
          System.out.println(e.getMessage());
        }catch(ExistingRequestException e){
          System.out.println(e.getMessage());
        }
        
      }
      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        try{
          String account = "";
          System.out.print("User Account Id: ");
          if (scanner.hasNextLine())
          {
            account = scanner.nextLine();
          }
          String from = "";
          System.out.print("From Address: ");
          if (scanner.hasNextLine())
          {
            from = scanner.nextLine();
          }
          String to = "";
          System.out.print("To Address: ");
          if (scanner.hasNextLine())
          {
            to = scanner.nextLine();
          }
          String restaurant = "";
          System.out.print("Restaurant: ");
          if (scanner.hasNextLine())
          {
            restaurant = scanner.nextLine();
          }
          String foodOrder = "";
          System.out.print("Food Order #: ");
          if (scanner.hasNextLine())
          {
            foodOrder = scanner.nextLine();
          }
          tmuber.requestDelivery(account, from, to, restaurant, foodOrder);
          User user = tmuber.getUser(account);
          System.out.printf("\nDELIVERY for: %-15s From: %-15s To: %-15s", user.getName(), from, to);
        }catch(InvalidInputException e){
          System.out.println(e.getMessage()); 
        }catch(BadAddressException e){
          System.out.println(e.getMessage()); 
        }catch(InsufficientTravelDistanceException e){
          System.out.println(e.getMessage()); 
        }catch(InsufficientFundsException e){
          System.out.println(e.getMessage()); 
        }catch(ExistingRequestException e){
          System.out.println(e.getMessage()); 
        }
      }
      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      /*else if (action.equalsIgnoreCase("SORTBYDIST")) 
      {
        tmuber.sortByDistance();
      }*/
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        int request;
        int zone;
        try {
          System.out.print("Zone: ");
          zone = scanner.nextInt();
          System.out.print("Request #: ");
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl character
          tmuber.cancelServiceRequest(zone, request);
          System.out.println("Service request #" + request + " cancelled in Zone " + zone);
        }catch(InputMismatchException e){
          System.out.println("Type Mismatch for Input");
          scanner.nextLine();
        }catch (ZoneOutOfBoundsException e){
          System.out.println(e.getMessage());
        }catch (BadRequestNumberException e){
          System.out.println(e.getMessage());
        } catch (InvalidInputException e){
          System.out.println(e.getMessage());
        }       
      }
      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        try{
          String driverId = "";
          System.out.print("Driver Id: ");
          if (scanner.hasNextLine())
          {
            driverId = scanner.nextLine();
          }
          tmuber.dropOff(driverId);
          System.out.println("Driver " + driverId + " Dropping Off"); 
        }catch (InvalidInputException e){
          System.out.println(e.getMessage());
        }catch (DriverNotFoundException e){
          System.out.println(e.getMessage());
        }catch (UnavailableDriverException e){
          System.out.println(e.getMessage());
        }catch (EmptyServiceRequestsException e){
          System.out.println(e.getMessage());
        }
      }
      // Driver drives to a certain destination
      else if (action.equalsIgnoreCase("DRIVETO")) 
      {
        try{
          String driverId = "";
          System.out.print("Driver Id: ");
          if (scanner.hasNextLine())
          {
            driverId = scanner.nextLine();
          }
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine())
          {
            address = scanner.nextLine();
          }
          tmuber.driveTo(driverId,address);
          System.out.println("Driver " + driverId + " Now in Zone " + CityMap.getCityZone(address));
        }catch(DriverNotFoundException e){
          System.out.println(e.getMessage());
        }catch(InvalidInputException e){
          System.out.println(e.getMessage());
        }catch(UnavailableDriverException e){
          System.out.println(e.getMessage());
        }catch(BadAddressException e){
          System.out.println(e.getMessage());
        }
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      // Pick up a ride or a delivery. 
      else if (action.equalsIgnoreCase("PICKUP")) 
      {
        try{
          String driverId = "";
          System.out.print("Driver Id: ");
          if (scanner.hasNextLine())
          {
            driverId = scanner.nextLine();
          }
          tmuber.pickup(driverId);
        }catch(DriverNotFoundException e){
          System.out.println(e.getMessage());
        }catch(InvalidInputException e){
          System.out.println(e.getMessage());
        }catch(ZoneOutOfBoundsException e){
          System.out.println(e.getMessage());
        }catch(EmptyServiceRequestsException e){
          System.out.println(e.getMessage());
        }catch(UnavailableDriverException e){
          System.out.println(e.getMessage());
        }
      }
      // Load users from a file
      else if (action.equalsIgnoreCase("LOADUSERS"))
      {
        System.out.print("User File: ");
        Scanner sc = new Scanner(System.in);
        String filename = sc.nextLine();
        try {
          tmuber.setUsers(TMUberRegistered.loadPreregisteredUsers(filename));
          System.out.println("Users Loaded");
        }catch(FileNotFoundException e){
          System.out.println("Users File: " + filename + " Not Found");
        }
      }
      // Load drivers from a file
      else if (action.equalsIgnoreCase("LOADDRIVERS"))
      {
        System.out.print("Driver File: ");
        Scanner sc = new Scanner(System.in);
        String filename = sc.nextLine();
        try {
          tmuber.setDrivers(TMUberRegistered.loadPreregisteredDrivers(filename));
          System.out.println("Drivers Loaded");
        }catch(FileNotFoundException e){
          System.out.println("File: " + filename + " Not Found");
        }
      }
      System.out.print("\n>");
    }
  }
}

