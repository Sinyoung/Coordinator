package app.kandroid.org.coordinator;

/**
 * Created by isin-yeong on 17. 6. 29..
 */
public class User {

    private String phone;

    private static User ourInstance = new User();

    public static User getInstance() {

        if(ourInstance == null){
            ourInstance = new User();
        }
        return ourInstance;
    }

    private User() {
    }

    private void setPhone(String phone){
        this.phone = phone;
    }

    private String getPhone(){
        return phone;
    }
}

