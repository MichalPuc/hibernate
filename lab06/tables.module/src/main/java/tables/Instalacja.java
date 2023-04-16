package tables;

import javax.persistence.*;

@Entity
@Table(name = "Instalacja")
public class Instalacja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "adres")
    private String adres;

    @Column(name = "numer_routera")
    private String numerRoutera;

    @Column(name = "typ_uslugi")
    private String typUslugi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "klient_id")
    private Klient klient;

    // Constructor
    public Instalacja(int id,String adres,String numerRoutera,String typUslugi,Klient klient) {
        this.adres =adres;
        this.numerRoutera = numerRoutera;
        this.typUslugi = typUslugi;
        this.klient = klient;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getNumerRoutera() {
        return numerRoutera;
    }

    public void setNumerRoutera(String numerRoutera) {
        this.numerRoutera = numerRoutera;
    }

    public String getTypUslugi() {
        return typUslugi;
    }

    public void setTypUslugi(String typUslugi) {
        this.typUslugi = typUslugi;
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }
}

