package br.com.coffeeandit.sale.domain;

import br.com.coffeeandit.client.domain.ClientConverter;
import br.com.coffeeandit.client.domain.ClientEntity;
import br.com.coffeeandit.produto.infra.ProductProxyBusiness;
import org.springframework.stereotype.Component;

@Component
public class SaleConverter {

    private ClientConverter clientConverter;
    private ProductProxyBusiness productProxyBusiness;

    public SaleConverter(final ClientConverter clientConverter, final ProductProxyBusiness productProxyBusiness) {
        this.clientConverter = clientConverter;
        this.productProxyBusiness = productProxyBusiness;
    }

    public SaleEntity fromTransport(final Sale sale) {

        var saleEntity = new SaleEntity();
        saleEntity.setDtMovement(sale.getDtMovement());
        saleEntity.setId(sale.getId());
        saleEntity.setClientEntity(getClientEntity(sale));
        saleEntity.setQuantity(sale.getQuantity());
        saleEntity.setIdProduct(sale.getProduct().getId());
        saleEntity.setId(sale.getId());
        saleEntity.setInactivationDate(sale.getInactivationDate());
        return saleEntity;

    }

    public ClientEntity getClientEntity(Sale sale) {
        return clientConverter.fromTransport(sale.getClient());
    }

    public Sale fromDomain(final SaleEntity sale) {

        var saleEntity = new Sale(clientConverter.fromDomain(sale.getClientEntity()),
                productProxyBusiness.findById(sale.getIdProduct()).get(),
                sale.getQuantity());
        saleEntity.setInactivationDate(sale.getInactivationDate());
        return saleEntity;

    }
}
