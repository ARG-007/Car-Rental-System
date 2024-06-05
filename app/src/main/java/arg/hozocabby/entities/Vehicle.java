package arg.hozocabby.entities;


import java.util.Arrays;
import java.util.NoSuchElementException;

public class Vehicle {

    private Integer id, seats;
    private Double chargePerKm, mileage;
    private Account owner;
    private FuelType  fuelType;
    private VehicleType vehicleType;


    public Vehicle(int id, VehicleType vehicleType, Account owner, int seats, double cpk, double mileage,FuelType fuelType){
        this(vehicleType, owner, seats, cpk, mileage, fuelType);
        this.id = id;
    }

    public Vehicle(VehicleType vehicleType, Account owner, int seats, double cpk, double mileage,FuelType fuelType){
        this.vehicleType = vehicleType;
        this.owner = owner;
        this.seats = seats;
        this.chargePerKm = cpk;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.id = null;
    }

    public int getId() {
        return id;
    }

    public int getSeats() {
        return seats;
    }

    public double getChargePerKm() {
        return chargePerKm;
    }

    public double getMileage() {
        return mileage;
    }

    public Account getOwner() {
        return owner;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }



    public enum VehicleType {
        SEDAN(1),
        HATCHBACK(2),
        SUV(3),
        MAXICAB(4);

        private final int ordinal;
        private final static VehicleType[] values;

        static {
            values = values();
        }

        VehicleType(int ord){
            ordinal = ord;
        }

        public int getOrdinal(){
            return ordinal;
        }


        public static VehicleType valueOf(int ordinal) throws NoSuchElementException {
            return Arrays.stream(values).findFirst().filter(e -> e.getOrdinal()==ordinal).orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }

    public enum FuelType {
        Petrol(1),
        Diesel(2),
        CNG(3);

        private final int ordinal;
        private final static FuelType[] values;

        static {
            values = values();
        }

        FuelType(int ord){
            ordinal = ord;
        }

        public int getOrdinal(){
            return ordinal;
        }


        public static FuelType valueOf(int ordinal) throws NoSuchElementException {
            return Arrays.stream(values).findFirst().filter(e -> e.getOrdinal()==ordinal).orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }
}
