package core.screening.persistence.repository;

import core.screening.persistence.entity.Screening;
import core.screening.persistence.entity.ScreeningId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, ScreeningId> {

    Optional<Screening> findByScreeningId(ScreeningId screeningId);

}
