package arg.hozocabby.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;

public class Rental {
    private Integer id;
    private RentalInfo info;

    private Account driver;
    private Double distance;
    private Date fareEndTime;
    private Double cost;
    private RentalStatus status;

    public Rental(int id, RentalInfo info) {
        this.id = id;
        this.info = info;
    }

    public enum RentalStatus {
        PENDING(1),
        ONGOING(2),
        COMPLETED(3),
        CANCELLED(4);


        private final int ordinal;
        private final static RentalStatus[] values;

        private RentalStatus(int ordinal){
            this.ordinal = ordinal;
        }


        static {
            values = values();
        }

        public int getOrdinal(){
            return ordinal;
        }


        public static RentalStatus valueOf(int ordinal) throws NoSuchElementException {
            return Arrays.stream(values).findFirst().filter(e -> e.getOrdinal()==ordinal).orElseThrow(()-> new NoSuchElementException("Enum Has No Constant With That Ordinal"));
        }

        public static int size(){
            return values.length;
        }
    }

    public Integer getId() {
        return id;
    }

    public RentalInfo getInfo() {
        return info;
    }

    public Account getDriver() {
        return driver;
    }

    public void setDriver(Account driver) {
        this.driver = driver;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getFareEndTime() {
        return fareEndTime;
    }

    public void setFareEndTime(Date fareEndTime) {
        this.fareEndTime = fareEndTime;
    }

    public Double getCost() {
        return cost;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}
