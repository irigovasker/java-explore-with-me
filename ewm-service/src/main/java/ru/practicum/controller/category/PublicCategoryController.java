package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<NewCategoryDto> findAllCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.findAllCategories(PageRequest.of(from, size));
    }

    @GetMapping("/{categoryId}")
    public NewCategoryDto findCategoryById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
}
