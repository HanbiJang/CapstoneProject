package map;

/**DB에서 가져오는 사용자 추천 장소 정보**/
public class UserPlace_data {
    String PlaceName = "";
    double Latitude = 0.0;
    double Longitude = 0.0;

    public UserPlace_data(String PlaceName, double Latitude, double Longitude){
        this.PlaceName=PlaceName;
        this.Latitude=Latitude;
        this.Longitude=Longitude;
    }

    public void setPlaceName(String PlaceName){this.PlaceName=PlaceName;}
    public String getPlaceName() {return PlaceName; }
    public void setLatitude(double latitude){this.Latitude=latitude;}
    public double getLatitude() {return Latitude;}
    public void setLongitude(double longitude){this.Longitude=longitude;}
    public double getLongitude() {return Longitude;}
}
