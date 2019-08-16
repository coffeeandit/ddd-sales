package br.com.coffeeandit.sale.api;

import br.com.coffeeandit.produto.infra.ProductProxyBusiness;
import br.com.coffeeandit.sale.business.SalesBusiness;
import br.com.coffeeandit.sale.domain.Sale;
import br.com.coffeeandit.sale.infra.SaleTransport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class SalesAPI {

    private SalesBusiness salesBusiness;
    private ProductProxyBusiness productProxyBusiness;

    public SalesAPI(final SalesBusiness salesBusiness, final ProductProxyBusiness productProxyBusiness) {
        this.salesBusiness = salesBusiness;
        this.productProxyBusiness = productProxyBusiness;
    }

    @RequestMapping(value = "/sales/", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST

    )
    public ResponseEntity<Sale> doSale(@Valid @RequestBody final SaleTransport saleTransport) {

        var product = productProxyBusiness.findById(saleTransport.getIdProduct());
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var sale = salesBusiness.saleProduct(saleTransport.getClient(),
                product.get(), saleTransport.getQuantity());
        return ResponseEntity.ok(sale);

    }

    @RequestMapping(value = "/sales/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET

    )
    public ResponseEntity<Sale> saleById(@RequestHeader(name = "Content-type", defaultValue = MediaType.APPLICATION_JSON_VALUE)
                                                 String contentType, @PathVariable(name = "id") Integer id
    ) {
        final Optional<Sale> optionalSale = salesBusiness.findById(id);
        if (optionalSale.isPresent()) {
            return ResponseEntity.ok(optionalSale.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/sales", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET

    )
    public ResponseEntity<List<Sale>> listSales(@RequestHeader(name = "Content-type", defaultValue = MediaType.APPLICATION_JSON_VALUE)
                                                        String contentType
    ) {
        return ResponseEntity.ok(salesBusiness.getSales());
    }
}
