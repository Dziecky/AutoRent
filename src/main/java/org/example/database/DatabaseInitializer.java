package org.example.database;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitializer {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Tabela Uzytkownik
            String createUserTable = """
                    CREATE TABLE IF NOT EXISTS Uzytkownik (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        imie VARCHAR(50) NOT NULL,
                        nazwisko VARCHAR(50) NOT NULL,
                        login VARCHAR(50) NOT NULL UNIQUE,
                        haslo VARCHAR(100) NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        numer_telefonu VARCHAR(15) NOT NULL,
                        rola VARCHAR(20) NOT NULL CHECK (rola IN ('USER', 'OWNER'))
                    )
                    """;
            statement.execute(createUserTable);

            // Tabela Samochod
            String createCarTable = """
                    CREATE TABLE IF NOT EXISTS Samochod (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        marka VARCHAR(50) NOT NULL,
                        model VARCHAR(50) NOT NULL,
                        rocznik INT NOT NULL,
                        moc INT NOT NULL,
                        pojemnosc_silnika DECIMAL(4, 1) NOT NULL,
                        rodzaj_paliwa VARCHAR(20) NOT NULL CHECK (rodzaj_paliwa IN ('benzyna', 'diesel', 'lpg', 'elektryczny', 'hybryda')),
                        skrzynia_biegow VARCHAR(20) NOT NULL CHECK (skrzynia_biegow IN ('manualna', 'automatyczna')),
                        ilosc_miejsc INT NOT NULL,
                        cena_za_dzien DECIMAL(10, 2) NOT NULL,
                        image_url VARCHAR(255)
                    )
                    """;
            statement.execute(createCarTable);

            // Tabela Wypozyczenie
            String createRentalTable = """
                    CREATE TABLE IF NOT EXISTS Wypozyczenie (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        uzytkownik_id INT NOT NULL,
                        samochod_id INT NOT NULL,
                        data_wypozyczenia DATE NOT NULL,
                        data_zwrotu DATE,
                        status VARCHAR(20) DEFAULT 'AKTYWNE',
                        FOREIGN KEY (uzytkownik_id) REFERENCES Uzytkownik(id),
                        FOREIGN KEY (samochod_id) REFERENCES Samochod(id)
                    )
                    """;
            statement.execute(createRentalTable);

            // Dodanie właściciela i użytkowników na start
            String insertUser = "INSERT INTO Uzytkownik (imie, nazwisko, login, haslo, email, numer_telefonu, rola) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser)) {
                // Właściciel
                preparedStatement.setString(1, "Jan");
                preparedStatement.setString(2, "Kowalski");
                preparedStatement.setString(3, "owner1");
                preparedStatement.setString(4, BCrypt.hashpw("ownerpass", BCrypt.gensalt()));
                preparedStatement.setString(5, "jan.kowalski@example.com");
                preparedStatement.setString(6, "123456789");
                preparedStatement.setString(7, "OWNER");
                preparedStatement.executeUpdate();

                // Użytkownik 1
                preparedStatement.setString(1, "Adam");
                preparedStatement.setString(2, "Nowak");
                preparedStatement.setString(3, "user1");
                preparedStatement.setString(4, BCrypt.hashpw("userpass1", BCrypt.gensalt()));
                preparedStatement.setString(5, "adam.nowak@example.com");
                preparedStatement.setString(6, "987654321");
                preparedStatement.setString(7, "USER");
                preparedStatement.executeUpdate();

                // Użytkownik 2
                preparedStatement.setString(1, "Ewa");
                preparedStatement.setString(2, "Kowalska");
                preparedStatement.setString(3, "user2");
                preparedStatement.setString(4, BCrypt.hashpw("userpass2", BCrypt.gensalt()));
                preparedStatement.setString(5, "ewa.kowalska@example.com");
                preparedStatement.setString(6, "654987321");
                preparedStatement.setString(7, "USER");
                preparedStatement.executeUpdate();
            }

            // Dodanie 10 samochodów na start
            String insertCar = "INSERT INTO Samochod (marka, model, rocznik, moc, pojemnosc_silnika, rodzaj_paliwa, skrzynia_biegow, ilosc_miejsc, cena_za_dzien) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertCar)) {
                preparedStatement.setString(1, "Toyota");
                preparedStatement.setString(2, "Corolla");
                preparedStatement.setInt(3, 2020);
                preparedStatement.setInt(4, 132);
                preparedStatement.setDouble(5, 1.8);
                preparedStatement.setString(6, "benzyna");
                preparedStatement.setString(7, "automatyczna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 150.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Volkswagen");
                preparedStatement.setString(2, "Golf");
                preparedStatement.setInt(3, 2019);
                preparedStatement.setInt(4, 115);
                preparedStatement.setDouble(5, 1.6);
                preparedStatement.setString(6, "diesel");
                preparedStatement.setString(7, "manualna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 130.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Audi");
                preparedStatement.setString(2, "A4");
                preparedStatement.setInt(3, 2021);
                preparedStatement.setInt(4, 150);
                preparedStatement.setDouble(5, 2.0);
                preparedStatement.setString(6, "benzyna");
                preparedStatement.setString(7, "automatyczna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 200.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "BMW");
                preparedStatement.setString(2, "X3");
                preparedStatement.setInt(3, 2022);
                preparedStatement.setInt(4, 190);
                preparedStatement.setDouble(5, 2.0);
                preparedStatement.setString(6, "diesel");
                preparedStatement.setString(7, "automatyczna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 250.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Mercedes");
                preparedStatement.setString(2, "C-Class");
                preparedStatement.setInt(3, 2020);
                preparedStatement.setInt(4, 184);
                preparedStatement.setDouble(5, 1.5);
                preparedStatement.setString(6, "benzyna");
                preparedStatement.setString(7, "automatyczna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 220.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Skoda");
                preparedStatement.setString(2, "Octavia");
                preparedStatement.setInt(3, 2018);
                preparedStatement.setInt(4, 150);
                preparedStatement.setDouble(5, 1.4);
                preparedStatement.setString(6, "benzyna");
                preparedStatement.setString(7, "manualna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 120.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Ford");
                preparedStatement.setString(2, "Focus");
                preparedStatement.setInt(3, 2017);
                preparedStatement.setInt(4, 120);
                preparedStatement.setDouble(5, 1.0);
                preparedStatement.setString(6, "benzyna");
                preparedStatement.setString(7, "manualna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 100.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Tesla");
                preparedStatement.setString(2, "Model 3");
                preparedStatement.setInt(3, 2021);
                preparedStatement.setInt(4, 283);
                preparedStatement.setDouble(5, 0.0);
                preparedStatement.setString(6, "elektryczny");
                preparedStatement.setString(7, "automatyczna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 300.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Hyundai");
                preparedStatement.setString(2, "Tucson");
                preparedStatement.setInt(3, 2020);
                preparedStatement.setInt(4, 136);
                preparedStatement.setDouble(5, 1.6);
                preparedStatement.setString(6, "diesel");
                preparedStatement.setString(7, "manualna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 180.00);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Kia");
                preparedStatement.setString(2, "Sportage");
                preparedStatement.setInt(3, 2019);
                preparedStatement.setInt(4, 177);
                preparedStatement.setDouble(5, 1.6);
                preparedStatement.setString(6, "benzyna");
                preparedStatement.setString(7, "automatyczna");
                preparedStatement.setInt(8, 5);
                preparedStatement.setDouble(9, 170.00);
                preparedStatement.executeUpdate();
            }

            System.out.println("Inicjalizacja bazy danych zakończona sukcesem.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Inicjalizacja bazy danych zakończona niepowodzeniem");
        }
    }
}