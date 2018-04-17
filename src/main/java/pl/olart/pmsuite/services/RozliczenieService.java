package pl.olart.pmsuite.services;

import pl.olart.pmsuite.model.Metadane;
import pl.olart.pmsuite.model.RozliczenieRFPBean;
import pl.olart.pmsuite.model.TypKosztuBean;
import pl.olart.pmsuite.model.Wynik;
import pl.olart.pmsuite.util.CommonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 02:17
 */
public class RozliczenieService {

    public static List<TypKosztuBean> wyodrebnijTypyKosztow(List<RozliczenieRFPBean> lista) {
        HashSet<String> listaTypow = new HashSet<String>();
        for(RozliczenieRFPBean rozliczenie : lista) {
            listaTypow.add(rozliczenie.getTypElementuKosztu());
        }
        List<TypKosztuBean> typy = new ArrayList<TypKosztuBean>();
        for(String typ : listaTypow) {
            typy.add(new TypKosztuBean(typ));
        }
        przypiszOsobyDoTypu(typy, lista);
        Collections.sort(typy);
        return typy;
    }

    private static void przypiszOsobyDoTypu(List<TypKosztuBean> typy, List<RozliczenieRFPBean> lista) {
        for(TypKosztuBean typ : typy) {
            for(RozliczenieRFPBean rozliczenie : lista) {
                if(rozliczenie.getTypElementuKosztu().equals(typ.getNazwa())) {
                    if(typ.getKtoPrzypisany() == null) {
                        typ.setKtoPrzypisany(rozliczenie.getWygenerowanyPrzez());
                    } else if (!typ.getKtoPrzypisany().contains(rozliczenie.getWygenerowanyPrzez())) {
                        typ.setKtoPrzypisany(typ.getKtoPrzypisany() + ", " + rozliczenie.getWygenerowanyPrzez());
                    }
                }
            }
        }
    }

    public static void sklasyfikujWstepnieTypyKosztow(List<TypKosztuBean> nieprzydzielone, List<TypKosztuBean> etatIdzielo, List<TypKosztuBean> kontraktowcy, List<TypKosztuBean> zewnetrzni, List<TypKosztuBean> nieosobowe) {
        List<TypKosztuBean> nieprzydzieloneCopy = (List<TypKosztuBean>) ((ArrayList<TypKosztuBean>)  nieprzydzielone).clone();
        for(TypKosztuBean typBean : nieprzydzieloneCopy) {
            String typ = typBean.getNazwa();
            //reguly
            if(typ.toLowerCase().contains("zewn".toLowerCase())) {
                zewnetrzni.add(typBean);
                nieprzydzielone.remove(typBean);
            } else if(typ.toLowerCase().contains("zus".toLowerCase())) {
                etatIdzielo.add(typBean);
                nieprzydzielone.remove(typBean);
            } else if(typ.toLowerCase().contains("prac".toLowerCase())) {
                etatIdzielo.add(typBean);
                nieprzydzielone.remove(typBean);
            }  else if(typ.toLowerCase().contains("kontr".toLowerCase())) {
                kontraktowcy.add(typBean);
                nieprzydzielone.remove(typBean);
//            }   else if(typ.toLowerCase().contains("obc".toLowerCase())) {
//                zewnetrzni.add(typBean);
//                nieprzydzielone.remove(typBean); //nie zawsze to dobrze dziala, mozna to przywrocic jesli umozliwie przenoszenie typow kosztow pomiedzy wszystkimi sekcjami
            }   else if(typ.toLowerCase().contains("pozos".toLowerCase())) {
                nieosobowe.add(typBean);
                nieprzydzielone.remove(typBean);
            }
        }
    }

