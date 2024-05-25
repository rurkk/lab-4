package mappers;

import dto.CatDto;
import dto.OwnerDto;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import entity.Cat;
import entity.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
@ExtensionMethod(CatMapper.class)
public class OwnerMapper {
    public OwnerDto asDto(Owner owner) {
        Objects.requireNonNull(owner);
        List<CatDto> catDto = new ArrayList<>();
        for (Cat cat : owner.getCatList()) {
            catDto.add(cat.asDto());
        }

        return new OwnerDto(owner.getId(), owner.getName(), owner.getBirthDate(), catDto);
    }
}
