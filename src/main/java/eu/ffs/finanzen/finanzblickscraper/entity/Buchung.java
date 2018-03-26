package eu.ffs.finanzen.finanzblickscraper.entity;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Buchung {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String buchungsDatum;
    private String empfaenger;
    private String verwendungsZweck;
    private String buchungsText;
    private String betrag;
    private String iban;
    private String bic;
    private String kategorie;


    public String getBuchungsDatum() {
        return buchungsDatum;
    }

    public void setBuchungsDatum(String buchungsDatum) {
        this.buchungsDatum = buchungsDatum;
    }

    public String getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(String empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getVerwendungsZweck() {
        return verwendungsZweck;
    }

    public void setVerwendungsZweck(String verwendungsZweck) {
        this.verwendungsZweck = verwendungsZweck;
    }

    public String getBuchungsText() {
        return buchungsText;
    }

    public void setBuchungsText(String buchungsText) {
        this.buchungsText = buchungsText;
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
}
