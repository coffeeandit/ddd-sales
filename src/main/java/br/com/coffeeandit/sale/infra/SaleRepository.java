package br.com.coffeeandit.sale.infra;

import br.com.coffeeandit.sale.domain.SaleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SaleRepository extends CrudRepository<SaleEntity, Integer> {
}
