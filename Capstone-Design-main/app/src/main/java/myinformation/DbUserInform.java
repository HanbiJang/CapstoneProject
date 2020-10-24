package myinformation;

public class DbUserInform {
    String userID;
    Integer myDogAge_int;
    String myDogSize;
    String myDogSpecies;
    String badDog;

    public Integer getMyDogAge_int() {
        return myDogAge_int;
    }

    public String getBadDog() {
        return badDog;
    }

    public String getMyDogSize() {
        return myDogSize;
    }

    public String getMyDogSpecies() {
        return myDogSpecies;
    }

    public String getUserID() {
        return userID;
    }

    public void setBadDog(String badDog) {
        this.badDog = badDog;
    }

    public void setMyDogAge_int(Integer myDogAge_int) {
        this.myDogAge_int = myDogAge_int;
    }

    public void setMyDogSize(String myDogSize) {
        this.myDogSize = myDogSize;
    }

    public void setMyDogSpecies(String myDogSpecies) {
        this.myDogSpecies = myDogSpecies;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}


