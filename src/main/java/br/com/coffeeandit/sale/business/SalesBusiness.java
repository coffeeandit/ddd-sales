package br.com.coffeeandit.sale.business;

import br.com.coffeeandit.client.domain.Client;
import br.com.coffeeandit.client.domain.ClientEntity;
import br.com.coffeeandit.produto.domain.Product;
import br.com.coffeeandit.sale.domain.Sale;
import br.com.coffeeandit.sale.domain.SaleConverter;
import br.com.coffeeandit.sale.domain.SaleEntity;
import br.com.coffeeandit.sale.infra.ClientRepository;
import br.com.coffeeandit.sale.infra.SaleRepository;
import br.com.coffeeandit.warehouse.domain.WarehouseOutSaleProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class SalesBusiness {

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Value("${app.topic}")
    private String topic;
    @Value("${app.topic}")
    private String topicError;
    private Logger LOG = LoggerFactory.getLogger(SalesBusiness.class);
    private SaleRepository saleRepository;
    private SaleConverter saleConverter;
    private ClientRepository clientRepository;

    public SalesBusiness(final SaleRepository saleRepository, final SaleConverter saleConverter,
                         final ClientRepository clientRepository, final KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.saleRepository = saleRepository;
        this.saleConverter = saleConverter;
        this.clientRepository = clientRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional("chainedTransactionManager")
    public Sale saleProduct(final Client client, final Product product, final Integer quantity) {

        var sale = new Sale(client, product, quantity);
        var clientEntity = saveClient(sale);
        var entity = saleConverter.fromTransport(sale);
        entity.setClientEntity(clientEntity);
        var saleEntity = saleRepository.save(entity);
        sale.setId(saleEntity.getId());
        LOG.info(String.format("Venda sendo efetuada %s", sale.toString()));
        sendMessage(sale);
        return saleConverter.fromDomain(saleEntity);
    }

    public Optional<Sale> findById(final Integer id) {
        final Optional<SaleEntity> saleRepositoryById = saleRepository.findById(id);
        if (saleRepositoryById.isPresent()) {
            return Optional.of(saleConverter.fromDomain(saleRepositoryById.get()));
        }
        return Optional.empty();
    }

    @Transactional("transactionManager")
    public void inativarVenda(final Integer id) {
        final Optional<SaleEntity> saleRepositoryById = saleRepository.findById(id);
        LOG.info("Atualizando para inativa a venda.: " + saleRepositoryById);
        if (saleRepositoryById.isPresent()) {
            var saleEntity = saleRepositoryById.get();
            saleEntity.inactivateSale();
            saleEntity = saleRepository.save(saleEntity);
            LOG.info("Atualizado para inativa venda com a data de inativaçao.: " + saleEntity.getInactivationDate());

        }
    }

    public List<Sale> getSales() {

        var listSales = new ArrayList<Sale>();

        Iterable<SaleEntity> all = saleRepository.findAll();
        all.forEach(saleEntity -> {
            listSales.add(saleConverter.fromDomain(saleEntity));
        });
        return listSales;
    }

    private void sendMessage(final Sale sale) {
        try {

            var warehouseTransport = new WarehouseOutSaleProduct();
            warehouseTransport.setIdProduct(sale.getProduct().getId());
            warehouseTransport.setQuantity(sale.getQuantity() * -1);
            warehouseTransport.setIdSale(sale.getId());
            var payload = objectMapper.writeValueAsString(warehouseTransport);
            var message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.PARTITION_ID, 0)
                    .build();
            LOG.info(String.format("Enviando para o kafka %s", sale.toString()));
            kafkaTemplate.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ClientEntity saveClient(Sale sale) {
        var clientEntity = saleConverter.getClientEntity(sale);
        clientEntity = clientRepository.save(clientEntity);
        return clientEntity;
    }

}
