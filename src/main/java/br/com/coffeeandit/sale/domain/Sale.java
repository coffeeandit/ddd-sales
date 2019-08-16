package br.com.coffeeandit.sale.domain;

import br.com.coffeeandit.client.domain.Client;
import br.com.coffeeandit.moeda.Price;
import br.com.coffeeandit.produto.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Sale {


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    private Integer id;

    public Sale(final Client client, final Product product, final Integer quantity) {
        this.client = client;
        this.product = product;
        this.quantity = quantity;
        this.dtMovement = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", inactivationDate=" + inactivationDate +
                ", product=" + product +
                ", quantity=" + quantity +
                ", dtMovement=" + dtMovement +
                ", client=" + client +
                '}';
    }

    public LocalDateTime getDtMovement() {
        return dtMovement;
    }

    public LocalDateTime getInactivationDate() {
        return inactivationDate;
    }

    public void setInactivationDate(LocalDateTime inactivationDate) {
        this.inactivationDate = inactivationDate;
    }

    private LocalDateTime inactivationDate;


    public Client getClient() {
        return client;
    }

    private Product product;
    private Integer quantity;
    private LocalDateTime dtMovement;
    private Client client;


    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(product, sale.product) &&
                Objects.equals(quantity, sale.quantity) &&
                Objects.equals(dtMovement, sale.dtMovement) &&
                Objects.equals(client, sale.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, dtMovement, client);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getTotal() {
        if (Objects.nonNull(getProduct()) && Objects.nonNull(getQuantity())) {
            var price = new Price(getProduct().getPrice().multiply(BigDecimal.valueOf(getQuantity())));
            return price.toString();
        }
        return "";
    }

}
