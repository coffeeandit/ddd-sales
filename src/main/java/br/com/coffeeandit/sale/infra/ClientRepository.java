package br.com.coffeeandit.sale.infra;

import br.com.coffeeandit.client.domain.ClientEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {
}
