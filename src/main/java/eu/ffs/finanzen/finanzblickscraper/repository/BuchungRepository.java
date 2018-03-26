package eu.ffs.finanzen.finanzblickscraper.repository;

import eu.ffs.finanzen.finanzblickscraper.entity.Buchung;
import org.springframework.data.repository.CrudRepository;

public interface BuchungRepository extends CrudRepository<Buchung, Long> {
}
