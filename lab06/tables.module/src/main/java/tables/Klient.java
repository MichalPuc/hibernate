package tables;

import javax.persistence.*;

@Entity
@Table(name = "Klient")
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imie;

    private String nazwisko;

    @Column(unique = true)
    private String numer_klienta;

    public Klient() {}

    public Klient(String imie, String nazwisko, String numer_klienta) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.numer_klienta = numer_klienta;
    }

    // getters and setters

}
