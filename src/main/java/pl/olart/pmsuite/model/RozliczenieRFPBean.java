package pl.olart.pmsuite.model;

import java.io.Serializable;
import java.util.Date;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 01:09
 */
public class RozliczenieRFPBean implements Serializable {
    private Date dataDostawy;
    private String typElementuKosztu;
    private String wygenerowanyPrzez;
    private String identyfikatorZewnetrzny;
    private String fundusz;
    private String kontoAnalityczne;
    private Double kwotaNetto;
    private Double kwotaVat;
    private Double kwotaBrutto;
    private String opis;
    private Date dataZaplaty;

    public Date getDataDostawy() {
        return dataDostawy;
    }

    public void setDataDostawy(Date dataDostawy) {
        this.dataDostawy = dataDostawy;
    }

    public String getTypElementuKosztu() {
        return typElementuKosztu;
    }

    public void setTypElementuKosztu(String typElementuKosztu) {
        this.typElementuKosztu = typElementuKosztu;
    }

    public String getWygenerowanyPrzez() {
        return wygenerowanyPrzez;
    }

    public void setWygenerowanyPrzez(String wygenerowanyPrzez) {
        this.wygenerowanyPrzez = wygenerowanyPrzez;
    }

    public String getIdentyfikatorZewnetrzny() {
        return identyfikatorZewnetrzny;
    }

    public void setIdentyfikatorZewnetrzny(String identyfikatorZewnetrzny) {
        this.identyfikatorZewnetrzny = identyfikatorZewnetrzny;
    }

    public String getFundusz() {
        return fundusz;
    }

    public void setFundusz(String fundusz) {
        this.fundusz = fundusz;
    }

    public String getKontoAnalityczne() {
        return kontoAnalityczne;
    }

    public void setKontoAnalityczne(String kontoAnalityczne) {
        this.kontoAnalityczne = kontoAnalityczne;
    }

    public Double getKwotaNetto() {
        return kwotaNetto;
    }

    public void setKwotaNetto(Double kwotaNetto) {
        this.kwotaNetto = kwotaNetto;
    }

    public Double getKwotaVat() {
        return kwotaVat;
    }

    public void setKwotaVat(Double kwotaVat) {
        this.kwotaVat = kwotaVat;
    }

    public Double getKwotaBrutto() {
        return kwotaBrutto;
    }

    public void setKwotaBrutto(Double kwotaBrutto) {
        this.kwotaBrutto = kwotaBrutto;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Date getDataZaplaty() {
        return dataZaplaty;
    }

    public void setDataZaplaty(Date dataZaplaty) {
        this.dataZaplaty = dataZaplaty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RozliczenieRFPBean that = (RozliczenieRFPBean) o;

        if (!dataDostawy.equals(that.dataDostawy)) return false;
        if (!dataZaplaty.equals(that.dataZaplaty)) return false;
        if (!fundusz.equals(that.fundusz)) return false;
        if (!identyfikatorZewnetrzny.equals(that.identyfikatorZewnetrzny)) return false;
        if (!kontoAnalityczne.equals(that.kontoAnalityczne)) return false;
        if (!kwotaBrutto.equals(that.kwotaBrutto)) return false;
        if (!kwotaNetto.equals(that.kwotaNetto)) return false;
        if (!kwotaVat.equals(that.kwotaVat)) return false;
        if (!opis.equals(that.opis)) return false;
        if (!typElementuKosztu.equals(that.typElementuKosztu)) return false;
        if (!wygenerowanyPrzez.equals(that.wygenerowanyPrzez)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataDostawy.hashCode();
        result = 31 * result + typElementuKosztu.hashCode();
        result = 31 * result + wygenerowanyPrzez.hashCode();
        result = 31 * result + identyfikatorZewnetrzny.hashCode();
        result = 31 * result + fundusz.hashCode();
        result = 31 * result + kontoAnalityczne.hashCode();
        result = 31 * result + kwotaNetto.hashCode();
        result = 31 * result + kwotaVat.hashCode();
        result = 31 * result + kwotaBrutto.hashCode();
        result = 31 * result + opis.hashCode();
        result = 31 * result + dataZaplaty.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RozliczenieRFPBean{" +
                "dataDostawy=" + dataDostawy +
                ", typElementuKosztu='" + typElementuKosztu + '\'' +
                ", wygenerowanyPrzez='" + wygenerowanyPrzez + '\'' +
                ", identyfikatorZewnetrzny='" + identyfikatorZewnetrzny + '\'' +
                ", fundusz='" + fundusz + '\'' +
                ", kontoAnalityczne='" + kontoAnalityczne + '\'' +
                ", kwotaNetto=" + kwotaNetto +
                ", kwotaVat=" + kwotaVat +
                ", kwotaBrutto=" + kwotaBrutto +
                ", opis='" + opis + '\'' +
                ", dataZaplaty=" + dataZaplaty +
                '}';
    }
}
