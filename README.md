# Pasteleria-PRI

Proyecto e-commerce de pasteleria. Spring Boot 4.1 / Java 21, Thymeleaf on the front,
a REST API alongside it. See `CLAUDE.md` for architecture and `THYMELEAF-CONVENTIONS.md`
for the web layer rules.

## Running the app

Use the Maven wrapper. On Windows in Git Bash, run `./mvnw` (the shell script) —
`mvnw.cmd` needs PowerShell on your PATH and will fail without it.

### Profiles

| Profile | Database | Data survives a restart? | Use it for |
|---|---|---|---|
| `dev` | H2, in memory | No | Quick UI work, nothing to install |
| `local` | MariaDB via XAMPP | **Yes** | Actually testing persistence, inspecting rows |
| `test` | H2, in memory | No | Automated tests only |

```bash
./mvnw -Dspring.profiles.active=dev   spring-boot:run   # throwaway H2
./mvnw -Dspring.profiles.active=local spring-boot:run   # persistent MariaDB
```

With no profile set there is no datasource configured at all, so pick one.

### Running against the local database (XAMPP + phpMyAdmin)

1. Open the **XAMPP Control Panel** and press **Start** next to **MySQL**.
2. Confirm phpMyAdmin answers at <http://localhost/phpmyadmin>.
3. Start the app: `./mvnw -Dspring.profiles.active=local spring-boot:run`
4. Refresh phpMyAdmin. A `pasteleria_db` schema appears with the tables in it —
   you do not need to create the database by hand, the JDBC URL does it
   (`createDatabaseIfNotExist=true`).

Connection details, should you want them in a GUI client: host `localhost`,
port `3306`, user `root`, **no password** (XAMPP defaults). All of it lives in
`src/main/resources/application-local.properties`, commented.

> XAMPP ships **MariaDB**, not Oracle MySQL, even though the control panel says
> "MySQL". We connect with the `mysql-connector-j` driver anyway, which handles
> everything this project does.
>
> On boot you will see Hibernate log `Database dialect: MySQLDialect` and
> `Database version: 5.5.5`. That is expected, not a misconfiguration: MariaDB
> announces itself as `5.5.5-10.4.32-MariaDB` (a legacy prefix for old
> replication clients) and the driver reads only the leading part. The generated
> schema is still InnoDB with all foreign keys and utf8mb4 — verified — so it
> costs us nothing here. `application-local.properties` explains the escape hatch.

The `local` profile uses `ddl-auto=update`, so Hibernate adds new tables and
columns but never drops your rows. To start from scratch, drop `pasteleria_db`
in phpMyAdmin and restart the app.

> **Trap worth knowing.** `update` only ever *adds* missing tables and columns. It
> will **not** alter an existing column or retrofit a constraint. Add
> `@Column(unique = true, nullable = false)` to a field whose table already exists
> and the app boots perfectly while the constraint silently never appears — this
> happened while building the login feature. After changing a constraint, check it
> landed:
>
> ```sql
> DESCRIBE pasteleria_db.clients;   -- Null / Key columns
> SHOW INDEX FROM pasteleria_db.clients;
> ```
>
> If it did not, drop the table (or the whole schema) and let Hibernate rebuild it.

On first boot against an empty catalog, `DevDataSeeder` inserts ~12 demo products
across 4 categories so the pages have something to show. It only runs when the
product table is empty.

## Tests

```bash
./mvnw clean test
```

**Always `clean`.** Maven does not delete class files whose source has moved or
been renamed, and Spring then component-scans both the old and new copies and
dies with a misleading "Ambiguous mapping" error.
