package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable("compilationId") Long compilationId) {
        return compilationService.getCompilationById(compilationId);
    }

    @GetMapping
    public List<CompilationDto> getAllCompilationsWithFilters(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return compilationService.getAllCompilations(pinned, PageRequest.of(from, size));
    }
}
