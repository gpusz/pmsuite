package pl.olart.pmsuite.model;

import java.io.Serializable;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 02:39
 */
public class TypKosztuBean implements Serializable, Comparable {
    private String nazwa;
    private String ktoPrzypisany;

    public TypKosztuBean(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKtoPrzypisany() {
        return ktoPrzypisany;
    }

    public void setKtoPrzypisany(String ktoPrzypisany) {
        this.ktoPrzypisany = ktoPrzypisany;
    }

    @Override
    public int compareTo(Object o) {
        return this.getNazwa().compareTo(((TypKosztuBean) o).getNazwa());
    }
}
