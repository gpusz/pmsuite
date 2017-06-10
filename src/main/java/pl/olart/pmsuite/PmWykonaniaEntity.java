package pl.olart.pmsuite;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: Grzegorz
 * Date: 09.06.17
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "PM_WYKONANIA", schema = "PMSUITE", catalog = "")
@Entity
public class PmWykonaniaEntity {
    @Id
    private Integer id;
    private Timestamp dataWykonania;
    private String uzytkownik;
    private String nazwaPliku;

    @javax.persistence.Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 9, precision = 0)
    @Basic
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @javax.persistence.Column(name = "DATA_WYKONANIA", nullable = false, insertable = true, updatable = true, length = 7, precision = 0)
    @Basic
    public Timestamp getDataWykonania() {
        return dataWykonania;
    }

    public void setDataWykonania(Timestamp dataWykonania) {
        this.dataWykonania = dataWykonania;
    }

    @javax.persistence.Column(name = "UZYTKOWNIK", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @Basic
    public String getUzytkownik() {
        return uzytkownik;
    }

    public void setUzytkownik(String uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    @javax.persistence.Column(name = "NAZWA_PLIKU", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @Basic
    public String getNazwaPliku() {
        return nazwaPliku;
    }

    public void setNazwaPliku(String nazwaPliku) {
        this.nazwaPliku = nazwaPliku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PmWykonaniaEntity that = (PmWykonaniaEntity) o;

        if (dataWykonania != null ? !dataWykonania.equals(that.dataWykonania) : that.dataWykonania != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (nazwaPliku != null ? !nazwaPliku.equals(that.nazwaPliku) : that.nazwaPliku != null) return false;
        if (uzytkownik != null ? !uzytkownik.equals(that.uzytkownik) : that.uzytkownik != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataWykonania != null ? dataWykonania.hashCode() : 0);
        result = 31 * result + (uzytkownik != null ? uzytkownik.hashCode() : 0);
        result = 31 * result + (nazwaPliku != null ? nazwaPliku.hashCode() : 0);
        return result;
    }
}
