package carRentalSystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	private Customer customer;
	private Car car;
	private int rating; // 1-5 stars
	private String comment;
	private LocalDateTime reviewDate;
	private LocalDateTime returnDate; // Thêm trường returnDate

	public Review(Customer customer, Car car, int rating, String comment, LocalDateTime reviewDate,
			LocalDateTime returnDate) {
		this.customer = customer;
		this.car = car;
		this.rating = rating;
		this.comment = comment;
		this.reviewDate = reviewDate;
		this.returnDate = returnDate; // Khởi tạo returnDate
	}

	@Override
	public String toString() {
		// Kiểm tra nếu returnDate là null
		String formattedReturnDate = (returnDate != null)
				? returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
				: "N/A"; // Giá trị mặc định nếu returnDate là null

		// Định dạng thông tin review
		String reviewInfo = String.format("Customer: %s | Rating: %d/5 | Comment: %s", customer.getName(), rating,
				comment);

		// Kết hợp ngày trả xe và thông tin review
		return formattedReturnDate + "\n" + reviewInfo;
	}
}