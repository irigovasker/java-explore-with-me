package ru.practicum.repository.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventsByInitiator_Id(long initiator, Pageable pageable);

    List<Event> findByCategory(Category category);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (CAST(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)"
    )
    List<Event> getEventsWithUsersStatesCategoriesDateTime(@Param("users") List<Long> users,
                                                           @Param("states") List<State> states,
                                                           @Param("categories") List<Long> categories,
                                                           @Param("rangeStart") LocalDateTime rangeStart,
                                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                                           Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) ) " +
            "OR (:text IS NULL OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')))) " +
            "AND (:state IS NULL OR e.state = :state) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)" +
            "AND (e.participantLimit > e.confirmedRequests)" +
            "ORDER BY e.eventDate DESC"
    )
    List<Event> getAvailableEventsWithFiltersDateSorted(@Param("text") String text,
                                                        @Param("state") State state,
                                                        @Param("categories") List<Long> categories,
                                                        @Param("paid") Boolean paid,
                                                        @Param("rangeStart") LocalDateTime rangeStart,
                                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                                        Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%') ) ) " +
            "OR (:text IS NULL OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%') ) )) " +
            "AND (:state IS NULL OR e.state = :state) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)" +
            "AND (e.participantLimit > e.confirmedRequests)"
    )
    List<Event> getAvailableEventsWithFilters(@Param("text") String text,
                                              @Param("state") State state,
                                              @Param("categories") List<Long> categories,
                                              @Param("paid") Boolean paid,
                                              @Param("rangeStart") LocalDateTime rangeStart,
                                              @Param("rangeEnd") LocalDateTime rangeEnd,
                                              Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%') ) ) " +
            "OR (:text IS NULL OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%') ) )) " +
            "AND (:state IS NULL OR e.state = :state) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)" +
            "ORDER BY e.eventDate DESC")
    List<Event> getAllEventsWithFiltersDateSorted(@Param("text") String text,
                                                  @Param("state") State state,
                                                  @Param("categories") List<Long> categories,
                                                  @Param("paid") Boolean paid,
                                                  @Param("rangeStart") LocalDateTime rangeStart,
                                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                                  Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%') ) ) " +
            "OR (:text IS NULL OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%') ) )) " +
            "AND (:state IS NULL OR e.state = :state) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)"
    )
    List<Event> getAllEventsWithFilters(@Param("text") String text,
                                        @Param("state") State state,
                                        @Param("categories") List<Long> categories,
                                        @Param("paid") Boolean paid,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        Pageable pageable);
}
