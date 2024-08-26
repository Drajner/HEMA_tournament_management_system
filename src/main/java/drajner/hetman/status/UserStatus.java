package drajner.hetman.status;

public enum UserStatus {
    STANDARD("STANDARD"),
    ADMIN("ADMIN"),
    BANNED("BANNED");

    private final String text;

    UserStatus(String text){
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
