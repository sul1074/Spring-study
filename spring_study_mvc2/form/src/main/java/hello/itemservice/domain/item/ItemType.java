package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {

    BOOK("도서"),
    FOOD("음식"),
    ETC("기타");

    private final String description;
}
