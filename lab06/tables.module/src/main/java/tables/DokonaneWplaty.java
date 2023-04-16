package tables;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Dokonane_wplaty")
public class DokonaneWplaty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "termin_wplaty")
    private Date terminWplaty;

    @Column(name = "kwota_wplaty")
    private double kwotaWplaty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instalacja_id")
    private Instalacja instalacja;

    public DokonaneWplaty() {}

    public DokonaneWplaty(Date terminWplaty, double kwotaWplaty, Instalacja instalacja) {
        this.terminWplaty = terminWplaty;
        this.kwotaWplaty = kwotaWplaty;
        this.instalacja = instalacja;
    }

    public int getId() {
        return id;
    }

    public Date getTerminWplaty() {
        return terminWplaty;
    }

    public void setTerminWplaty(Date terminWplaty) {
        this.terminWplaty = terminWplaty;
    }

    public double getKwotaWplaty() {
        return kwotaWplaty;
    }

    public void setKwotaWplaty(double kwotaWplaty) {
        this.kwotaWplaty = kwotaWplaty;
    }

    public Instalacja getInstalacja() {
        return instalacja;
    }

    public void setInstalacja(Instalacja instalacja) {
        this.instalacja = instalacja;
    }
}

