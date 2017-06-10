package pl.olart.pmsuite.model;

import org.supercsv.cellprocessor.constraint.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Grzegorz
 * Date: 09.06.17
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "pm_wykonania")
public class Wykonania {
    @Id
    @GeneratedValue(generator = "pm_wykonania_id", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_wykonania", nullable = false)
    private Date dataWykonania;

    @Column( nullable = false)
    private String uzytkownik;

    @Column(name = "nazwa_pliku", nullable = false)
    private String nazwaPliku;

    public Date getDataWykonania() {
        return dataWykonania;
    }

    public void setDataWykonania(Date dataWykonania) {
        this.dataWykonania = dataWykonania;
    }

    public String getUzytkownik() {
        return uzytkownik;
    }

    public void setUzytkownik(String uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public String getNazwaPliku() {
        return nazwaPliku;
    }

    public void setNazwaPliku(String nazwaPliku) {
        this.nazwaPliku = nazwaPliku;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
