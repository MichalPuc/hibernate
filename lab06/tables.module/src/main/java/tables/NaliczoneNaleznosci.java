package tables;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "Naliczone_naleznosci")
public class NaliczoneNaleznosci {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "termin_platnosci")
    private Date terminPlatnosci;

    @Column(name = "kwota_do_zaplaty")
    private BigDecimal kwotaDoZaplaty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instalacja_id")
    private Instalacja instalacja;

    public NaliczoneNaleznosci() {}

    public NaliczoneNaleznosci(Date terminPlatnosci, BigDecimal kwotaDoZaplaty, Instalacja instalacja) {
        this.terminPlatnosci = terminPlatnosci;
        this.kwotaDoZaplaty = kwotaDoZaplaty;
        this.instalacja = instalacja;
    }

    public int getId() {
        return id;
    }

    public Date getTerminPlatnosci() {
        return terminPlatnosci;
    }

    public void setTerminPlatnosci(Date terminPlatnosci) {
        this.terminPlatnosci = terminPlatnosci;
    }

    public BigDecimal getKwotaDoZaplaty() {
        return kwotaDoZaplaty;
    }

    public void setKwotaDoZaplaty(BigDecimal kwotaDoZaplaty) {
        this.kwotaDoZaplaty = kwotaDoZaplaty;
    }

    public Instalacja getInstalacja() {
        return instalacja;
    }

    public void setInstalacja(Instalacja instalacja) {
        this.instalacja = instalacja;
    }

}
