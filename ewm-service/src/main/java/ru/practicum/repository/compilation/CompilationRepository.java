package ru.practicum.repository.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.compilation.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation c WHERE (:pinned IS NULL OR c.pinned = :pinned)")
    List<Compilation> findByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}
