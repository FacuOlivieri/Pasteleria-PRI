package com.pasteleriaPri.Pasteleria.config;

import com.pasteleriaPri.Pasteleria.entity.Product;
import com.pasteleriaPri.Pasteleria.entity.ProductType;
import com.pasteleriaPri.Pasteleria.repository.ProductRepository;
import com.pasteleriaPri.Pasteleria.repository.ProductTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * Fills the in-memory database with demo catalog data so the web pages have
 * something to render. Only active under the "dev" profile:
 * {@code mvn -Dspring.profiles.active=dev spring-boot:run}
 *
 * This writes through the repositories rather than ProductService on purpose:
 * Mapper.toProduct() deliberately drops the product type (see Mapper), so
 * ProductService.save() cannot attach a category. A seeder is infrastructure,
 * not a web controller, so the "call the service layer only" rule of
 * THYMELEAF-CONVENTIONS.md does not apply to it.
 */
@Configuration
@Profile("dev")
public class DevDataSeeder {

    @Bean
    public CommandLineRunner seedCatalog(ProductTypeRepository productTypeRepository,
                                         ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                return;
            }

            ProductType cakes = saveType(productTypeRepository, "Tortas");
            ProductType cupcakes = saveType(productTypeRepository, "Cupcakes");
            ProductType macarons = saveType(productTypeRepository, "Macarons");
            ProductType chocolates = saveType(productTypeRepository, "Bombones");

            productRepository.saveAll(List.of(
                    product("Torta Violeta", 18500.0, 6, cakes,
                            "Three layers of vanilla sponge, wild berry cream and a violet glaze mirror finish."),
                    product("Torta Selva Negra", 21000.0, 4, cakes,
                            "Dark chocolate sponge soaked in cherry syrup, chantilly cream and shaved chocolate."),
                    product("Torta Red Velvet", 22500.0, 3, cakes,
                            "The classic red velvet with a cream cheese frosting we whip fresh every morning."),
                    product("Cheesecake de Frutos Rojos", 19800.0, 5, cakes,
                            "Baked cheesecake on a butter biscuit base, crowned with a warm red fruit compote."),

                    product("Cupcake de Vainilla", 2400.0, 40, cupcakes,
                            "Madagascar vanilla sponge with a swirl of Italian meringue buttercream."),
                    product("Cupcake de Chocolate", 2600.0, 36, cupcakes,
                            "Intense cocoa sponge with a molten dulce de leche heart."),
                    product("Cupcake de Limon", 2500.0, 28, cupcakes,
                            "Lemon zest sponge, lemon curd centre and a torched meringue crown."),

                    product("Macaron de Pistacho", 1200.0, 60, macarons,
                            "Crisp shell, tender centre, Sicilian pistachio ganache."),
                    product("Macaron de Frambuesa", 1200.0, 55, macarons,
                            "Raspberry ganache with a touch of rose water."),
                    product("Macaron de Chocolate Blanco", 1250.0, 48, macarons,
                            "White chocolate and Tahitian vanilla, finished with edible gold dust."),

                    product("Bombon de Dulce de Leche", 950.0, 80, chocolates,
                            "Dark chocolate shell filled with slow-cooked dulce de leche."),
                    product("Bombon de Maracuya", 1050.0, 65, chocolates,
                            "Passion fruit ganache balanced by a 70% cocoa shell.")
            ));
        };
    }

    private ProductType saveType(ProductTypeRepository repository, String name) {
        ProductType productType = new ProductType();
        productType.setProductTypeName(name);
        return repository.save(productType);
    }

    private Product product(String name, Double price, Integer quantity, ProductType type, String description) {
        return Product.builder()
                .prodName(name)
                .prodPrice(price)
                .prodQuantity(quantity)
                .prodDescription(description)
                .productType(type)
                .build();
    }
}