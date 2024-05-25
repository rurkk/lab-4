package exceptions;

public class UserServiceException extends RuntimeException {
    private UserServiceException(String message) {super(message);}

    public static UserServiceException usernameAlreadyExist(String username){
        return new UserServiceException("User with this username:" + username + " already exist");
    }
    public static UserServiceException noRole(String role){
        return new UserServiceException("This role: " + role + " not exist");
    }
    public static UserServiceException ownerRegistered(int id){
        return new UserServiceException("Owner with id" + id + " already registered");
    }
    public static UserServiceException noOwner(int id){
        return new UserServiceException("Owner with id: " + id + " not exist");
    }
    public static UserServiceException noUser(String username){
        return new UserServiceException("This username: " + username + " not exist");
    }
}
