package ru.practicum.service.category;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.category.Category;

import java.util.List;

public interface CategoryService {
    void deleteCategoryById(long id);

    NewCategoryDto getCategoryById(long id);

    Category getCategoryByIdOrThrow(long categoryId);

    List<NewCategoryDto> findAllCategories(PageRequest pageRequest);

    NewCategoryDto createCategory(NewCategoryDto newCategoryDto);

    NewCategoryDto updateCategory(long id, NewCategoryDto newCategoryDto);
}
