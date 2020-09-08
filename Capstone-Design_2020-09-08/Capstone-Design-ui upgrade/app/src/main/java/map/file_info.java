package map;

public class file_info {
    private String parkName;
    private String category;
    private String address_land;
    private double latitude;
    private double longitude;


    public String getParkName(){
        return parkName;
    }

    public void setParkName(String parkName){
        this.parkName=parkName;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public String getAddress_land(){
        return address_land;
    }

    public void setAddress_land(String address_land){
        this.address_land=address_land;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude=latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }


    @Override
    public String toString() {
        return "test{" +
                "parkname='" + parkName + '\'' +
                ", category=" + category +
                ", address_land='" + address_land + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
