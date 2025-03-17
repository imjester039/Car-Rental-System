package carRentalSystem;

import java.io.Serializable;

public class SUV extends Car implements Serializable {
	private static final long serialVersionUID = 1L;

	private int size;

	public SUV() {

	}

	public SUV(String carId, String brand, String model, double pricePerDay, int noOfAvailableCar, int size) {
		super.setCarId(carId);
		super.setBrand(brand);
		super.setModel(model);
		super.setPricePerDay(pricePerDay);
		super.setNoOfAvailableCar(noOfAvailableCar);
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double calculatePrice(int days, int size) {
		double basePrice = super.calculatePrice(days);
		if (size == 5) {
			return basePrice + 50;
		} else
			return basePrice + 70;
	}
}
