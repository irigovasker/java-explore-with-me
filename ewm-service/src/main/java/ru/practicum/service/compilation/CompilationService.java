package ru.practicum.service.compilation;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    void deleteCompilationById(Long compId);

    CompilationDto getCompilationById(Long compId);

    CompilationDto createCompilation(NewCompilationDto dto);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest dto);

    List<CompilationDto> getAllCompilations(Boolean pinned, PageRequest request);
}
