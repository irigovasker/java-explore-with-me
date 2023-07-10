package ru.practicum.mapper.category;

import org.mapstruct.Mapper;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.category.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    NewCategoryDto toCategoryDto(Category category);

    Category toCategory(NewCategoryDto newCategoryDto);
}
