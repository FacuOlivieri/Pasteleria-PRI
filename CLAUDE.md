## Project

Spring Boot 4.1.0 / Java 21 e-commerce backend and frontend with Thymeleaf for a pastry shop (`Pasteleria-PRI`). Base package: `com.pasteleriaPri.Pasteleria`. Maven build.

## Commands

```bash
mvn spring-boot:run                                   # run the app (defaults to no active profile -> H2 in-memory, see below)
mvn test                                               # run all tests
mvn test -Dtest=ClientServiceTest                      # run a single test class
mvn test -Dtest=ClientServiceTest#createClient          # run a single test method
mvn -Dspring.profiles.active=dev spring-boot:run        # run with a specific profile
```

There is no separate lint/format tool configured — rely on `mvn test` (surefire) and compilation for validation.

## Architecture

Classic layered Spring MVC stack, one vertical per domain concept:

```
controller (@RestController) -> service interface + impl (@Service) -> repository (Spring Data JPA) -> entity (@Entity)
```

- **Controllers** (`Restcontroller/`) are thin: bind HTTP to a service call and wrap the result in `ResponseEntity`. All routes are under `/api/pasteleria/<resource>`.
- **Services** are split into an interface (`IXxxService`) and implementation (`XxxService`), injected via `@Autowired` field injection (not constructor injection) — follow this pattern for new services.
- **DTOs vs entities**: controllers and services never leak JPA entities across the API boundary — everything crossing the controller layer is a `dto/*DTO` (Lombok `@Builder`). Conversion between entity and DTO is centralized in one static class, `helpers/Mapper.java` (no MapStruct/ModelMapper) — add new conversions there rather than inlining mapping logic in services.
- **Not-found handling**: `exception/NotFoundException` is thrown from services (`repository.findById(...).orElseThrow(...)`) — note it extends `NullPointerException`, not `RuntimeException`, which is unusual and matters if you're writing `catch`/`@ExceptionHandler` logic around it.
- **`helpers/Validator.java`** is a stub (all methods return `null`) — not wired into any service yet. Don't assume validation is happening anywhere it isn't explicitly called.
- **SOLID Principles**: Maintain use of SOLID principles.

### Entity relationships

- `Client` 1—* `Order`
- `Order` *—1 `Payment` (`@OneToOne`, cascade ALL — payment is created/saved together with its order)
- `Order` 1—* `OrderProductDetail` (cascade ALL, `orphanRemoval = true`) — the order-line snapshot (product, quantity, unit price, subtotal) at time of purchase
- `Order` *—* `ProductBox` (join table `order_product_boxes`)
- `ProductBox` *—* `Product` (join table `product_box_products`)
- `Cart` is a plain POJO (not `@Entity`) — not persisted, used as an in-flight product/box holder.

`OrderService.createOrder` is the main write path worth understanding before touching order logic: it resolves the client and each requested product, builds `OrderProductDetail` lines with computed subtotals, sums the total into a new `Payment`, and cascades the save through `Order`.

### Persistence / profiles

- Three Spring profiles via `application-{dev,prod,test}.properties`; `application.properties` itself only sets `spring.application.name` (no active profile is forced there).
- `test` profile: H2 in-memory (`create-drop`), used by `@SpringBootTest`/`@ActiveProfiles("test")` integration tests.
- MySQL driver (`mysql-connector-j`) is a runtime dependency for non-test/dev use; H2 console dependency is also present.
- Email sending config (`config/EmailConfig.java`) reads `email.username`/`email.password` from `classpath:email.properties` and builds the `JavaMailSender` bean for Gmail SMTP. `EmailService`/`EmailController` are currently incomplete/WIP (email sending logic and endpoints are stubbed out).

### Testing conventions

Two distinct styles are used side by side — match the existing one for the layer you're testing:

- **Service unit tests** (e.g. `ClientServiceTest`): `@ExtendWith(MockitoExtension.class)`, `@Mock` repository, `@InjectMocks` service, `@Captor`/`ArgumentCaptor` to assert on saved entities.
- **Controller integration tests** (e.g. `ClientControllerIntTest`): `@SpringBootTest` + `@AutoConfigureMockMvc` + `@AutoConfigureTestDatabase` + `@ActiveProfiles("test")`, driving real HTTP through `MockMvc` against the H2 test DB.

### WorkFlow
- Before editing, resume the changes in 5 bullet points and ask me
- Asume that im a junior, explain the decisions, not just the code
- Be skeptic in the changes I recommend, if something is weird, tell me
- If you re less than 80% sure, ask me. Don't invent
- When you finish, list what did you test and what not

### Commit Format

After validating tha everything is in order and validated, make a commit to the repo, using the next format:
- "feat: (.....)" when creating a new feature
- "fix: (.....)" when fixing a bug or modifying an existing file
- "test: (.....)" for new Unit Test and Integration Test

## Restrictions
- Don't install dependencies without asking