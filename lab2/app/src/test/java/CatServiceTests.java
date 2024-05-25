import dao.CatDao;
import dao.OwnerDao;
import dto.CatDto;
import exceptions.CatServiceException;
import exceptions.OwnerServiceException;
import mappers.CatMapper;
import models.Breed;
import entity.Cat;
import models.Colour;
import entity.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.CatService;
import services.OwnerService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CatServiceTests {

    @Mock
    private CatDao catDao;

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private CatService catService;

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCatShouldReturnValidDto() {
        // Arrange
        int ownerId = 1;
        Owner testOwner = new Owner("TestOwner", LocalDate.of(2024, 10, 10), new ArrayList<>());
        testOwner.setId(ownerId);
        Cat testCat = new Cat("TestCat", LocalDate.of(2024, 10, 10), Breed.Unknown, Colour.Unknown, testOwner, new HashSet<>());
        CatDto expectedDto = CatMapper.asDto(testCat);

        when(ownerDao.getById(ownerId)).thenReturn(Optional.of(testOwner));
        when(catDao.update(any(Cat.class))).thenReturn(testCat);

        // Act
        CatDto result = catService.createCat("Tom", LocalDate.of(2012, 7, 15), "Siamese", "White", ownerId);

        // Assert
        verify(ownerDao).getById(ownerId);
        verify(catDao).update(any(Cat.class));
        assertEquals(expectedDto, result);
    }

    @Test
    void unfriendCatsShouldRemoveFriendships() {
        // Arrange
        int ownerId = 1;
        Owner testOwner = new Owner("TestOwner", LocalDate.of(2024, 10, 10), new ArrayList<>());
        testOwner.setId(ownerId);
        Cat testCat1 = new Cat("TestCat1", LocalDate.of(2024, 10, 10), Breed.Unknown, Colour.Unknown, testOwner, new HashSet<>());
        Cat testCat2 = new Cat("TestCat2", LocalDate.of(2024, 10, 10), Breed.Unknown, Colour.Unknown, testOwner, new HashSet<>());

        testCat1.addFriend(testCat1);
        testCat2.addFriend(testCat2);

        int catId1 = 1;
        int catId2 = 2;

        testCat1.setId(catId1);
        testCat2.setId(catId2);

        when(catDao.getById(catId1)).thenReturn(Optional.of(testCat1));
        when(catDao.getById(catId2)).thenReturn(Optional.of(testCat2));

        // Act
        catService.unfriendCats(catId1, catId2);

        // Assert
        assertFalse(testCat1.getFriends().contains(testCat2));
        assertFalse(testCat1.getFriends().contains(testCat2));
        verify(catDao).update(testCat1);
        verify(catDao).update(testCat2);
    }

    @Test
    void removeCatShouldRemoveCatAndItsRelationships() {
        // Arrange
        int catIdToRemove = 1;
        int ownerId = 1;
        Owner testOwner = new Owner("TestOwner", LocalDate.of(2024, 10, 10), new ArrayList<>());
        testOwner.setId(ownerId);
        Cat friendCat =  new Cat("TestCat1", LocalDate.of(2024, 10, 10), Breed.Unknown, Colour.Unknown, testOwner, new HashSet<>());
        testOwner.addCat(friendCat);

        friendCat.setId(catIdToRemove);
        friendCat.setOwner(testOwner);
        friendCat.setFriends(new HashSet<>(Collections.singletonList(friendCat)));

        when(catDao.getById(catIdToRemove)).thenReturn(Optional.of(friendCat));
        when(catDao.getAllFriends(catIdToRemove)).thenReturn(Collections.singletonList(friendCat));
        doNothing().when(catDao).remove(friendCat);
        when(catDao.update(friendCat)).thenReturn(friendCat);

        // Act
        catService.removeCat(catIdToRemove);

        // Assert
        verify(catDao).getById(catIdToRemove);
        verify(catDao).remove(friendCat);
    }

    @Test
    void removeCatShouldThrowExceptionIfCatNotFound() {
        // Arrange
        int nonExistentCatId = 1;

        // Set up the mock to return an empty Optional when getById is called
        when(catDao.getById(nonExistentCatId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CatServiceException.class, () -> {
            catService.removeCat(nonExistentCatId);
        }, CatServiceException.noSuchCat(nonExistentCatId).getMessage());

        // Additional verification to ensure no more interactions with the mock after the exception
        verify(catDao).getById(nonExistentCatId);
        verifyNoMoreInteractions(catDao);
    }

    @Test
    void findAllCatsShouldThrowExceptionWhenOwnerNotFound() {
        // Arrange
        int nonExistentOwnerId = 99;
        when(ownerDao.getById(nonExistentOwnerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OwnerServiceException.class, () -> ownerService.findAllCats(nonExistentOwnerId));

        // Verify that getById is called but getAllCats is not, due to the exception
        verify(ownerDao).getById(nonExistentOwnerId);
        verify(ownerDao, never()).getAllCats(nonExistentOwnerId);
    }

    @Test
    void findAllCatsShouldReturnListOfCatDtosWhenOwnerExists() {
        // Arrange
        int ownerId = 1;
        Owner testOwner = new Owner();
        testOwner.setId(ownerId);

        List<Cat> cats = List.of(
                new Cat("Cat1", LocalDate.now(), Breed.Unknown, Colour.Unknown, testOwner, new HashSet<>()),
                new Cat("Cat2", LocalDate.now(), Breed.Unknown, Colour.Unknown, testOwner, new HashSet<>())
        );
        when(ownerDao.getById(ownerId)).thenReturn(Optional.of(testOwner));
        when(ownerDao.getAllCats(ownerId)).thenReturn(cats);

        List<CatDto> expectedDtos = cats.stream().map(CatMapper::asDto).collect(Collectors.toList());

        // Act
        List<CatDto> actualDtos = ownerService.findAllCats(ownerId);

        // Assert
        assertEquals(expectedDtos, actualDtos);

        // Verify the interactions with the mock
        verify(ownerDao).getById(ownerId);
        verify(ownerDao).getAllCats(ownerId);
    }
}
