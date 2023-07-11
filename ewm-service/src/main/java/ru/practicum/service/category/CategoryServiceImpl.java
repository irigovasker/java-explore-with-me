package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.repository.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventsRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public NewCategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Transactional
    public void deleteCategoryById(long categoryId) {
        Category category = getCategoryByIdOrThrow(categoryId);
        List<Event> events = eventsRepository.findByCategory(category);
        if (events.size() > 0) {
            throw new ConflictException(
                    String.format("Cannot delete the category with id=%d because of existing event in this category", categoryId)
            );
        }
        categoryRepository.deleteById(categoryId);
        log.info("Deleted category with id=" + categoryId);
    }

    @Transactional
    public NewCategoryDto updateCategory(long categoryId, NewCategoryDto newCategoryDto) {
        Category category = getCategoryByIdOrThrow(categoryId);
        category.setName(newCategoryDto.getName() == null ? category.getName() : newCategoryDto.getName());
        log.info("Updated category with id=" + categoryId);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    public List<NewCategoryDto> findAllCategories(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public NewCategoryDto getCategoryById(long categoryId) {
        return categoryMapper.toCategoryDto(getCategoryByIdOrThrow(categoryId));
    }

    public Category getCategoryByIdOrThrow(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " not found"));
    }
}
