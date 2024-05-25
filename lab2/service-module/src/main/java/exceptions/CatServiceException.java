package exceptions;

public class CatServiceException extends CatException {
    private CatServiceException(String message) {
        super(message);
    }

    public static CatServiceException noSuchCat(int id) {
        return new CatServiceException("Cat with id " + id + " doesn't exist");
    }

    public static CatServiceException noSuchOwner(int ownerId) {
        return new CatServiceException("Owner with id " + ownerId + " doesn't exist");
    }

    public static CatServiceException noUser(String username) {
        return new CatServiceException("User with username " + username + " not found");
    }
}
