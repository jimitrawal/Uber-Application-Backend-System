import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class TMUberRegistered
{
    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(ArrayList<User> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    // The test scripts and test outputs included with the skeleton code use these
    // users and drivers below. You may want to work with these to test your code (i.e. check your output with the
    // sample output provided). 
    public static ArrayList<User> loadPreregisteredUsers(String filename) throws FileNotFoundException
    {
        ArrayList<User> users = new ArrayList<>();
        try{
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String name = sc.nextLine();
                String address = sc.nextLine();
                double wallet = Double.parseDouble(sc.nextLine());
                User userN = new User(generateUserAccountId(users), name, address, wallet);
                users.add(userN);
            }
            
        }catch (FileNotFoundException e){
            throw new FileNotFoundException("User File: " + filename + " Not Found");
        }
        return users;
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    public static ArrayList<Driver> loadPreregisteredDrivers(String filename) throws FileNotFoundException
    {
        ArrayList<Driver> drivers = new ArrayList<>();
        try{
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String name = sc.nextLine();
                String carModel = sc.nextLine();
                String licensePlate = sc.nextLine();
                String address = sc.nextLine();
                int zone = CityMap.getCityZone(address);
                Driver driverN = new Driver(generateDriverId(drivers), name, carModel, licensePlate, address, zone);
                drivers.add(driverN);
            }
        }catch(FileNotFoundException e){
            throw new FileNotFoundException("Driver File: " + filename + " Not Found");
        }
        return drivers;
    }
}

