package pl.olart.pmsuite.model;

import java.io.Serializable;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 18:54
 */
public class Metadane implements Serializable
{
    private Double lacznie = 0d;
    private String rok = null;

    public Double getLacznie() {
        return lacznie;
    }

    public void setLacznie(Double lacznie) {
        this.lacznie = lacznie;
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public void clear() {
        lacznie = 0d;
        rok = null;
    }
}
