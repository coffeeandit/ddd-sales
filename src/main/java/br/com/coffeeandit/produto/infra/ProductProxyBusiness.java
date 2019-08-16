package br.com.coffeeandit.produto.infra;

import br.com.coffeeandit.produto.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductProxyBusiness {

    public static final String ID_PRODUCT = "id_product";
    private Logger LOG = LoggerFactory.getLogger(ProductProxyBusiness.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    @Value("${product.url}")
    private String productUrl;
    private Set<Product> products = new HashSet<>();

    @HystrixCommand(fallbackMethod = "recoverFromCache")
    public Optional<Product> findById(Integer id) {

        var client = HttpClient.newHttpClient();
        var request = getHttpRequest(id);
        try {

            var send = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (send.statusCode() == HttpStatus.OK.value()) {

                return putOnCache(Optional.ofNullable(objectMapper.readValue(send.body(), Product.class)));

            }
        } catch (InterruptedException | IOException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private HttpRequest getHttpRequest(Integer id) {
        String urlWithProduct = productUrl.replaceAll(ID_PRODUCT, id.toString());
        return HttpRequest.newBuilder()
                .uri(URI.create(
                        urlWithProduct))
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Optional<Product> recoverFromCache(Integer id) {
        if (Objects.nonNull(id)) {
            LOG.info(String.format("Está pegando produto da cache pelo failback %s", id));

            var productOnCache = products.stream().filter(product -> {
                return id.equals(product.getId());
            }).findFirst();
            LOG.info(String.format("Retornando da cache produto %s", productOnCache));
            return productOnCache;
        }

        return Optional.empty();
    }

    private Optional<Product> putOnCache(final Optional<Product> product) {
        // TODO Usar Cache Spring JSR - 107
        if (product.isPresent()) {

            Product toPutInCache = product.get();
            if (products.contains(toPutInCache)) {
                products.remove(toPutInCache);
            }
            products.add(toPutInCache);
        }
        return product;
    }
}
