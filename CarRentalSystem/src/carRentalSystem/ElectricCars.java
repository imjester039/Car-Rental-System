package carRentalSystem;

import java.io.Serializable;

public class ElectricCars extends Car implements Serializable {
	private static final long serialVersionUID = 1L;

	private int pinCapacity;

	public ElectricCars() {

	}

	public ElectricCars(String carId, String brand, String model, double pricePerDay, int noOfAvailableCar,
			int pinCapacity) {
		super.setCarId(carId);
		super.setBrand(brand);
		super.setModel(model);
		super.setPricePerDay(pricePerDay);
		super.setNoOfAvailableCar(noOfAvailableCar);
		this.pinCapacity = pinCapacity;
	}

	public int getPinCapacity() {
		return pinCapacity;
	}

	public void setPinCapacity(int pinCapacity) {
		this.pinCapacity = pinCapacity;
	}

	@Override
	public double calculatePrice(int days) {
		double basePrice = super.calculatePrice(days);
		double chargingFee = pinCapacity * 0.05 * days;
		return basePrice + chargingFee;
	}
}
