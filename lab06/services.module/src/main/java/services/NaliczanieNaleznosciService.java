package services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import tables.DokonaneWplaty;
import tables.Instalacja;
import tables.NaliczoneNaleznosci;

public class NaliczanieNaleznosciService {
    private EntityManagerFactory entityManagerFactory;
    private String logFilepath;

    public NaliczanieNaleznosciService(String persistenceUnitName, String logFilepath) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        this.logFilepath = logFilepath;
    }

    public void naliczNaleznosci() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Query instalacjaQuery = entityManager.createQuery("SELECT i FROM Instalacja i");
            List<Instalacja> instalacje = instalacjaQuery.getResultList();

            for (Instalacja instalacja : instalacje) {
                BigDecimal kwotaDoZaplaty = obliczKwoteDoZaplaty(entityManager, instalacja);

                if (kwotaDoZaplaty.compareTo(BigDecimal.ZERO) < 0) {
                    // Jeśli kwota do zapłaty jest ujemna, to utwórz nowe naliczenie na sumę wpłat pomniejszoną o kwoty naliczeń
                    NaliczoneNaleznosci noweNaliczenie = new NaliczoneNaleznosci(new Date(), kwotaDoZaplaty.abs(), instalacja);
                    entityManager.persist(noweNaliczenie);
                }

                entityManager.flush();
                entityManager.clear();
            }

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    private BigDecimal obliczKwoteDoZaplaty(EntityManager entityManager, Instalacja instalacja) {
        BigDecimal sumaNaliczen = BigDecimal.ZERO;

        Query naliczeniaQuery = entityManager.createQuery("SELECT n FROM NaliczoneNaleznosci n WHERE n.instalacja = :instalacja AND n.terminPlatnosci <= :dzisiaj AND n.oplacone = false");
        naliczeniaQuery.setParameter("instalacja", instalacja);
        naliczeniaQuery.setParameter("dzisiaj", LocalDate.now());

        List<NaliczoneNaleznosci> naliczenia = naliczeniaQuery.getResultList();

        for (NaliczoneNaleznosci naliczenie : naliczenia) {
            sumaNaliczen = sumaNaliczen.add(naliczenie.getKwotaDoZaplaty());
            entityManager.remove(naliczenie);
        }

        Query wpataQuery = entityManager.createQuery("SELECT w FROM DokonaneWplaty w WHERE w.instalacja = :instalacja ORDER BY w.terminWplaty DESC");
        wpataQuery.setParameter("instalacja", instalacja);
        wpataQuery.setMaxResults(1);

        List<DokonaneWplaty> wplaty = wpataQuery.getResultList();

        BigDecimal sumaWplat = BigDecimal.ZERO;
        for (DokonaneWplaty wpata : wplaty) {
            entityManager.persist(wpata);
            sumaWplat = sumaWplat.add(BigDecimal.valueOf(wpata.getKwotaWplaty()));
        }

        return sumaNaliczen.subtract(sumaWplat);
    }
}