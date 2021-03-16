package manu.apps.androidcodingstarterpack.classes;

public class Vehicle {

    int vehicleId;
    String vehicleName;

    public Vehicle() {

    }

    public Vehicle(int vehicleId, String vehicleName) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
