package arg.hozocabby.entities;

public record Place(int id, String name, double latitude, double longitude) {
    /**
     * Uses Standard two point distance calculation.
     * @param b Place to be calculated for distance
     * @return double Distance between this and passed Place Object
     */
    double distanceBetween(Place b){
        return Math.sqrt(
                Math.pow(b.latitude() - this.latitude, 2)
                + Math.pow(b.longitude() - this.longitude, 2)
            );
    }
}
