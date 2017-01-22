package pl.olart.pmsuite.beans;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import pl.olart.pmsuite.model.Metadane;
import pl.olart.pmsuite.model.Wynik;
import pl.olart.pmsuite.services.RozliczenieService;
import pl.olart.pmsuite.model.RozliczenieRFPBean;
import pl.olart.pmsuite.model.TypKosztuBean;
import pl.olart.pmsuite.util.FacesUtils;
import pl.olart.pmsuite.util.ParserCSV;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: grp
 * Date: 28.09.15
 * Time: 18:22
 */
@ManagedBean
@SessionScoped
public class ParsujArkusz implements Serializable {
    private List<TypKosztuBean> typyKosztow;
    private List<TypKosztuBean> etatIdzielo;
    private List<TypKosztuBean> kontraktowcy;
    private List<TypKosztuBean> zewnetrzni;
    private List<TypKosztuBean> nieosobowe;
    private List<Wynik> wyniki;
    private String nazwaPliku = null;
    private Metadane metadane;
    List<RozliczenieRFPBean> listaPierwotnaZCSV = null;

    @PostConstruct
    public void init() {
        typyKosztow = new ArrayList<TypKosztuBean>();
        etatIdzielo = new ArrayList<TypKosztuBean>();
        kontraktowcy = new ArrayList<TypKosztuBean>();
        zewnetrzni = new ArrayList<TypKosztuBean>();
        nieosobowe = new ArrayList<TypKosztuBean>();
        wyniki = new ArrayList<Wynik>();
        metadane = new Metadane();
    }

    public void handleFileUpload(FileUploadEvent event) {
        if(event.getFile() != null) {
            try {
//                clearAll();
                if(typyKosztow.size() > 0) {
                    FacesUtils.addErrorMessage("Blad", "Zmapuj najpierw wszystkie typy kosztow z poprzedniego pliku");
                    return;
                }
                etatIdzielo = new ArrayList<TypKosztuBean>(); //zapamietywanie poprzednich wartosci jest poprzez wynik, po kazdym wrzuceniu pliku trzeba zmapowac wszystkie typy
                kontraktowcy = new ArrayList<TypKosztuBean>();
                zewnetrzni = new ArrayList<TypKosztuBean>();
                nieosobowe = new ArrayList<TypKosztuBean>();

                listaPierwotnaZCSV = ParserCSV.parsujCSV(event.getFile().getInputstream());
                nazwaPliku = event.getFile().getFileName();
                typyKosztow = RozliczenieService.wyodrebnijTypyKosztow(listaPierwotnaZCSV);
                RozliczenieService.sklasyfikujWstepnieTypyKosztow(typyKosztow, etatIdzielo, kontraktowcy, zewnetrzni, nieosobowe);
                wyliczWartosci();
                FacesUtils.addInfoMessage("Przetworzono plik ", nazwaPliku);
            } catch (Throwable e) {
                FacesUtils.addErrorMessage("Blad ", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void onEtatDrop(DragDropEvent ddEvent) {
        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
        etatIdzielo.add(typKosztu);
        typyKosztow.remove(typKosztu);
        wyliczWartosci();
    }

//    public void onEtatFromKontraktowcyDrop(DragDropEvent ddEvent) {
//        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
//        etatIdzielo.add(typKosztu);
//        kontraktowcy.remove(typKosztu);
//        wyliczWartosci();
//    }
//
//    public void onEtatFromZewnetrzniDrop(DragDropEvent ddEvent) {
//        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
//        etatIdzielo.add(typKosztu);
//        zewnetrzni.remove(typKosztu);
//        wyliczWartosci();
//    }

    public void onKontraktowcyDrop(DragDropEvent ddEvent) {
        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
        kontraktowcy.add(typKosztu);
        typyKosztow.remove(typKosztu);
        wyliczWartosci();
    }

//    public void onKontraktowcyFromEtatDrop(DragDropEvent ddEvent) {
//        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
//        kontraktowcy.add(typKosztu);
//        etatIdzielo.remove(typKosztu);
//        wyliczWartosci();
//    }
//
//    public void onKontraktowcyFromZewnetrzniDrop(DragDropEvent ddEvent) {
//        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
//        kontraktowcy.add(typKosztu);
//        zewnetrzni.remove(typKosztu);
//        wyliczWartosci();
//    }

    public void onZewnetrzniDrop(DragDropEvent ddEvent) {
        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
        zewnetrzni.add(typKosztu);
        typyKosztow.remove(typKosztu);
        wyliczWartosci();
    }

//    public void onZewnetrzniFromKontraktowcyDrop(DragDropEvent ddEvent) {
//        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
//        zewnetrzni.add(typKosztu);
//        kontraktowcy.remove(typKosztu);
//        wyliczWartosci();
//    }
//
//    public void onZewnetrzniFromEtatDrop(DragDropEvent ddEvent) {
//        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
//        zewnetrzni.add(typKosztu);
//        etatIdzielo.remove(typKosztu);
//        wyliczWartosci();
//    }


    public void onNieosoboweDrop(DragDropEvent ddEvent) {
        TypKosztuBean typKosztu = ((TypKosztuBean) ddEvent.getData());
        nieosobowe.add(typKosztu);
        typyKosztow.remove(typKosztu);
        wyliczWartosci();
    }

    private void wyliczWartosci() {
        if(typyKosztow != null && typyKosztow.size() == 0) {
            RozliczenieService.wyliczWartosci(wyniki, listaPierwotnaZCSV, etatIdzielo, kontraktowcy, zewnetrzni, nieosobowe, metadane);
        }
    }

    public List<TypKosztuBean> getTypyKosztow() {
        return typyKosztow;
    }
    public List<TypKosztuBean> getEtatIdzielo() {
        return etatIdzielo;
    }
    public List<TypKosztuBean> getKontraktowcy() {
        return kontraktowcy;
    }
    public List<TypKosztuBean> getZewnetrzni() {
        return zewnetrzni;
    }
    public String getNazwaPliku() {
        return nazwaPliku;
    }

    public List<Wynik> getWyniki() {
        return wyniki;
    }

    public void setWyniki(List<Wynik> wyniki) {
        this.wyniki = wyniki;
    }

    public Metadane getMetadane() {
        return metadane;
    }

    public void setMetadane(Metadane metadane) {
        this.metadane = metadane;
    }


    public List<TypKosztuBean> getNieosobowe() {
        return nieosobowe;
    }

    public void setNieosobowe(List<TypKosztuBean> nieosobowe) {
        this.nieosobowe = nieosobowe;
    }

    public void clearActionListener(ActionEvent al) {
        clearAll();
    }

    private void clearAll() {
        nazwaPliku = null;
        if(typyKosztow != null) {
            typyKosztow.clear();
        }
        if(etatIdzielo != null) {
            etatIdzielo.clear();
        }
        if(kontraktowcy != null) {
            kontraktowcy.clear();
        }
        if(zewnetrzni != null) {
            zewnetrzni.clear();
        }
        if(nieosobowe != null) {
            nieosobowe.clear();
        }
        if(metadane != null) {
            metadane.clear();
        }
        if(wyniki != null) {
            wyniki.clear();
        }
    }
}