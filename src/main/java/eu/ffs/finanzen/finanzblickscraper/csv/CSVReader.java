package eu.ffs.finanzen.finanzblickscraper.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import eu.ffs.finanzen.finanzblickscraper.entity.Buchung;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class CSVReader {

    public List<Buchung> doRead(String filePath) throws FileNotFoundException {
        return new CsvToBeanBuilder(new FileReader(filePath))
                .withType(Buchung.class)
                .withSkipLines(1)
                .build().parse();
    }

}
