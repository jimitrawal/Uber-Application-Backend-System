# Uber Application Backend System

TMUber is a simple java backend (command-line based) simulation of an Uber-like application. It allows users to register as drivers or users, request rides, request food deliveries, and perform various other actions typically found in ride-sharing and delivery services.

## Features

- **User Management**: Register new users and drivers.
- **Request Services**: Request rides and food deliveries.
- **Cancel Requests**: Cancel current service requests.
- **Driver Actions**: Drop off users or food deliveries, drive to destinations, and pick up rides or deliveries.
- **Sorting**: Sort users by name or wallet balance.
- **Unit Testing**: Test for valid city addresses and calculate distances.
- **Data Loading**: Load users and drivers from external files.

## Getting Started

To run TMUber, you'll need Java installed on your system. Follow these steps:

1. Clone this repository to your local machine.
2. Compile the Java source files using `javac TMUberUI.java`.
3. Run the application with `java TMUberUI`.

## Usage

TMUber supports various commands that can be entered through the command-line interface. Here are some examples:

- `DRIVERS`: List all registered drivers.
- `USERS`: List all registered users.
- `REQUESTS`: List all current ride or delivery requests.
- `REGDRIVER`: Register a new driver.
- `REGUSER`: Register a new user.
- `REQRIDE`: Request a ride.
- `REQDLVY`: Request a food delivery.
- `SORTBYNAME`: Sort users by name.
- `SORTBYWALLET`: Sort users by wallet balance.
- `CANCELREQ`: Cancel a current service request.
- `DROPOFF`: Drop off a user or a food delivery.
- `DRIVETO`: Driver drives to a certain destination.
- `REVENUES`: Get the total revenue.
- `ADDR`: Unit test for a valid city address.
- `DIST`: Unit test for the CityMap distance method.
- `PICKUP`: Pick up a ride or a delivery.

## Contributing

Contributions are welcome! If you find any bugs or have suggestions for improvements, please open an issue or submit a pull request.
