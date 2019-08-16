package br.com.coffeeandit.produto.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {


    public Integer getId() {
        return id;
    }

    private Integer id;

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    private String formattedPrice;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private String name;
    private BigDecimal price;

    public SituacaoProduto getSituacaoProduto() {
        return situacaoProduto;
    }

    public void setSituacaoProduto(SituacaoProduto situacaoProduto) {
        this.situacaoProduto = situacaoProduto;
    }

    private SituacaoProduto situacaoProduto;


    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    enum SituacaoProduto {

        ATIVO,
        INATIVO,
        SEM_ESTOQUE;
    }

}
