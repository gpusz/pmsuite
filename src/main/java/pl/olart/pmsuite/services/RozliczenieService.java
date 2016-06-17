package pl.olart.pmsuite.services;

import pl.olart.pmsuite.model.Metadane;
import pl.olart.pmsuite.model.RozliczenieRFPBean;
import pl.olart.pmsuite.model.TypKosztuBean;
import pl.olart.pmsuite.model.Wynik;

import java.text.SimpleDateFormat;
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

    public static void sklasyfikujWstepnieTypyKosztow(List<TypKosztuBean> nieprzydzielone, List<TypKosztuBean> etatIdzielo, List<TypKosztuBean> kontraktowcy, List<TypKosztuBean> zewnetrzni) {
        List<TypKosztuBean> nieprzydzieloneCopy = (List<TypKosztuBean>) ((ArrayList<TypKosztuBean>)  nieprzydzielone).clone();
        for(TypKosztuBean typBean : nieprzydzieloneCopy) {
            String typ = typBean.getNazwa();
            //reguly
            if(typ.toLowerCase().contains("zewn".toLowerCase())) {
                zewnetrzni.add(typBean);
                nieprzydzielone.remove(typBean);
            }
            else if(typ.toLowerCase().contains("zus".toLowerCase())) {
                etatIdzielo.add(typBean);
                nieprzydzielone.remove(typBean);
            } else if(typ.toLowerCase().contains("prac".toLowerCase())) {
                etatIdzielo.add(typBean);
                nieprzydzielone.remove(typBean);
            }  else if(typ.toLowerCase().contains("kontr".toLowerCase())) {
                kontraktowcy.add(typBean);
                nieprzydzielone.remove(typBean);
            }   else if(typ.toLowerCase().contains("obc".toLowerCase())) {
                zewnetrzni.add(typBean);
                nieprzydzielone.remove(typBean);
            }
        }
    }

    public static ArrayList<Wynik> wyliczWartosci(List<RozliczenieRFPBean> listaPierwotnaZCSV, List<TypKosztuBean> etatIdzielo, List<TypKosztuBean> kontraktowcy, List<TypKosztuBean> zewnetrzni, Metadane metadane) {
        ArrayList<Wynik> wyniki = new ArrayList<Wynik>();
        String rok = wyliczRok(listaPierwotnaZCSV);
        metadane.setRok(rok);
        Double lacznie = 0d;

        Wynik wynikEtat = new Wynik("Rzeczywiste koszty bezpośrednie pracowników etatowych lub o dzieło", rok);
        Wynik wynikKontrakt = new Wynik("Rzeczywiste koszty bezpośrednie współpracowników kontraktowych", rok);
        Wynik wynikZewnetrzni = new Wynik("Rzeczywiste koszty bezpośrednie współpracowników zewnętrznych", rok);

        wyliczWartosci(listaPierwotnaZCSV, etatIdzielo, wynikEtat, metadane);
        wyliczWartosci(listaPierwotnaZCSV, kontraktowcy, wynikKontrakt, metadane);
        wyliczWartosci(listaPierwotnaZCSV, zewnetrzni, wynikZewnetrzni, metadane);

        wyniki.add(wynikEtat);
        wyniki.add(wynikKontrakt);
        wyniki.add(wynikZewnetrzni);

        return wyniki;
    }

    private static void wyliczWartosci(List<RozliczenieRFPBean> listaPierwotnaZCSV, List<TypKosztuBean> etatIdzielo, Wynik wynikEtat, Metadane metadane) {
        Map<Integer, Double> wynikMap = new HashMap<Integer, Double>();
        inicjujMape(wynikMap);

        for(TypKosztuBean typKosztuBean : etatIdzielo) {
            wypelnijMape(listaPierwotnaZCSV, wynikMap, typKosztuBean, metadane);
        }
        przypiszElementyMapyDoWyniku(wynikMap, wynikEtat);
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
            if(entry.getKey() == 0) {
                wynik.setWynik1(entry.getValue());
            } else if(entry.getKey() == 1) {
                wynik.setWynik2(entry.getValue());
            }  else if(entry.getKey() == 2) {
                wynik.setWynik3(entry.getValue());
            } else if(entry.getKey() == 3) {
                wynik.setWynik4(entry.getValue());
            } else if(entry.getKey() == 4) {
                wynik.setWynik5(entry.getValue());
            } else if(entry.getKey() == 5) {
                wynik.setWynik6(entry.getValue());
            } else if(entry.getKey() == 6) {
                wynik.setWynik7(entry.getValue());
            } else if(entry.getKey() == 7) {
                wynik.setWynik8(entry.getValue());
            } else if(entry.getKey() == 8) {
                wynik.setWynik9(entry.getValue());
            } else if(entry.getKey() == 9) {
                wynik.setWynik10(entry.getValue());
            } else if(entry.getKey() == 10) {
                wynik.setWynik11(entry.getValue());
            } else if(entry.getKey() == 11) {
                wynik.setWynik12(entry.getValue());
            }
        }

    }

    private static void inicjujMape(Map<Integer, Double>wynikMap) {
        wynikMap.put(0, 0d);
        wynikMap.put(1, 0d);
        wynikMap.put(2, 0d);
        wynikMap.put(3, 0d);
        wynikMap.put(4, 0d);
        wynikMap.put(5, 0d);
        wynikMap.put(6, 0d);
        wynikMap.put(7, 0d);
        wynikMap.put(8, 0d);
        wynikMap.put(9, 0d);
        wynikMap.put(10, 0d);
        wynikMap.put(11, 0d);
        wynikMap.put(12, 0d);

    }
}
