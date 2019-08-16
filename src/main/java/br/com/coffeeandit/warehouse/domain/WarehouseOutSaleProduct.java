package br.com.coffeeandit.warehouse.domain;

import javax.validation.constraints.NotNull;

public class WarehouseOutSaleProduct {

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @NotNull
    private Integer idProduct;
    @NotNull
    private Integer quantity;

    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    private Integer idSale;

}
