package pl.olart.pmsuite.services;

import pl.olart.pmsuite.model.Metadane;
import pl.olart.pmsuite.model.RozliczenieRFPBean;
import pl.olart.pmsuite.model.TypKosztuBean;
import pl.olart.pmsuite.model.Wynik;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * User: grp
 * Date: 16.06.16
 * Time: 02:17
 */
public class RozliczenieService {

    @PersistenceContext(unitName = "pmsuite")
    private EntityManager em;

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
            if(typ.toLowerCase().contains("kontr".toLowerCase())) {
                kontraktowcy.add(typBean);
                nieprzydzielone.remove(typBean);
            } else if(typ.toLowerCase().contains("zus".toLowerCase())) {
                etatIdzielo.add(typBean);
                nieprzydzielone.remove(typBean);
            } else if(typ.toLowerCase().contains("prac".toLowerCase())) {
                etatIdzielo.add(typBean);
                nieprzydzielone.remove(typBean);
            }  else if(typ.toLowerCase().contains("zewn".toLowerCase())) {
                zewnetrzni.add(typBean);
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
        if(miesiac == 0 && wynik.getWynik1() != null) {
            return wynik.getWynik1();
        } else if(miesiac == 1 && wynik.getWynik2() != null) {
            return wynik.getWynik2();
        }  else if(miesiac == 2 && wynik.getWynik3() != null) {
            return wynik.getWynik3();
        } else if(miesiac == 3 && wynik.getWynik4() != null) {
            return wynik.getWynik4();
        } else if(miesiac == 4 && wynik.getWynik5() != null) {
            return wynik.getWynik5();
        } else if(miesiac == 5 && wynik.getWynik6() != null) {
            return wynik.getWynik6();
        } else if(miesiac == 6 && wynik.getWynik7() != null) {
            return wynik.getWynik7();
        } else if(miesiac == 7 && wynik.getWynik8() != null) {
            return wynik.getWynik8();
        } else if(miesiac == 8 && wynik.getWynik9() != null) {
            return wynik.getWynik9();
        } else if(miesiac == 9 && wynik.getWynik10() != null) {
            return wynik.getWynik10();
        } else if(miesiac == 10 && wynik.getWynik11() != null) {
            return wynik.getWynik11();
        } else if(miesiac == 11 && wynik.getWynik12() != null) {
            return wynik.getWynik12();
        }
        return 0d;
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

    private static void inicjujMape(Map<Integer, Double> wynikMap, Wynik wynik) {
        for (int i = 0; i < 12; i++) {
            wynikMap.put(i,  getWynikDlaMiesiaca(wynik, i));
        }

    }
}
