package carRentalSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class CarRentalService {
	private List<Car> cars;
	private List<Customer> customers;
	private List<BookedCarInformation> bookedCarInformations;
	private List<Review> reviews;
	private static final String FILE_NAME = "rental_data.txt";

	public CarRentalService() {
		cars = new ArrayList<>();
		customers = new ArrayList<>();
		bookedCarInformations = new ArrayList<>();
		reviews = new ArrayList<>();
		loadDataFromFile();
	}

	
	public void saveDataToFile() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			
			// Loại bỏ trùng lặp trước khi lưu bằng cách dùng LinkedHashSet
			cars = new ArrayList<>(new LinkedHashSet<>(cars));
			customers = new ArrayList<>(new LinkedHashSet<>(customers));
			bookedCarInformations = new ArrayList<>(new LinkedHashSet<>(bookedCarInformations));
			reviews = new ArrayList<>(new LinkedHashSet<>(reviews));

			oos.writeObject(cars);
			oos.writeObject(customers);
			oos.writeObject(bookedCarInformations);
			oos.writeObject(reviews);

			System.out.print("");
		} catch (IOException e) {
			System.out.println("Error saving data: " + e.getMessage());
		}
	}

	
	@SuppressWarnings("unchecked")
	public void loadDataFromFile() {
		File file = new File(FILE_NAME);
		if (!file.exists())
			return;
		// Ghi các đối tượng vào một luồng đầu ra bằng ObjectInputStream
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			List<Car> loadedCars = (List<Car>) ois.readObject();
			List<Customer> loadedCustomers = (List<Customer>) ois.readObject();
			List<BookedCarInformation> loadedBookings = (List<BookedCarInformation>) ois.readObject();
			List<Review> loadedReviews = (List<Review>) ois.readObject();

			// Xóa danh sách cũ trước khi load dữ liệu mới
			cars.clear();
			customers.clear();
			bookedCarInformations.clear();
			reviews.clear();

			// Ngăn chặn trùng lặp
			cars.addAll(new LinkedHashSet<>(loadedCars));
			customers.addAll(new LinkedHashSet<>(loadedCustomers));
			bookedCarInformations.addAll(new LinkedHashSet<>(loadedBookings));
			reviews.addAll(new LinkedHashSet<>(loadedReviews));

			System.out.println("Data loaded successfully.");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error loading data: " + e.getMessage());
		}
	}

	public void bookCar(Car car, Customer customer, int days) {
		boolean alreadyBooked = bookedCarInformations.stream().anyMatch(
				b -> b.getCar().getCarId().equals(car.getCarId()) && b.getCustomer().getId().equals(customer.getId()));

		if (alreadyBooked) {
			System.out.println("You have already booked this car.");
			return;
		}

		// Cập nhật đúng số lượng xe trong danh sách chính `cars`
		for (Car c : cars) {
			if (c.getCarId().equals(car.getCarId())) {
				if (c.getNoOfAvailableCar() > 0) {
					c.setNoOfAvailableCar(c.getNoOfAvailableCar() - 1);
					bookedCarInformations.add(new BookedCarInformation(c, customer, days));
					saveDataToFile();
					LocalDateTime bookingTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
					System.out.println("Car booked successfully at: " + bookingTime.format(formatter));
				} else {
					System.out.println("Car is not available for rent.");
				}
				return;
			}
		}
	}

	public void returnCar(Car car, BookedCarInformation bookedCarInformation) {
		for (Car c : cars) {
			if (c.getCarId().equals(car.getCarId())) {
				c.setNoOfAvailableCar(c.getNoOfAvailableCar() + 1);
				bookedCarInformations.remove(bookedCarInformation);
				saveDataToFile();
				LocalDateTime returnTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
				System.out.println("Car returned successfully at: " + returnTime.format(formatter));
				return;
			}
		}
	}

	public void addCustomer(Customer cust) {
		boolean exists = customers.stream().anyMatch(c -> c.getId().equals(cust.getId()));
		if (!exists) {
			customers.add(cust);
			saveDataToFile();
		} else {
			System.out.println("Customer already exists: " + cust.getId());
		}
	}

	public void addCar(Car car) {
		if (cars.stream().noneMatch(c -> c.getCarId().equals(car.getCarId()))) {
			cars.add(car);
			saveDataToFile();
		} else {
			System.out.print("");
		}
	}

	public void displayMenu() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("\n===== Welcome to our Car Rental System =====");
			System.out.println("1. Book a Car");
			System.out.println("2. Return a Car");
			System.out.println("3. View Available Cars");
			System.out.println("4. Add a New Car");
			System.out.println("5. View Reviews");
			System.out.println("6. View Rented Cars");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				bookCarOption(sc);
				break;
			case 2:
				returnCarOption(sc);
				break;
			case 3:
				displayAvailableCars();
				break;
			case 4:
				addNewCarFromUserInput(sc);
				break;
			case 5:
				displayReviews();
				break;
			case 6:
				displayRentedCars();
				break;
			case 7:
				System.out.println("Thank you for choosing our service!");
				sc.close();
				return;
			default:
				System.out.println("Invalid choice! Please try again.");
			}
		}
	}

	private void bookCarOption(Scanner sc) {
		System.out.println("== Book a Car ==");
		System.out.print("Enter your name: ");
		String custName = sc.nextLine();

		System.out.print("Enter the Car ID you want to rent: ");
		String carId = sc.nextLine();

		System.out.print("Enter the number of rental days: ");
		int days = sc.nextInt();

		Customer customer = new Customer("CUSTOMER-" + (customers.size() + 1), custName);
		addCustomer(customer);

		Optional<Car> optionalCar = cars.stream()
				.filter(c -> c.getCarId().equalsIgnoreCase(carId) && c.getNoOfAvailableCar() > 0).findAny();

		if (optionalCar.isEmpty()) {
			System.out.println("Car is not available. Please try another car.");
			return;
		}

		Car selectedCar = optionalCar.get();
		System.out.println("=== Rental Confirmation ===");
		System.out.println("Customer: " + customer.getName());
		System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
		System.out.println("Rental Days: " + days);
		System.out.println("Total Price: " + selectedCar.calculatePrice(days));

		System.out.print("Confirm rental (Y/N): ");
		if (sc.next().equalsIgnoreCase("Y")) {
			bookCar(selectedCar, customer, days);
		} else {
			System.out.println("Booking canceled.");
		}
	}

	public void addNewCarFromUserInput(Scanner sc) {
		System.out.println("== Add a New Car ==");
		System.out.print("Enter Car ID: ");
		String carId = sc.nextLine();
		System.out.print("Enter Brand: ");
		String brand = sc.nextLine();
		System.out.print("Enter Model: ");
		String model = sc.nextLine();
		System.out.print("Enter Number of Available Cars: ");
		int noOfAvailableCar = sc.nextInt();
		System.out.print("Enter Price Per Day: ");
		double pricePerDay = sc.nextDouble();
		sc.nextLine(); 

		System.out.print("Is this an Electric Car? (Y/N): ");
		if (sc.next().equalsIgnoreCase("Y")) {
			sc.nextLine();
			System.out.print("Enter Pin Capacity: ");
			int pinCapacity = sc.nextInt();
			sc.nextLine(); 

			ElectricCars electricCar = new ElectricCars();
			electricCar.setCarId(carId);
			electricCar.setBrand(brand);
			electricCar.setModel(model);
			electricCar.setNoOfAvailableCar(noOfAvailableCar);
			electricCar.setPricePerDay(pricePerDay);
			electricCar.setPinCapacity(pinCapacity);

			addCar(electricCar);
		} else {
			System.out.print("Is this an SUV? (Y/N): ");
			if (sc.next().equalsIgnoreCase("Y")) {
				sc.nextLine();
				System.out.print("Enter Size: ");
				int size = sc.nextInt();
				sc.nextLine();

				SUV suvCar = new SUV();
				suvCar.setCarId(carId);
				suvCar.setBrand(brand);
				suvCar.setModel(model);
				suvCar.setNoOfAvailableCar(noOfAvailableCar);
				suvCar.setPricePerDay(pricePerDay);
				suvCar.setSize(size);

				addCar(suvCar);
			} else {
				Car car = new Car();
				car.setCarId(carId);
				car.setBrand(brand);
				car.setModel(model);
				car.setNoOfAvailableCar(noOfAvailableCar);
				car.setPricePerDay(pricePerDay);

				addCar(car);
			}
		}

		saveDataToFile();
		System.out.println("New car added successfully!");
	}

	public void addReview(Scanner sc, Car car, Customer customer, LocalDateTime returnDate) {
		System.out.print("Do you want to give us some feedback? (Y/N): ");
		if (sc.next().equalsIgnoreCase("Y")) {
			sc.nextLine();
			System.out.print("Enter your rating (1-5): ");
			int rating = sc.nextInt();
			sc.nextLine();
			System.out.print("Enter your review: ");
			String comment = sc.nextLine();

			LocalDateTime reviewDate = LocalDateTime.now();
			reviews.add(new Review(customer, car, rating, comment, reviewDate, returnDate));
			saveDataToFile();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			System.out.println("Thank you for your feedback! Review added at: " + reviewDate.format(formatter));
		} else {
			System.out.println("Thank you for choosing us.");
		}
	}

	private void returnCarOption(Scanner sc) {
		System.out.println("== Return a Car ==");
		System.out.print("Enter your name: ");
		String custName = sc.nextLine();

		System.out.print("Enter the Car ID: ");
		String carId = sc.nextLine();

		Optional<BookedCarInformation> bookedInfo = bookedCarInformations.stream()
				.filter(b -> b.getCar().getCarId().equalsIgnoreCase(carId)
						&& b.getCustomer().getName().equalsIgnoreCase(custName))
				.findFirst();

		if (bookedInfo.isEmpty()) {
			System.out.println("No booking found. Please check your details.");
			return;
		}

		BookedCarInformation bookedCar = bookedInfo.get();
		LocalDateTime returnDate = LocalDateTime.now();
		returnCar(bookedCar.getCar(), bookedCar);
		addReview(sc, bookedCar.getCar(), bookedCar.getCustomer(), returnDate);
	}

	private void displayAvailableCars() {
		// Sắp xếp danh sách cars theo carId
		cars.sort((car1, car2) -> car1.getCarId().compareTo(car2.getCarId()));
		
		System.out.println("== Available Cars ==");
		System.out.printf("%-10s %-15s %-20s %-10s\n", "Car ID", "Brand", "Model", "Available");
		System.out.println("----------------------------------------");
		for (Car car : cars) {
			System.out.printf("%-10s %-15s %-20s %-10d\n", car.getCarId(), car.getBrand(), car.getModel(),
					car.getNoOfAvailableCar());
		}

		// Kiểm tra xem danh sách có bị trùng hay không
		Set<String> carIds = new HashSet<>();
		boolean hasDuplicates = false;
		for (Car car : cars) {
			if (!carIds.add(car.getCarId())) {
				hasDuplicates = true;
				System.out.println("Duplicate detected: " + car.getCarId());
			}
		}

		if (!hasDuplicates) {
			System.out.print("");
		}
	}

	private void displayReviews() {
		System.out.println("== Customer Reviews ==");

		if (reviews.isEmpty()) {
			System.out.println("No reviews available.");
			return;
		}

		for (int i = 0; i < reviews.size(); i++) {
			System.out.println((i + 1) + ". " + reviews.get(i).toString());
		}
	}

	public void displayRentedCars() {
		System.out.println("\n== List of Rented Cars ==");

		if (bookedCarInformations.isEmpty()) {
			System.out.println("No cars have been rented yet.");
			return;
		}

		System.out.printf("%-20s %-10s %-15s %-10s %-20s\n", "Customer Name", "Car ID", "Car Brand", "Model",
				"Rental Time");
		System.out.println("---------------------------------------------------------------------------------");

		for (BookedCarInformation booking : bookedCarInformations) {
			Customer customer = booking.getCustomer();
			Car car = booking.getCar();
			LocalDateTime rentalTime = booking.getRentDate();
			String formattedRentalTime = rentalTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
			System.out.printf("%-20s %-10s %-15s %-10s %-20s\n", customer.getName(), car.getCarId(), car.getBrand(),
					car.getModel(), formattedRentalTime);
		}
	}

}
