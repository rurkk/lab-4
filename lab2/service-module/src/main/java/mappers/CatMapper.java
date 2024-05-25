package mappers;

import dto.CatDto;
import lombok.experimental.UtilityClass;
import entity.Cat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class CatMapper {
    public CatDto asDto(Cat cat) {
        Objects.requireNonNull(cat);
        List<Integer> friendsId = new ArrayList<>();
        for (Cat k : cat.getFriends()) {
            friendsId.add(k.getId());
        }
        return new CatDto(cat.getId(), cat.getName(), cat.getBirthDate(), cat.getBreed().name(),
                cat.getColour().name(), cat.getOwner().getId(), friendsId);
    }
}
