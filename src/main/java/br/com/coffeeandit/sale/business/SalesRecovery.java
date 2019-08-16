package br.com.coffeeandit.sale.business;

import br.com.coffeeandit.warehouse.domain.WarehouseOutSaleProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class SalesRecovery {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger LOG = LoggerFactory.getLogger(SalesRecovery.class);

    private SalesBusiness salesBusiness;

    public SalesRecovery(final SalesBusiness salesBusiness) {
        this.salesBusiness = salesBusiness;
    }

    @KafkaListener(topics = "${app.topicError}")
    public void onConsume(final String message) throws IOException {
        var warehouseInOutProduct = objectMapper.readValue(message, WarehouseOutSaleProduct.class);
        if (Objects.nonNull(warehouseInOutProduct.getIdSale())) {
            LOG.info("Desfazendo venda.: " + warehouseInOutProduct.getIdSale());
            salesBusiness.inativarVenda(warehouseInOutProduct.getIdSale());
        }
    }
}
