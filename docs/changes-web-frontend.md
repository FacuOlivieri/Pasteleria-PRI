claude# Web Frontend Layer — What Changed and Why

**Audience:** trainee onboarding onto this codebase.
**Scope:** uncommitted changes currently sitting on top of commit `aee39b6` (see `git status`). Nothing here has been committed yet.

## 1. The big picture

Until now this project only exposed a REST API (`@RestController`, JSON in/out, `/api/pasteleria/...`). This change adds a **second, parallel controller layer** that serves actual HTML pages with Thymeleaf, so a browser can visit the shop directly instead of only talking to the JSON API.

This matters architecturally: both layers now call the **same service layer** (`IProductService`, `ICartService`, etc.). The rule going forward is *controllers never talk to repositories directly* — web controllers reuse the exact same services the REST controllers use. That's why, for example, `ProductWebController` depends on `IProductService`/`IProductTypeService` instead of duplicating query logic.

```
Browser  ──▶ *WebController (Thymeleaf, returns view names)
REST client ──▶ *Controller (@RestController, returns JSON)
                        │
                        ▼
                  Service layer (shared)
                        │
                        ▼
                    Repositories
```

## 2. New web controllers (`controller/*WebController.java`)

All new. Package convention: `@Controller` (not `@RestController`), one `@RequestMapping("/pasteleria/<resource>")` per resource, methods return **view names** (Thymeleaf template paths), not data.

| Controller | Routes | Purpose |
|---|---|---|
| `HomeWebController` | `GET /`, `GET /pasteleria`, `POST /pasteleria/contact` | Landing page + contact form submission |
| `ProductWebController` | `GET /pasteleria/product`, `GET /pasteleria/product/{id}` | Catalog list (with filters) and product detail |
| `CartWebController` | `GET /pasteleria/cart`, `POST /pasteleria/cart/add`, `POST /pasteleria/cart/{id}/remove`, `POST /pasteleria/cart/clear` | Cart page and cart mutations |
| `ClientWebController` | `GET /pasteleria/client/new`, `POST /pasteleria/client` | Account registration form |

`HomeWebController` deliberately has **no** class-level `@RequestMapping` — the home page isn't a "resource" like the others, so each route is declared on its own method.

### GlobalWebModelAdvice — the navbar cart badge

New `@ControllerAdvice` class. It injects `cartItemCount` into the Model for every page render, so the navbar can show "🛒 3" without every controller remembering to add that attribute themselves.

**Important detail:** it's scoped with `assignableTypes = { HomeWebController, ProductWebController, CartWebController, ClientWebController }` — explicitly, not left global. If this were left global, it would also try to inject into the `@RestController`s (the JSON API), which would be wrong and wasteful. **If you add a new `*WebController`, you must add it to this list or its pages won't get the cart badge.**

### A security-relevant detail: open redirect protection

`CartWebController.add()` accepts a `returnUrl` request parameter so "add to cart" can bounce the visitor back to whatever page they were browsing (including active filters). Because that value comes from the browser, it's **never trusted directly** — `safeReturnUrl()` only allows it through if it starts with `/pasteleria` and isn't a protocol-relative URL (`//evil.com`). Anything else silently falls back to `/pasteleria/cart`. Without this check, a crafted link/form could redirect a visitor off-site through your own domain — that's a real class of vulnerability called an **open redirect**.

### A known gap, flagged on purpose: plaintext passwords

`ClientWebController.create()` has a comment warning that `ClientDTO.passwordDTO` is saved exactly as submitted — there is **no `PasswordEncoder`, no Spring Security** anywhere in the project yet. This is fine for a local demo/learning project, but it is explicitly called out as **not production-ready**. If this project ever needs real accounts, hashing has to be added in `ClientService` before that happens.

## 3. Cart: from "flat product list" to real quantities and subtotals

This is the most substantial change. Previously `CartDTO` held `List<ProductDTO> productsDTO` — no quantity, no subtotal, no total. That's not enough to render a real cart page (you need "2× Torta Selva Negra = $42.000").

### New/changed DTOs

- **`CartItemDTO`** (new) — one cart line: `productDTO`, `quantityDTO`, `subtotalDTO`.
- **`CartDTO`** (changed) — `productsDTO` → `itemsDTO` (`List<CartItemDTO>`), plus a new `totalDTO`.

### `CartService` / `ICartService` (new)

This is a **session-scoped** Spring bean (`@SessionScope`), not backed by any table — there's no `CartRepository`. That's intentional: a shopping cart before checkout is per-visitor, in-flight state, not something that needs to survive a server restart. Under the hood, `@SessionScope` makes Spring inject a dynamic proxy into the (singleton) web controllers, and that proxy resolves to the correct instance for whichever HTTP session is making the current request.

Internally it's just `Map<Long productId, Integer quantity>` (a `LinkedHashMap`, so insertion order is preserved — items stay in the order they were added). Key methods:

- `addProduct(productId, quantity)` — resolves the product through `IProductService.findById()` first, so a bad/deleted product id fails immediately at "add to cart" time instead of blowing up later when the cart page tries to render it.
- `getCart()` — builds the `CartDTO` on demand: looks up each product, computes `subtotal = unitPrice * quantity`, sums the total. If a product was deleted while sitting in someone's cart, that line is silently skipped rather than breaking the whole page (see the `NotFoundException` note below).
- `getItemCount()` — sum of quantities, used by `GlobalWebModelAdvice` for the navbar badge.

**Gotcha worth knowing:** `NotFoundException` in this codebase extends `NullPointerException`, not `RuntimeException` (documented in the root `CLAUDE.md`). A plain `catch (RuntimeException e)` around the stale-product lookup would **not** catch it — `CartService.getCart()` catches `NotFoundException` explicitly for this reason.

