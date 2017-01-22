package pl.olart.pmsuite.model;

import java.io.Serializable;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 16:43
 */
public class Wynik implements Serializable {
    private String nazwa;
    private Double wynik1 = 0d;
    private Double wynik2 = 0d;
    private Double wynik3 = 0d;
    private Double wynik4 = 0d;
    private Double wynik5 = 0d;
    private Double wynik6 = 0d;
    private Double wynik7 = 0d;
    private Double wynik8 = 0d;
    private Double wynik9 = 0d;
    private Double wynik10 = 0d;
    private Double wynik11 = 0d;
    private Double wynik12 = 0d;

    public Wynik(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Double getWynik1() {
        return wynik1;
    }

    public void setWynik1(Double wynik1) {
        this.wynik1 = wynik1;
    }

    public Double getWynik2() {
        return wynik2;
    }

    public void setWynik2(Double wynik2) {
        this.wynik2 = wynik2;
    }

    public Double getWynik3() {
        return wynik3;
    }

    public void setWynik3(Double wynik3) {
        this.wynik3 = wynik3;
    }

    public Double getWynik4() {
        return wynik4;
    }

    public void setWynik4(Double wynik4) {
        this.wynik4 = wynik4;
    }

    public Double getWynik5() {
        return wynik5;
    }

    public void setWynik5(Double wynik5) {
        this.wynik5 = wynik5;
    }

    public Double getWynik6() {
        return wynik6;
    }

    public void setWynik6(Double wynik6) {
        this.wynik6 = wynik6;
    }

    public Double getWynik7() {
        return wynik7;
    }

    public void setWynik7(Double wynik7) {
        this.wynik7 = wynik7;
    }

    public Double getWynik8() {
        return wynik8;
    }

    public void setWynik8(Double wynik8) {
        this.wynik8 = wynik8;
    }

    public Double getWynik9() {
        return wynik9;
    }

    public void setWynik9(Double wynik9) {
        this.wynik9 = wynik9;
    }

    public Double getWynik10() {
        return wynik10;
    }

    public void setWynik10(Double wynik10) {
        this.wynik10 = wynik10;
    }

    public Double getWynik11() {
        return wynik11;
    }

    public void setWynik11(Double wynik11) {
        this.wynik11 = wynik11;
    }

    public Double getWynik12() {
        return wynik12;
    }

    public void setWynik12(Double wynik12) {
        this.wynik12 = wynik12;
    }

    public void dodaj(Wynik skladnik) {
        setWynik1(getWynik1() + skladnik.getWynik1());
        setWynik2(getWynik2() + skladnik.getWynik2());
        setWynik3(getWynik3() + skladnik.getWynik3());
        setWynik4(getWynik4() + skladnik.getWynik4());
        setWynik5(getWynik5() + skladnik.getWynik5());
        setWynik6(getWynik6() + skladnik.getWynik6());
        setWynik7(getWynik7() + skladnik.getWynik7());
        setWynik8(getWynik8() + skladnik.getWynik8());
        setWynik9(getWynik9() + skladnik.getWynik9());
        setWynik10(getWynik10() + skladnik.getWynik10());
        setWynik11(getWynik11() + skladnik.getWynik11());
        setWynik12(getWynik12() + skladnik.getWynik12());
    }

    public void przyjmijWartosci(Wynik wartosc) {
        setWynik1(wartosc.getWynik1());
        setWynik2(wartosc.getWynik2());
        setWynik3(wartosc.getWynik3());
        setWynik4(wartosc.getWynik4());
        setWynik5(wartosc.getWynik5());
        setWynik6(wartosc.getWynik6());
        setWynik7(wartosc.getWynik7());
        setWynik8(wartosc.getWynik8());
        setWynik9(wartosc.getWynik9());
        setWynik10(wartosc.getWynik10());
        setWynik11(wartosc.getWynik11());
        setWynik12(wartosc.getWynik12());

    }
}
