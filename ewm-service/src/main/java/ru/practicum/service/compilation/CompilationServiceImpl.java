package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.compilation.CompilationMapper;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.compilation.CompilationRepository;
import ru.practicum.repository.event.EventRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventsRepository;
    private final EventMapper eventMapper;

    @Transactional
    public CompilationDto createCompilation(NewCompilationDto dto) {
        dto.setPinned(ofNullable(dto.getPinned()).orElse(false));
        List<Event> events = dto.getEvents() == null ? Collections.emptyList() : eventsRepository.findAllById(dto.getEvents());
        return compilationMapper.toCompilationDto(
                compilationRepository.save(compilationMapper.toCompilation(dto, events)), toShortEventDtoList(events)
        );
    }

    @Transactional
    public void deleteCompilationById(Long compilationId) {
        compilationRepository.deleteById(compilationId);
    }

    @Transactional
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest dto) {
        List<Event> eventList;
        List<ShortEventDto> eventShortDtoList;
        Compilation compilation = getCompilationByIdOrElseThrow(compilationId);

        if (dto.getEvents() != null) {
            eventList = eventsRepository.findAllById(dto.getEvents());
            eventShortDtoList = eventList.stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
            compilation.setEvents(eventList);
        } else {
            eventList = compilation.getEvents();
            eventShortDtoList = eventList.stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }

        ofNullable(dto.getPinned()).ifPresent(compilation::setPinned);
        ofNullable(dto.getTitle()).ifPresent(compilation::setTitle);
        Compilation newCompilation = compilationRepository.save(compilation);

        return compilationMapper.toCompilationDto(newCompilation, eventShortDtoList);
    }

    public CompilationDto getCompilationById(Long compilationId) {
        return compilationMapper.toCompilationDto(getCompilationByIdOrElseThrow(compilationId));
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned, PageRequest pageRequest) {
        return compilationRepository.findByPinned(pinned, pageRequest)
                .stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    private Compilation getCompilationByIdOrElseThrow(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + "  not found"));
    }

    private List<ShortEventDto> toShortEventDtoList(List<Event> events) {
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }
}
