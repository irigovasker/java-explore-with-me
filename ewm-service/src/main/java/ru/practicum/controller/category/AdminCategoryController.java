package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewCategoryDto createCategory(@RequestBody @Valid NewCategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @DeleteMapping("{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
    }

    @PatchMapping("{categoryId}")
    public NewCategoryDto updateCategory(@PathVariable("categoryId") Long categoryId,
                                                         @RequestBody @Valid NewCategoryDto dto) {
        return categoryService.updateCategory(categoryId, dto);
    }
}
