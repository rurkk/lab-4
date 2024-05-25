package exceptions;

public class OwnerServiceException extends CatException {
    private OwnerServiceException(String message) {
        super(message);
    }

    public static OwnerServiceException catAlreadyExistsException(int CatId) {
        return new OwnerServiceException("Cat with id " + CatId + " already exists and is already taken");
    }

    public static OwnerServiceException noSuchOwner(int ownerId) {
        return new OwnerServiceException("Owner with id " + ownerId + " doesn't exist");
    }
}
