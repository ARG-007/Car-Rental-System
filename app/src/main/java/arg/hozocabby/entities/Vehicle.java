package arg.hozocabby.entities;


import java.util.Arrays;
import java.util.NoSuchElementException;

public class Vehicle {

    private Integer id = -1, seats;
    private String name;
    private Double chargePerKm, mileage;
    private Account owner;
    private FuelType  fuelType;
    private VehicleType vehicleType;
    private VehicleStatus status = VehicleStatus.AVAILABLE;

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
            return Arrays.stream(values).filter(e -> e.getOrdinal()==ordinal).findFirst().orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }

    public enum FuelType {
        PETROL(1),
        DIESEL(2),
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
            return Arrays.stream(values).filter(e -> e.getOrdinal()==ordinal).findFirst().orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }

    public enum VehicleStatus {
        AVAILABLE(1),
        BOOKED(2),
        RETIRED(3);

        private final int ordinal;
        private final static VehicleStatus[] values;

        static {
            values = values();
        }

        VehicleStatus(int ord){
            ordinal = ord;
        }

        public int getOrdinal(){
            return ordinal;
        }


        public static VehicleStatus valueOf(int ordinal) throws NoSuchElementException {
            return Arrays.stream(values).filter(e -> e.getOrdinal()==ordinal).findFirst().orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }


    public Vehicle(String name, VehicleType vehicleType, Account owner, int seats, double cpk, double mileage,FuelType fuelType){
        this.name = name;
        this.vehicleType = vehicleType;
        this.owner = owner;
        this.seats = seats;
        this.chargePerKm = cpk;
        this.mileage = mileage;
        this.fuelType = fuelType;
    }

    public Vehicle(int id, String name, VehicleType vehicleType, Account owner, int seats, double cpk, double mileage,FuelType fuelType){
        this(name,vehicleType, owner, seats, cpk, mileage, fuelType);
        this.id = id;
    }

    public Vehicle(int id, String name, VehicleType vehicleType, Account owner, int seats, double cpk, double mileage,FuelType fuelType, VehicleStatus status) {
        this(id, name, vehicleType, owner, seats, cpk, mileage, fuelType);
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() {
        return seats;
    }

    public Double getChargePerKm() {
        return chargePerKm;
    }

    public Double getMileage() {
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

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
