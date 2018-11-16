package eu.ffs.finanzen.finanzblickscraper.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class CSVReader {

    public List<BuchungDTO> doRead(File file) throws FileNotFoundException {
        return new CsvToBeanBuilder(new FileReader(file))
                .withSeparator(';')
                .withType(BuchungDTO.class)
                .withSkipLines(1)
                .build().parse();
    }

}
