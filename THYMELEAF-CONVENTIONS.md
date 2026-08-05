# Thymeleaf Frontend Conventions

Scope: this document governs the **server-rendered web layer** (Thymeleaf templates + `@Controller` classes + plain CSS/JS) for Pasteleria-PRI. It is a sibling to the existing `@RestController` API, not a replacement for it — both layers stay, and both call the same `service` layer.

Stack decisions already made for this project (don't relitigate without asking):
- **No CSS framework/preprocessor.** Plain hand-written CSS. No Bootstrap, no Sass, no build step, no npm.
- **No client-side framework.** Vanilla JS only, and only where a template genuinely needs interactivity (e.g. cart quantity, form validation feedback).
- **Server-rendered pages**, not a fetch/AJAX SPA. New `@Controller` classes fetch data through the service layer and pass it to Thymeleaf via `Model`. The existing `@RestController`s stay as-is for any future API/JS consumers, but web pages should not route through them.

## Directory Distribution

```
src/main/resources/templates/          # .html Thymeleaf views
  fragments/                           # shared fragments: layout, nav, footer, head
  client/
  product/
  productBox/
  order/
  cart/
src/main/resources/static/
  css/                                 # plain CSS, one file per page/section + a shared base.css
  js/                                  # vanilla JS, one file per page that needs it
  images/
```

- One subfolder per domain entity under `templates/`, mirroring the existing `controller`/`service` verticals (`client/`, `product/`, `productBox/`, `order/`).
- Shared markup (head, nav, footer, layout skeleton) lives only in `templates/fragments/`. Never copy-paste a `<nav>` or `<head>` block into a page template.

## Architecture — Web Controllers

- Class name: `XxxWebController` (e.g. `ClientWebController`), one per domain entity, in the existing `controller/` package alongside the REST controllers.
- Annotated `@Controller` (not `@RestController` — this is the whole point, it must return view names, not serialized bodies).
- Base route: `@RequestMapping("/pasteleria/<resource>")` — **no `/api` prefix**. That prefix is reserved for the JSON API (`/api/pasteleria/<resource>`), and reusing it would collide with the `@RestController` mappings on the same path.
- Inject the **service interface** (`IClientService`, `IProductService`, …), not the concrete class. This is the one place this doc deviates from what you'll see in the existing `@RestController`s (some inject the concrete `ClientService`/`ProductService` directly) — that's an existing inconsistency in the API layer, not a pattern to copy. The project's own `CLAUDE.md` states services are split into interface + impl specifically so callers depend on the interface; web controllers should follow that correctly.
- Methods return `String` view names, take `Model model` as a parameter, and never return `ResponseEntity`.
- Method naming convention:
  - `list(Model model)` → `GET /pasteleria/client` → view `client/list`
  - `detail(@PathVariable Long id, Model model)` → `GET /pasteleria/client/{id}` → view `client/detail`
  - `createForm(Model model)` → `GET /pasteleria/client/new` → view `client/form`
  - `create(@ModelAttribute ClientDTO clientDTO)` → `POST /pasteleria/client` → redirect (see below)
  - `editForm(@PathVariable Long id, Model model)` → `GET /pasteleria/client/{id}/edit` → view `client/form`
  - `update(@PathVariable Long id, @ModelAttribute ClientDTO clientDTO)` → `POST /pasteleria/client/{id}` → redirect
  - `delete(@PathVariable Long id)` → `POST /pasteleria/client/{id}/delete` → redirect
- View name format: `"<entity-folder>/<view-file>"`, no leading slash, no `.html` suffix (Thymeleaf's `ViewResolver` adds it).
- After any state-changing action (`create`, `update`, `delete`) **redirect**, don't return a view directly — standard Post/Redirect/Get to avoid duplicate form resubmission on refresh. Use `"redirect:/pasteleria/client"`.

## Data Flow Rules

- Controllers pass **DTOs only** into `Model` — same rule as the API layer (`dto/*DTO`, never a JPA entity crossing into a template). Templates should never see a `Client`/`Product` entity.
- `Model` attribute naming: singular for one object (`client`), plural for a list (`clients`). Keep this consistent across every controller — templates will rely on it.
- No business logic, computation, or formatting decisions inside templates beyond trivial display logic (`th:if` on a null check, `th:each` over a list). Anything more (price formatting, totals, filtering) belongs in the service or the controller, not in `th:` expressions.

## Fragments & Layout Reuse

Define shared fragments once in `templates/fragments/layout.html` and include them with `th:replace`/`th:insert`. Example shape:

```html
<!-- templates/fragments/layout.html -->
<div th:fragment="nav">...</div>
<div th:fragment="footer">...</div>
```

```html
<!-- templates/client/list.html -->
<div th:replace="~{fragments/layout :: nav}"></div>
...
<div th:replace="~{fragments/layout :: footer}"></div>
```

Never duplicate the `<head>`, nav bar, or footer markup inside an individual page template.

## Static Resources

- Reference via `th:href="@{/css/client.css}"` and `th:src="@{/js/client.js}"` — always use `@{...}` link expressions, never hardcoded relative paths, so they survive a context-path change.
- One CSS file per page/section plus a shared `base.css` for resets, typography, and layout primitives common to every page. Don't grow a single monolithic stylesheet.
- JS files are vanilla, no bundler. Keep each file scoped to the page it's loaded on; don't rely on global state shared across pages.

## Forms

- Bind with `th:object="${clientDTO}"` and `th:field="*{name}"` against the DTO, matching the `@ModelAttribute` parameter type in the controller.
- `helpers/Validator.java` is currently a stub (returns `null` for everything) — **do not assume server-side validation is happening**. Until it's wired up, forms rely only on basic HTML5 constraints (`required`, `type="email"`, etc.) plus whatever the entity/DTO already enforces. Don't write template logic that assumes a `BindingResult` with real validation errors will be populated — it won't be, yet.

## Error Handling

There is currently **no `@ControllerAdvice`/`@ExceptionHandler` anywhere in the project** — not even for the REST API. `NotFoundException` (thrown from services via `.orElseThrow(...)`) will propagate unhandled today.

For web controllers specifically, this matters more than it does for the JSON API, because an unhandled exception on a `@Controller` renders Spring Boot's default whitelabel error page instead of a page-shaped error view. Two things to keep in mind when a `WebController` method can throw `NotFoundException`:
- It extends `NullPointerException`, not `RuntimeException` — a generic `catch (RuntimeException e)` or an `@ExceptionHandler(RuntimeException.class)` will **not** catch it. Handle it explicitly.
- Until a real `@ControllerAdvice` exists for the web layer, don't silently swallow it in each controller — flag it and ask before improvising per-controller try/catch blocks, since this is a gap that likely deserves one shared solution rather than N ad-hoc ones.

## Restrictions

- Web controllers call the **service layer only** — never a `repository` directly.
- Never mix `@Controller` and `@RestController` responsibilities in the same class.
- Don't duplicate logic between a `WebController` and its `RestController` sibling — both must call the same service methods; if a needed service method doesn't exist yet, add it to the service, don't inline the logic in the controller.
- No new frontend dependencies (CSS/JS frameworks, npm, build tooling) without asking first — same as the project-wide dependency restriction in `CLAUDE.md`.

## Workflow for a New Page

Follow the project's existing workflow rule — propose before editing:
1. Summarize which `WebController`, routes, and views you're about to add (5 bullets), and ask before writing code.
2. Confirm the DTO(s) needed already exist; add to `Mapper.java`/service only if a genuine gap exists.
3. Create the template(s) under `templates/<entity>/`, reusing fragments from `templates/fragments/`.
4. Add CSS/JS only if the page needs page-specific styling/interactivity.
5. Run `mvn spring-boot:run` and manually click through the page in a browser before calling it done — Thymeleaf rendering bugs won't show up in `mvn test` unless you specifically write a `MockMvc` view test for it.
6. List what you tested (which routes, forms, redirects) and what you didn't.