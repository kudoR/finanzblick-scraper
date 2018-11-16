package eu.ffs.finanzen.finanzblickscraper.csv;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.util.List;

@RunWith(SpringRunner.class)
public class CSVReaderTest {


    CSVReader underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new CSVReader();
    }

    @Test
    public void doRead() throws FileNotFoundException {
        String filePath="/Users/j/Downloads/Buchungsliste.csv";
        List<BuchungDTO> buchungs = underTest.doRead(filePath);
        System.out.println(buchungs);
    }
}