    public static void wyliczWartosci(List<Wynik> wyniki, List<RozliczenieRFPBean> listaPierwotnaZCSV, List<TypKosztuBean> etatIdzielo, List<TypKosztuBean> kontraktowcy, List<TypKosztuBean> zewnetrzni,List<TypKosztuBean> nieosobowe, Metadane metadane) {
        String rok = wyliczRok(listaPierwotnaZCSV);
        metadane.setRok(rok);

        if(wyniki.size() == 0) {    //pierwszy wrzucony plik
            Wynik wynikEtat = new Wynik("Rzeczywiste koszty bezpośrednie pracowników etatowych lub o dzieło");
            Wynik wynikKontrakt = new Wynik("Rzeczywiste koszty bezpośrednie współpracowników kontraktowych");
            Wynik wynikZewnetrzni = new Wynik("Rzeczywiste koszty bezpośrednie współpracowników zewnętrznych");
            Wynik wynikNieosobowe = new Wynik("Rzeczywiste koszty nieosobowe");
            Wynik wynikLacznieOsobowe = new Wynik("Koszty osobowe lacznie");
            wyniki.add(wynikEtat);
            wyniki.add(wynikKontrakt);
            wyniki.add(wynikZewnetrzni);
            wyniki.add(wynikNieosobowe);
            wyniki.add(wynikLacznieOsobowe);
        }

        Wynik wynikEtat = wyniki.get(0);
        Wynik wynikKontrakt = wyniki.get(1);
        Wynik wynikZewnetrzni = wyniki.get(2);
        Wynik wynikNieosobowe = wyniki.get(3);
        Wynik wynikLacznieOsobowe = wyniki.get(4);

        wyliczWartosci(listaPierwotnaZCSV, etatIdzielo, wynikEtat, metadane);
        wyliczWartosci(listaPierwotnaZCSV, kontraktowcy, wynikKontrakt, metadane);
        wyliczWartosci(listaPierwotnaZCSV, zewnetrzni, wynikZewnetrzni, metadane);
        wyliczWartosci(listaPierwotnaZCSV, nieosobowe, wynikNieosobowe, metadane);
        wyliczLacznieOsobowe(wynikLacznieOsobowe, wynikEtat, wynikKontrakt, wynikZewnetrzni);
    }

    private static void wyliczLacznieOsobowe(Wynik wynikLacznieOsobowe, Wynik wynikEtat, Wynik wynikKontrakt, Wynik wynikZewnetrzni) {
        wynikLacznieOsobowe.przyjmijWartosci(wynikEtat);
        wynikLacznieOsobowe.dodaj(wynikKontrakt);
        wynikLacznieOsobowe.dodaj(wynikZewnetrzni);
    }

    private static void wyliczWartosci(List<RozliczenieRFPBean> listaPierwotnaZCSV, List<TypKosztuBean> etatIdzielo, Wynik wynik, Metadane metadane) {
        Map<Integer, Double> wynikMap = new HashMap<Integer, Double>();
        inicjujMape(wynikMap, wynik);

        for(TypKosztuBean typKosztuBean : etatIdzielo) {
            wypelnijMape(listaPierwotnaZCSV, wynikMap, typKosztuBean, metadane);
        }
        przypiszElementyMapyDoWyniku(wynikMap, wynik);
    }

    private static void wypelnijMape(List<RozliczenieRFPBean> listaPierwotnaZCSV, Map<Integer, Double> wynikMap, TypKosztuBean typKosztuBean, Metadane metadane) {
        for(RozliczenieRFPBean rozliczenie : listaPierwotnaZCSV) {
            if(typKosztuBean.getNazwa().equals(rozliczenie.getTypElementuKosztu())) {
                Date dataDostawy = rozliczenie.getDataDostawy();
                Calendar cal = Calendar.getInstance();
                cal.setTime(dataDostawy);
                Integer miesiac = cal.get(Calendar.MONTH);
                wynikMap.put(miesiac, wynikMap.get(miesiac) + rozliczenie.getKwotaNetto());
                metadane.setLacznie(metadane.getLacznie() + rozliczenie.getKwotaNetto());
            }
        }
    }

    private static Double getWynikDlaMiesiaca(Wynik wynik, Integer miesiac) {
        Object retobj
                = CommonUtils.wywolajMetodeBezArgumentowa(wynik, "getWynik" + (miesiac+1));

        return (Double) retobj;
    }

    private static String wyliczRok(List<RozliczenieRFPBean> listaPierwotnaZCSV) {
        if(listaPierwotnaZCSV != null && listaPierwotnaZCSV.size() > 0) {
            RozliczenieRFPBean rozliczenie = listaPierwotnaZCSV.get(0);
            Calendar cal = Calendar.getInstance();
            cal.setTime(rozliczenie.getDataDostawy());
            int year = cal.get(Calendar.YEAR);
            return year + "";
        }

        return null;
    }

    private static void przypiszElementyMapyDoWyniku(Map<Integer, Double> wynikMap, Wynik wynik) {
        for(Map.Entry<Integer, Double> entry : wynikMap.entrySet()) {
            CommonUtils.wywolajMetodeJednoArgumentowa(wynik, "setWynik" + (entry.getKey()+1), Double.class, entry.getValue());
        }

    }

    private static void inicjujMape(Map<Integer, Double> wynikMap, Wynik wynik) {
        for (int i = 0; i < 12; i++) {
            wynikMap.put(i,  getWynikDlaMiesiaca(wynik, i));
        }

    }
}
