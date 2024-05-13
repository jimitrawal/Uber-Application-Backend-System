import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private Map<String, User> users;
  private ArrayList<Driver> drivers;

  private Queue<TMUberService>[] serviceRequests; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  // These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  // Constructor
  public TMUberSystemManager()
  {
    users = new HashMap<String,User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new LinkedList[4]; 
    for(int x = 0; x<serviceRequests.length; x++){
      serviceRequests[x] = new LinkedList<>();
    }
    //TMUberRegistered.loadPreregisteredUsers(users);
    //TMUberRegistered.loadPreregisteredDrivers(drivers);
     
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;
  
  public String getErrorMessage()
  {
    return errMsg;
  }
  
  // Generate a new user account id
  private String generateUserAccountId()
  {
    return "" + userAccountId + users.size();
  }
  
  // Generate a new driver id
  private String generateDriverId()
  {
    return "" + driverId + drivers.size();
  }

  // Given user account id, find user in list of users
  public User getUser(String accountId)
  {
    return users.get(accountId);
  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {
    // Simple way
    return users.containsValue(user);
  }
  
 // Check for duplicate driver
  private boolean driverExists(Driver driver)
 {
   // simple way
   return drivers.contains(driver);
   
 }

 // Get users as Map
  public Map<String,User> getUsers(){
  return users;
 }

 // Set users given the users arraylist
  public void setUsers(ArrayList<User> listOfUsers){
  for (User user: listOfUsers){
    users.put(user.getAccountId(), user);
  }
} 

// Set users given the users arraylist
  public void setDrivers(ArrayList<Driver> listOfDrivers){
  for (Driver driver: listOfDrivers){
    //drivers.set(driver.getId(),driver);
    drivers.add(driver);
    //put(user.getId(), user);
  }
}

 // Given a user, check if user ride/delivery request already exists in service requests
 private boolean existingRequest(TMUberService req)
 {
   // Simple way
   for(int x = 0; x < serviceRequests.length; x++){
    if(serviceRequests[x].contains(req)){// check the index of queue
      return true;
    }
   }
   return false;
   
 } 
 
  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  private Driver getAvailableDriver()
  {
    for (int i = 0; i < drivers.size(); i++)
    {
      Driver driver = drivers.get(i);
      if (driver.getStatus() == Driver.Status.AVAILABLE)
        return driver;
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    ArrayList<User> usersArr = new ArrayList<>(users.values());
    Collections.sort(usersArr, new Comparator<User> () {// Sorts the users based on accountId
      public int compare(User a, User b){
        return Integer.compare(Integer.parseInt(a.getAccountId()), Integer.parseInt(b.getAccountId()));
      } 
    });
    System.out.println();
    
    for (int x = 0; x < usersArr.size(); x++){
      int index = x+1;
      System.out.print(index + " . ");
      usersArr.get(x).printInfo();
      System.out.println(); 
    }
  }
  
  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    System.out.println();
    
    for (int i = 0; i < drivers.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    System.out.println();
    for (int x = 0; x < serviceRequests.length; x++)
    {
      Queue<TMUberService> servicereq = serviceRequests[x];// Make Queue for Individual Zones
      System.out.println("ZONE " + x + "\n======");
      if(servicereq.size() == 0){
        System.out.println("");
      }else {
        int index = 1;
        String dashes = "";
        for(int y = 0; y < 60; y++){
          dashes += "-";
        }
        for (TMUberService eachServiceReq : servicereq) {
          System.out.println(index + ". " + dashes);
          eachServiceReq.printInfo();
          index++;
          System.out.println("\n");
        }
      } 
    }
  }  

  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet)
  {   
    // Check to ensure name is not empty
    if (name == null || name.equals("")){
      throw new InvalidInputException("Invalid Name " + name);
    }
    // Check to ensure address is valid
    if (!CityMap.validAddress(address)){
      throw new BadAddressException("Invalid User Address: " + address);
    }
    // Check to ensure wallet amount is valid
    if (wallet < 0){
      throw new InsufficientFundsException("Invalid Money in Wallet");
    }
    // Check for duplicate user
    User user = new User(generateUserAccountId(), name, address, wallet);
    if (userExists(user)){
      throw new ExistingUserException("User Already Exists in System");
    }
    String userAccountId = user.getAccountId();
    users.put(userAccountId, user);   
  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address, int zone)
  {
    // Check to ensure name is not empty
    if (name == null || name.equals("")){
      throw new InvalidInputException("Invalid Name " + name);
    }
    // Check to ensure car models is valid
    if (carModel == null || carModel.equals("")){
      throw new InvalidCarModelException("Invalid Car Model " + carModel);
    }
    // Check to ensure car licence plate is valid
    // i.e. not null or empty string
    if (carLicencePlate == null || carLicencePlate.equals("")){
      throw new InvalidLicensePlateExecption("Invalid Car Licence Plate " + carLicencePlate);    
    }
    // Check to ensure address is valid
    if (!CityMap.validAddress(address)){
      throw new BadAddressException("Invalid Driver Address");
    }
     
    // Check for duplicate driver. If not a duplicate, add the driver to the drivers list
    Driver driver = new Driver(generateDriverId(), name, carModel, carLicencePlate,address, CityMap.getCityZone(address));
    if (driverExists(driver)){
      throw new ExistingDriverExecption("Driver Already Exists in System");
    }
    drivers.add(driver);
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
    // Check valid user account
    User user = getUser(accountId);
    if(accountId == null || accountId.equals("")){
      throw new InvalidInputException("Invalid Account Id: " + accountId);
    }
    if (user == null){
      throw new UserNotFoundException("User Not Found: " + accountId);
    }
    // Check for a valid from and to addresses
    if (!CityMap.validAddress(from)){
      throw new BadAddressException("Invalid Address: " + from);
    }
    if (!CityMap.validAddress(to)){
      throw new BadAddressException("Invalid Address: " + to);
    }
    // Get the distance for this ride
    int distance = CityMap.getDistance(from, to);         // city blocks
    // Distance == 0 or == 1 is not accepted - walk!
    if (!(distance > 1)){
      throw new InsufficientTravelDistanceException("Insufficient Travel Distance");
    }
    // Check if user has enough money in wallet for this trip
    double cost = getRideCost(distance);
    if (user.getWallet() < cost){
      throw new InsufficientFundsException("Insufficient Funds in Wallet"); 
    }    
    // Create the request
    TMUberRide req = new TMUberRide(from, to, user, distance, cost);
    
    // Check if existing ride request for this user - only one ride request per user at a time
    if (existingRequest(req)){
      throw new ExistingRequestException("User Already Has Request");
    }
    int zone = CityMap.getCityZone(from);
    serviceRequests[zone].add(req);// Add request to the appropiate zone
    user.addRide();
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // Check for valid user account
    User user = getUser(accountId);
    if (user == null){
      throw new InvalidInputException("User Account Not Found " + accountId);
    }
    // Check for valid from and to address
    if (!CityMap.validAddress(from)){
      throw new BadAddressException("Invalid Address: " + from);
    }
    if (!CityMap.validAddress(to)){
      throw new BadAddressException("Invalid Address: " + to);
    }
    // Get the distance to travel
    int distance = CityMap.getDistance(from, to);         // city blocks
    // Distance must be at least 1 city block
    if (distance == 0){
      throw new InsufficientTravelDistanceException("Insufficient Travel Distance");
    }
    // Check if user has enough money in wallet for this delivery
    double cost = getDeliveryCost(distance);
    if (user.getWallet() < cost){
      throw new InsufficientFundsException("Insufficient Funds in Wallet");
    }    
    TMUberDelivery delivery = new TMUberDelivery(from, to, user, distance, cost, restaurant, foodOrderId); 
    // Check if existing delivery request for this user for this restaurant and food order #
    if (existingRequest(delivery)){
      throw new ExistingRequestException("User Already Has Request");
    }
    //driver.setStatus(Driver.Status.DRIVING);
    int zone = CityMap.getCityZone(from);
    serviceRequests[zone].add(delivery);// Add request to the appropiate zone
    user.addDelivery();
  }

  // Cancel an existing service request. 
  // parameter request is the index in the serviceRequests array list
  public void cancelServiceRequest(int zone, int request)
  {
    // Check if zones in bound
    if(zone < 0 || zone > 3){
      throw new ZoneOutOfBoundsException("Invalid Zone Number");
    }
    Queue<TMUberService> services = serviceRequests[zone];// Assign zones to service requests
    // Check if Request Number is Valid
    if (request < 1 || request > services.size()){
      throw new BadRequestNumberException("Invalid Request Number: " + request + " for Zone: " + zone);
    }
    Iterator<TMUberService> iter = services.iterator();// Iterator for services
    int i = 1;
    TMUberService  cancReq = null;
    while(iter.hasNext() && i <= request){// Iterate through services and remove the desired service by iterator
      TMUberService serv = iter.next();
      if(request == i){
        cancReq = serv;
        iter.remove();
        break;
      }
      i++;
    }
    if(cancReq == null){
      throw new InvalidInputException("Cannot Find the Inputs");
    }
  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public void dropOff(String driverId)
  {
    Driver d = null;
    for(Driver driv:drivers){// Iterate through Drivers to find desired driver
      if(driv.getId().equals(driverId)){
         d = driv;
         break;
      }
    }
    // Check valid driverid
    if (driverId == null || driverId.equals("")) {
      throw new InvalidInputException("Invalid Driver Id");
    }
    // Check if driver in the list
    if (d == null){
      throw new DriverNotFoundException("Driver Not Found");
    }
    // Check driver status
    if(d.getStatus() != Driver.Status.DRIVING){
      throw new UnavailableDriverException("No Service for Driver" + driverId);
    }
    String dAddress = d.getAddress();
    int zone = CityMap.getCityZone(dAddress);
    Queue<TMUberService> services = serviceRequests[zone];
    TMUberService service = d.getService();
    services.remove(service);// Remove the request from list
    totalRevenue += service.getCost();// Add service cost to revenues
    d.pay(service.getCost()*PAYRATE);// Pay the driver
    totalRevenue -= service.getCost()*PAYRATE;// Subtract driver fee from total revenues
    d.setStatus(Driver.Status.AVAILABLE);// Change driver status to available
    d.setZone(CityMap.getCityZone(service.getTo())); // Set driver zone to service destination zone
    d.setAddress(service.getTo());// Set driver address to service destination address
    User user = service.getUser();
    user.payForService(service.getCost());// User pays for the service
    user.setAddress(service.getTo());// Set user address to service destination address
  }
  
  // Pick up a ride or a delivery. 
  // parameter request is the driverid
  public void pickup(String driverId){
    Driver driv = null;
    for(Driver driver: drivers){// Iterate through Drivers to find desired driver
      if(driver.getId().equals(driverId)){
        driv = driver;
        break;
      }
    }
    // Check if driver in the list
    if (driv == null) {
      throw new DriverNotFoundException("Driver Not Found");
    }
    // Check valid driverid
    if (driverId == null || driverId.equals("")) {
      throw new InvalidInputException("Invalid Driver Name");
    }
    String drivAdress = driv.getAddress();
    int zone = CityMap.getCityZone(drivAdress);
    // Check if zones in bound
    if(zone < 0 || zone > 3){
      throw new ZoneOutOfBoundsException("Invalid Zone Number");
    } 
    Queue <TMUberService> services = serviceRequests[zone];
    // Check for empty services
    if(services.size() == 0){
      throw new EmptyServiceRequestsException("No Service Requests in Zone " + zone);
    }
    // Check driver status
    if(driv.getStatus() != Driver.Status.AVAILABLE){
      throw new UnavailableDriverException("Seleced Driver Not Available");
    }
    TMUberService serv = services.remove();
    driv.setService(serv);// Set the service for driver
    driv.setStatus(Driver.Status.DRIVING);// Set the status for driver
    driv.setAddress(serv.getFrom());// Set the address for driver
    System.out.println("Driver " + driverId + " Picking Up in Zone "  + CityMap.getCityZone(drivAdress));
  }
  
  // Drive to a given address. This is for a driver with driverid. 
  // parameter request is the driverid and address
  public void driveTo(String driverId, String address){
    Driver d = null;
    for(int x = 0; x < drivers.size(); x++){// Iterate through Drivers to find desired driver
      if(drivers.get(x).getId().equals(driverId)){
        d = drivers.get(x);
      }
    }
    // Check if driver in the list
    if (d == null) {
      throw new DriverNotFoundException("Driver Not Found");
    }
    // Check valid driverid
    if (driverId == null || driverId.equals("")) {
      throw new InvalidInputException("Invalid Driver Name");
    }
    // Check driver status
    if(d.getStatus() != Driver.Status.AVAILABLE){
      throw new UnavailableDriverException("No Drivers Available");
    }
    // Check valid address
    if(!CityMap.validAddress(address)){
      throw new BadAddressException("Invalid Address: " + address);
    }
    d.setAddress(address);// Set driver address to input address
    int zone = CityMap.getCityZone(address);
    d.setZone(zone);// Set driver zone to input address's zone
  }

  // Sort users by name
  public void sortByUserName()
  {
    ArrayList<User> alUsers = new ArrayList<User>(users.values());
    Collections.sort(alUsers, new NameComparator());
    listAllUsers();
  }

  private class NameComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      return a.getName().compareTo(b.getName());
    }
  }

  // Sort users by number amount in wallet
  public void sortByWallet()
  {
    ArrayList<User> alUsers = new ArrayList<User>(users.values());
    Collections.sort(alUsers, new UserWalletComparator());
    listAllUsers();
  }

  private class UserWalletComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      if (a.getWallet() > b.getWallet()) return 1;
      if (a.getWallet() < b.getWallet()) return -1; 
      return 0;
    }
  }
}

