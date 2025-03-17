package carRentalSystem;

public class Main {
	public static void main(String[] args) {
		Car lamborghiniCar = new Car();
		lamborghiniCar.setCarId("C1");
		lamborghiniCar.setBrand("Lamborghini");
		lamborghiniCar.setModel("Aventador LP 750-4");
		lamborghiniCar.setNoOfAvailableCar(2);
		lamborghiniCar.setPricePerDay(7000);

		Car ferrariCar = new Car();
		ferrariCar.setCarId("C2");
		ferrariCar.setBrand("Ferrari");
		ferrariCar.setModel("Daytona SP3");
		ferrariCar.setNoOfAvailableCar(3);
		ferrariCar.setPricePerDay(5500);

		Car porscheCar = new Car();
		porscheCar.setCarId("C3");
		porscheCar.setBrand("Porsche");
		porscheCar.setModel("Cayman");
		porscheCar.setNoOfAvailableCar(4);
		porscheCar.setPricePerDay(3500);

		ElectricCars vinfastCar = new ElectricCars();
		vinfastCar.setCarId("EC1");
		vinfastCar.setBrand("Vinfast");
		vinfastCar.setModel("VF8 Luxury");
		vinfastCar.setNoOfAvailableCar(7);
		vinfastCar.setPricePerDay(500);
		vinfastCar.setPinCapacity(87);

		ElectricCars teslaCar = new ElectricCars();
		teslaCar.setCarId("EC2");
		teslaCar.setBrand("Tesla");
		teslaCar.setModel("Performance AWD");
		teslaCar.setNoOfAvailableCar(5);
		teslaCar.setPricePerDay(2000);
		teslaCar.setPinCapacity(82);

		SUV kiaCar = new SUV();
		kiaCar.setCarId("S1");
		kiaCar.setBrand("Kia");
		kiaCar.setModel("Sorento");
		kiaCar.setNoOfAvailableCar(5);
		kiaCar.setPricePerDay(3000);
		kiaCar.setSize(5);

		SUV fordCar = new SUV();
		fordCar.setCarId("S2");
		fordCar.setBrand("Ford");
		fordCar.setModel("Expedition");
		fordCar.setNoOfAvailableCar(3);
		fordCar.setPricePerDay(4000);
		fordCar.setSize(7);

		CarRentalService carRentalService = new CarRentalService();
		carRentalService.addCar(lamborghiniCar);
		carRentalService.addCar(ferrariCar);
		carRentalService.addCar(porscheCar);
		carRentalService.addCar(vinfastCar);
		carRentalService.addCar(teslaCar);
		carRentalService.addCar(kiaCar);
		carRentalService.addCar(fordCar);

		carRentalService.saveDataToFile();
		carRentalService.displayMenu();

	}
}
