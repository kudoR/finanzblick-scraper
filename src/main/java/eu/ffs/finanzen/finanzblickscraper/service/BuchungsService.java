package eu.ffs.finanzen.finanzblickscraper.service;

import eu.ffs.finanzen.finanzblickscraper.csv.BuchungDTO;
import eu.ffs.finanzen.finanzblickscraper.entity.Buchung;
import eu.ffs.finanzen.finanzblickscraper.repository.BuchungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BuchungsService {

    @Autowired
    private BuchungRepository buchungRepository;

    public void processBuchungen(List<BuchungDTO> buchungDTOS) {
        Buchung byBuchungsdatumDesc = buchungRepository.findTop1ByOrderByBuchungsdatumDesc();
        LocalDate maxDate = byBuchungsdatumDesc != null ? byBuchungsdatumDesc.getBuchungsdatum() : null;
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
        if (maxDate == null) {
            return true;
        }
        LocalDate localDate = LocalDate.parse(datumAsString);
        return localDate.isAfter(maxDate);
    }

    private Buchung toBuchung(BuchungDTO source) {
        if (source != null) {
            Buchung buchung = new Buchung();
            if (source.getBetrag() != null) {
                System.out.println("source.betrag " + source.getBetrag());
                try {
                    buchung.setBetrag(new BigDecimal(source.getBetrag().replaceAll(",", ".")));
                } catch (Exception e) {
                    System.out.println("Numberformatexception...");
                }
            }
            buchung.setBic(source.getBic());
            if (source.getBuchungsdatum() != null) {
                try {
                    buchung.setBuchungsdatum(LocalDate.parse(source.getBuchungsdatum(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                } catch (Exception e) {
                    System.out.println("DateTimeFormatException");
                }
            }
            buchung.setBuchungstext(source.getBuchungstext());
            buchung.setEmpfaenger(source.getEmpfaenger());
            buchung.setIban(source.getBic());
            buchung.setKategorie(source.getKategorie());
            buchung.setNotiz(source.getNotiz());
            buchung.setVerwendungszweck(source.getVerwendungszweck());
            return buchung;
        }
        return null;
    }
}
