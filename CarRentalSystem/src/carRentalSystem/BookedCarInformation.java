package carRentalSystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookedCarInformation implements Serializable {
	private static final long serialVersionUID = 1L; // Khuyến nghị thêm serialVersionUID
	private Car car;
	private Customer customer;
	private int days;
	private LocalDateTime rentDate;
	private LocalDateTime returnDate;

	public BookedCarInformation(Car car, Customer customer, int days) {
		this.car = car;
		this.customer = customer;
		this.days = days;
		this.rentDate = LocalDateTime.now(); // Ghi lại thời gian thuê
		this.returnDate = rentDate.plusDays(days); // Mặc định khi chưa trả xe
	}

	public Car getCar() {
		return car;
	}

	public Customer getCustomer() {
		return customer;
	}

	public int getDays() {
		return days;
	}

	public LocalDateTime getRentDate() {
		return rentDate;
	}

	public LocalDateTime getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate;
	}

	// Hiển thị thông tin thời gian dễ đọc
	public String formatDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return dateTime != null ? dateTime.format(formatter) : "Not Returned";
	}

	@Override
	public String toString() {
		return "BookedCarInformation{" + "car=" + car.getCarId() + ", customer=" + customer.getName() + ", days=" + days
				+ ", rentDate=" + formatDateTime(rentDate) + ", returnDate=" + formatDateTime(returnDate) + '}';
	}
}