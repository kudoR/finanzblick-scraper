package eu.ffs.finanzen.finanzblickscraper.entity;

import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Buchung {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CsvBindByPosition(position = 0)
    private String buchungsdatum;

    @CsvBindByPosition(position = 1)
    private String empfaenger;

    @CsvBindByPosition(position = 2)
    private String verwendungszweck;

    @CsvBindByPosition(position = 3)
    private String buchungstext;

    @CsvBindByPosition(position = 4)
    private String betrag;

    @CsvBindByPosition(position = 5)
    private String iban;

    @CsvBindByPosition(position = 6)
    private String bic;

    @CsvBindByPosition(position = 7)
    private String kategorie;

    @CsvBindByPosition(position = 8)
    private String notiz;

    public String getBuchungsdatum() {
        return buchungsdatum;
    }

    public void setBuchungsdatum(String buchungsdatum) {
        this.buchungsdatum = buchungsdatum;
    }

    public String getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(String empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public void setVerwendungszweck(String verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    public String getBuchungstext() {
        return buchungstext;
    }

    public void setBuchungstext(String buchungstext) {
        this.buchungstext = buchungstext;
    }

    public String getBetrag() {
        return betrag;
    }

    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}
