package eu.ffs.finanzen.finanzblickscraper.service;

import eu.ffs.finanzen.finanzblickscraper.csv.BuchungDTO;
import eu.ffs.finanzen.finanzblickscraper.entity.Buchung;
import eu.ffs.finanzen.finanzblickscraper.repository.BuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BuchungsService {

    @Autowired
    private BuchungRepository buchungRepository;

    public void processBuchungen(List<BuchungDTO> buchungDTOS) {
        LocalDate maxDate = buchungRepository.findTop1ByOrderByBuchungsdatumDesc().getBuchungsdatum();
        buchungRepository.saveAll(
                buchungDTOS
                        .stream()
                        .filter(filterBuchungen(maxDate))
                        .map(this::toBuchung)
                        .collect(Collectors.toList())
        );
    }


    private Predicate<BuchungDTO> filterBuchungen(LocalDate maxDate) {
        return buchungDTO -> isBuchungsdatumValid(buchungDTO.getBuchungsdatum(), maxDate);
    }

    private boolean isBuchungsdatumValid(String datumAsString, LocalDate maxDate) {
        LocalDate localDate = LocalDate.parse(datumAsString);
        return localDate.isAfter(maxDate);
    }

    private Buchung toBuchung(BuchungDTO buchungDTO) {
        Buchung buchung = new Buchung();
        buchung.setBetrag(new BigDecimal(buchungDTO.getBetrag()));
        buchung.setBic(buchungDTO.getBic());
        buchung.setBuchungsdatum(LocalDate.parse(buchungDTO.getBuchungsdatum()));
        buchung.setBuchungstext(buchungDTO.getBuchungstext());
        buchung.setEmpfaenger(buchungDTO.getEmpfaenger());
        buchung.setIban(buchungDTO.getBic());
        buchung.setKategorie(buchungDTO.getKategorie());
        buchung.setNotiz(buchungDTO.getNotiz());
        buchung.setVerwendungszweck(buchungDTO.getVerwendungszweck());
        return buchung;
    }
}