### `Mapper.java` changes

- `toProductDTO` now also maps `idProductDTO` and the nested `productTypeDTO` (previously the id and category were dropped when going entity → DTO).
- `toProductDTO`/`toProductTypeDTO` → DTO direction only maps ids **one way** (entity → DTO). The comment in the code is explicit about why: if `toProduct(ProductDTO)` carried an id back, `ProductService.save()` would silently turn into an upsert; if it rebuilt a `ProductType` from `toProductTypeDTO`'s reverse, every save could insert a duplicate category row.
- `toCartDTO(Cart)` (the entity-based cart, distinct from the new session-based `CartService`) now builds `CartItemDTO`s — but since the `Cart` entity/POJO has no quantity concept, every line collapses to quantity `1`. The comment flags this as a known limitation of that path; `CartService` is the source of truth for real quantities.

## 4. Catalog filtering by category **and** price together

Before this change, `ProductRepository` had two separate finder methods — by type, or by price range — that couldn't be combined. The new catalog page needs "cupcakes between $2000 and $3000" to work as one query.

- **`ProductRepository.findAllByFilters(typeId, minPrice, maxPrice)`** — a `@Query` with a `LEFT JOIN` on `productType` and `WHERE (:param IS NULL OR ...)` per criterion, so any combination of filters (or none) works from a single query. The join is `LEFT JOIN` specifically so products with **no** category still show up when no category filter is applied — an inner join would have silently hidden them.
- **`IProductService`/`ProductService.findAllByFilters(...)`** — thin pass-through, wired up in `ProductWebController.list()`.

### Why `@Transactional(readOnly = true)` got added to read methods

`Product.productType` is a `LAZY` association, and `Mapper.toProductDTO` now dereferences it (to build the nested `productTypeDTO`). Without an active transaction, that lazy load would throw once you're outside of Spring's "open session in view" safety net — which doesn't exist in a `@Service`-level unit test or a scheduled job, only in a live web request. Every `ProductService` read method got `@Transactional(readOnly = true)` so the lazy load always has a transaction to run in, regardless of caller.

## 5. Contact form email

- **`ContactFormDTO`** (new): `senderNameDTO`, `senderEmailDTO`, `subjectDTO`, `messageDTO`.
- **`EmailService.sendContactMessage(...)`** (new): sends the message **to the shop's own inbox** (`email.username`, injected via `@Value`), with the **visitor's** address set as `Reply-To`. That's the key design point: a contact-form submission isn't "email to the visitor," it's "email about the visitor" — setting Reply-To means clicking "Reply" in Gmail answers the actual visitor, not the shop's own address.
- **`HomeWebController.contact()`**: on failure, catches both `MessagingException` (checked) and `RuntimeException` (Spring's `MailException` is unchecked) so a broken mail server never surfaces Spring's whitelabel error page — the user just sees a friendly error and their form input is preserved via a flash attribute.

## 6. Config changes

- **`application-dev.properties`** (was empty): now fully configures the `dev` profile — H2 in-memory DB, `ddl-auto=create-drop`, H2 console at `/h2-console`, Thymeleaf caching disabled (so template edits show up on refresh without restarting).
- **`application.properties`**: added `server.servlet.session.tracking-modes=cookie`. Reason: since the cart is now session-scoped, *every* visitor gets an HTTP session immediately. Without forcing cookie-only tracking, Tomcat's default fallback rewrites URLs as `...;jsessionid=ABC...`, which would leak session ids into referrer headers, access logs, and any shared link.
- **`DevDataSeeder`** (new, `@Profile("dev")`): a `CommandLineRunner` that seeds ~12 demo products across 4 categories (Tortas, Cupcakes, Macarons, Bombones) so the new pages have something to show. Only runs if the product table is empty, and only under the `dev` profile — it's infrastructure, not something the REST API or the `test` profile depend on. It intentionally writes through the repositories directly instead of `ProductService.save()`, because `Mapper.toProduct()` drops the product type by design (see §3) — the seeder needs the category attached, so it bypasses that mapper on this one path.

## 7. New templates & static assets (not reviewed in depth here)

```
src/main/resources/templates/
  index.html
  fragments/layout.html
  product/list.html, product/detail.html
  cart/detail.html
  client/form.html

src/main/resources/static/
  css/base.css, home.css, product.css, cart.css, client.css
  js/base.js
  images/.gitkeep
```

These are the Thymeleaf views/assets the controllers above render. Not covered line-by-line in this doc — read them alongside `THYMELEAF-CONVENTIONS.md` at the repo root, which governs how logic must stay out of `th:` expressions (that's why `ProductWebController.buildReturnUrl()` and all subtotal/total math live in Java, not in templates).

## 8. What this documentation does **not** cover / what to check yourself

- **No tests were written for any of this** — `CartService`, the new filter query, and all four `*WebController`s currently have zero test coverage. `codegraph_explore` flagged `HomeWebController.home()` specifically as having no covering tests.
- The project's baseline `mvn test` was already red before these changes (see prior known issue — always run `mvn clean test`, not `mvn test`, and expect two pre-existing failures unrelated to this work).
- I did not run the app or click through the pages in a browser as part of writing this document — only static code/diff review. Before trusting this as "working," run `mvn -Dspring.profiles.active=dev spring-boot:run` and click through: home → contact form, catalog with filters, add to cart, remove/clear cart, client registration.
- `helpers/Validator.java` is still a stub (per root `CLAUDE.md`) — none of the new form submissions (`ContactFormDTO`, `ClientDTO`) get server-side validation beyond whatever Thymeleaf/HTML gives you for free.