// Various Exceptions
class InsufficientTravelDistanceException extends RuntimeException {
  public InsufficientTravelDistanceException(String message) {
    super(message);
  }
}

class BadAddressException extends RuntimeException {
  public BadAddressException(String message) {
    super(message);
  }
}

class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(String message) {
    super(message);
  }
}

class UnavailableDriverException extends RuntimeException {
  public UnavailableDriverException(String message) {
    super(message);
  }
}

class ExistingRequestException extends RuntimeException {
  public ExistingRequestException(String message) {
    super(message);
  }
}

class ExistingUserException extends RuntimeException {
  public ExistingUserException(String message) {
    super(message);
  }
}

class ExistingDriverExecption extends RuntimeException {
  public ExistingDriverExecption(String message) {
    super(message);
  }
}

class InvalidInputException extends RuntimeException {
  public InvalidInputException(String message) {
    super(message);
  }
}

class InvalidCarModelException extends RuntimeException {
  public InvalidCarModelException(String message) {
    super(message);
  }
}

class InvalidLicensePlateExecption extends RuntimeException {
  public InvalidLicensePlateExecption(String message) {
    super(message);
  }
}

class BadRequestNumberException extends RuntimeException {
  public BadRequestNumberException(String message) {
    super(message);
  }
}

class ZoneOutOfBoundsException extends RuntimeException {
  public ZoneOutOfBoundsException(String message) {
    super(message);
  }
}

class EmptyServiceRequestsException extends RuntimeException {
  public EmptyServiceRequestsException(String message) {
    super(message);
  }
}

class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException(String message) {
    super(message);
  }
}

class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
