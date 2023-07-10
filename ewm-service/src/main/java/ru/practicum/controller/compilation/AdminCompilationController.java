package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto dto) {
        return compilationService.createCompilation(dto);
    }

    @DeleteMapping("{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable("compilationId") Long compilationId) {
        compilationService.deleteCompilationById(compilationId);
    }

    @PatchMapping("{compilationId}")
    public CompilationDto updateCompilation(@PathVariable("compilationId") Long compilationId,
                                            @RequestBody @Valid UpdateCompilationRequest dto) {
        return compilationService.updateCompilation(compilationId, dto);
    }
}
