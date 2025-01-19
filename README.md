# o2_enter_exam - Spring Boot User Management API

Tato aplikace implementuje REST API pro správu uživatelů s využitím Spring Boot, Spring Data JPA a JWT pro zabezpečení. Aplikace obsahuje základní CRUD operace pro práci s uživatelskými daty, autentizaci a autorizaci uživatelů pomocí JWT tokenů.

## Použití H2 in-memory databáze

V původním zadání bylo uvedeno, že uživatelé budou reprezentováni jako **statická data** (např. pomocí seznamu v paměti). Pro účely vývoje a implementace Spring Data JPA jsem však zvolil **H2 in-memory databázi**, která umožňuje dynamické uložení a správu dat během běhu aplikace. Tento přístup poskytuje možnost využití JPA (Java Persistence API) bez nutnosti perzistentní databáze, přičemž data jsou stále uložena v paměti a po restartu aplikace se ztrácí. 


## Technologie

- **Spring Boot**: Framework pro vývoj aplikací.
- **Spring Web**: Pro RESTful API.
- **Spring Data JPA**: Pro práci s databází (v tomto případě in-memory H2).
- **Lombok**: Pro zjednodušení psaní kódu (generování getterů, setterů, konstruktorů atd.).
- **Spring Security**: Ochrana API pomocí JWT autentizace.
- **H2 Database**: In-memory databáze pro ukládání uživatelských dat (k dispozici pouze během běhu aplikace).
- **Spring Boot DevTools**: Usnadňuje vývoj tím, že poskytuje automatické restartování aplikace a další nástroje.

## Jak spustit aplikaci

1. **Stáhněte si repozitář**:
   Pokud máte Git, stáhněte repozitář pomocí příkazu:
   ```bash
   git clone https://github.com/OroszVision/o2_enter_exam.git
   cd o2_enter_exam
2. **Vytvoření .jar souboru: Pomocí Maven sestavte projekt:**:
   ```bash
   mvn clean install
3. **Spuštění aplikace: Po sestavení aplikace spusťte:**:
    ```bash
    java -jar target/o2_enter_exam-0.0.1-SNAPSHOT.jar
**Aplikace bude dostupná na http://localhost:8080.**

**Nebo využijte jedno z několik IDE podporující Spring Boot pro spuštení pro testování**

## H2 Console
**Pro přístup k H2 konzoli, která umožňuje prohlížet data v in-memory databázi, otevřete následující URL ve vašem prohlížeči:**

**http://localhost:8080/h2-console**

**Přihlašovací údaje pro připojení k in-memory databázi:**
- **JDBC URL:** jdbc:h2:mem:testdb
- **Username:** sa
- **Password:** password

  ## Swagger UI
  **Pro interaktivní testování API použijte Swagger UI na následující URL:**
  ***http://localhost:8080/swagger-ui/***

  **Swagger UI poskytuje přehled všech dostupných endpointů API a umožňuje jejich testování.**

  ## Endpointy
  1. **GET /users**
   - **Popis**: Vrací seznam všech uživatelů.
   - **Vyžaduje autentizaci** (JWT token).

  2. **GET /users/{id}**
   - **Popis**: Vrací konkrétního uživatele podle ID.
   - **Vyžaduje autentizaci** (JWT token).

  3. **POST /authenticate**
   - **Popis**: Slouží k autentizaci uživatele pomocí přihlašovacích údajů (username, password).
   - **Vytvoří JWT token** pro přístup k chráněným endpointům.

  4. **POST /register**
   - **Popis**: Slouží k registraci nového uživatele.
   - **Umožňuje uživatelům vytvořit nový účet**.

  5. **POST /logout**
   - **Popis**: Slouží k odhlášení uživatele.

  ## Přihlašovací údaje pro autentizaci (pro testování mockovaných dat)
  - **Username**: JohnDoe
  - **Password**: johndoe  


