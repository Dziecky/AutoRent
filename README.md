**AutoRent**

AutoRent to aplikacja konsolowa oraz webowa do wynajmu samochodów, stworzona w języku Java. Projekt umożliwia użytkownikom przeglądanie dostępnych samochodów, wynajem pojazdów, zarządzanie kontem użytkownika, a administratorom dodatkowo zarządzanie flotą pojazdów.

**Funkcjonalności:**
Dla niezalogowanych użytkowników:
 - Przeglądanie dostępnych samochodów.
 - Filtrowanie pojazdów według parametrów (np. marka, model, cena).
 - Rejestracja konta.

Dla zalogowanych użytkowników:
 - Wynajem samochodów.
 - Podgląd aktualnych i wcześniejszych wynajmów.
 - Anulowanie rezerwacji (do 2 dni przed datą wynajmu).

Dla administratorów:
 - Dodawanie, edytowanie i usuwanie samochodów.
 - Podgląd wszystkich wynajmów, wraz z informacjami o użytkownikach.

**Wymagania:**
 - Java 17 lub nowsza.
 - Maven.
 - Baza danych MySQL działająca na localhost:3306.
 - Konto administratora bazy danych z odpowiednimi uprawnieniami.

**Instalacja:**
Sklonuj repozytorium:

  git clone <URL-repozytorium>
  cd AutoRent

**Skonfiguruj bazę danych:**
 Utwórz bazę danych autorent w MySQL.

  Wprowadź dane dostępu do bazy w pliku src/main/resources/application.properties.
  application.properties:
  server.port=8080
  spring.datasource.url= _uzupełnij_
  spring.datasource.username= _uzupełnij_
  spring.datasource.password= _uzupełnij_
  spring.thymeleaf.cache=false
  app.upload.dir=src/main/resources/static/images

**Zainicjalizuj bazę danych:**
 Przy pierwszym uruchomieniu należy wywołać DatabaseInitializer.initializeDatabase();
 Uruchom aplikację, aby automatycznie utworzyć wymagane tabele i wprowadzić dane początkowe.


**Funkcje dodatkowe:**
 - Obsługa szyfrowania haseł za pomocą BCrypt.
 - Wyraźne rozdzielenie odpowiedzialności zgodne z wzorcem MVC.
 - Kompatybilność z Lanterna dla interfejsu konsolowego.

