package app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import org.hibernate.cfg.Configuration;

import tables.*;

public class  MyApp {

    private static AtomicInteger counter = new AtomicInteger();
    private static Scanner scanner = new Scanner(System.in);
    private static Session session;

    public static void main(String[] args) {

        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();


        int choice = -1;

        while (choice != 0) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Dodaj klienta");
            System.out.println("2. Dodaj instalację");
            System.out.println("3. Dodaj płatność");
            System.out.println("4. Wyświetl klientów");
            System.out.println("5. Wyświetl instalacje");
            System.out.println("6. Wyświetl płatności");
            System.out.println("7. Usuń klienta");
            System.out.println("8. Usuń instalację");
            System.out.println("9. Usuń płatność");
            System.out.println("0. Wyjdź");

            choice = scanner.nextInt();
            scanner.nextLine(); // wyczyszczenie bufora

            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    addInstallation();
                    break;
                case 3:
                    addPayment();
                    break;
                case 4:
                    displayClients();
                    break;
                case 5:
                    displayInstallations();
                    break;
                case 6:
                    displayPayments();
                    break;
                case 7:
                    deleteClient();
                    break;
                case 8:
                    deleteInstallation();
                    break;
                case 9:
                    deletePayment();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Nieznana opcja");
            }
            transaction.commit();
        }

        session.close();
        sessionFactory.close();
    }

    private static void addClient() {
        System.out.println("Podaj imię klienta:");
        String firstName = scanner.nextLine();

        System.out.println("Podaj nazwisko klienta:");
        String lastName = scanner.nextLine();

        System.out.println("Podaj numer telefonu klienta:");
        String phoneNumber = scanner.nextLine();

        Klient klient = new Klient(firstName, lastName, phoneNumber);
        Transaction transaction = session.beginTransaction();
        session.save(klient);
        transaction.commit();
        System.out.println("Dodano klienta");
    }

    private static void addInstallation() {
        System.out.println("Podaj adres instalacji:");
        String address = scanner.nextLine();
        System.out.println("Podaj numer routera:");
        String router = scanner.nextLine();
        System.out.println("Podaj typ uslugi:");
        String typUslugi = scanner.nextLine();

        System.out.println("Podaj identyfikator klienta:");
        int clientId = scanner.nextInt();
        scanner.nextLine(); // wyczyszczenie bufora

        Klient klient = session.get(Klient.class, clientId);

        if (klient == null) {
            System.out.println("Nie znaleziono klienta o podanym identyfikatorze");
            return;
        }
        // generate a new integer id
        int id = counter.incrementAndGet();
        Instalacja instalacja = new Instalacja(id,address,router,typUslugi, klient);
        Transaction transaction = session.beginTransaction();
        session.save(instalacja);
        transaction.commit();
        System.out.println("Dodano instalację");
    }

    private static void addPayment() {
        System.out.println("Podaj identyfikator instalacji:");
        int installationId = scanner.nextInt();
        scanner.nextLine(); // wyczyszczenie bufora

        Instalacja instalacja = session.get(Instalacja.class, installationId);

        if (instalacja == null) {
            System.out.println("Nie znaleziono instalacji o podanym identyfikatorze");
            return;
        }

        System.out.println("Podaj kwotę płatności:");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine(); // wyczyszczenie bufora

        System.out.println("Podaj datę płatności (format YYYY-MM-DD):");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        NaliczoneNaleznosci platnosc = new NaliczoneNaleznosci( new Date(),amount, instalacja);
        Transaction transaction = session.beginTransaction();
        session.save(platnosc);
        transaction.commit();
        System.out.println("Dodano płatność");
    }

    private static void displayClients() {
        System.out.println("Lista klientów:");
        for (Klient klient : session.createQuery("from Klient", Klient.class).list()) {
            System.out.println(klient);
        }
    }

    private static void displayInstallations() {
        System.out.println("Lista instalacji:");
        for (Instalacja instalacja : session.createQuery("from Instalacja", Instalacja.class).list()) {
            System.out.println(instalacja);
        }
    }

    private static void displayPayments() {
        System.out.println("Lista płatności:");
        for (NaliczoneNaleznosci platnosc : session.createQuery("from Platnosc", NaliczoneNaleznosci.class).list()) {
            System.out.println(platnosc);
        }
    }

    private static void deleteClient() {
        System.out.println("Podaj identyfikator klienta:");
        int clientId = scanner.nextInt();
        scanner.nextLine(); // wyczyszczenie bufora

        Klient klient = session.get(Klient.class, clientId);

        if (klient == null) {
            System.out.println("Nie znaleziono klienta o podanym identyfikatorze");
            return;
        }

        Transaction transaction = session.beginTransaction();
        session.delete(klient);
        transaction.commit();
        System.out.println("Usunięto klienta");
    }

    private static void deleteInstallation() {
        System.out.println("Podaj identyfikator instalacji:");
        int installationId = scanner.nextInt();
        scanner.nextLine(); // wyczyszczenie bufora

        Instalacja instalacja = session.get(Instalacja.class, installationId);

        if (instalacja == null) {
            System.out.println("Nie znaleziono instalacji o podanym identyfikatorze");
            return;
        }

        Transaction transaction = session.beginTransaction();
        session.delete(instalacja);
        transaction.commit();
        System.out.println("Usunięto instalację");
    }

    private static void deletePayment() {
        System.out.println("Podaj identyfikator płatności:");
        int paymentId = scanner.nextInt();
        scanner.nextLine(); // wyczyszczenie bufora

        NaliczoneNaleznosci platnosc = session.get(NaliczoneNaleznosci.class, paymentId);

        if (platnosc == null) {
            System.out.println("Nie znaleziono płatności o podanym identyfikatorze");
            return;
        }

        Transaction transaction = session.beginTransaction();
        session.delete(platnosc);
        transaction.commit();
        System.out.println("Usunięto płatność");
    }
}