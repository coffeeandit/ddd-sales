package br.com.coffeeandit.sale.domain;


import br.com.coffeeandit.client.domain.ClientEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sale")
public class SaleEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleEntity that = (SaleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDtMovement() {
        return dtMovement;
    }

    public void setDtMovement(LocalDateTime dtMovement) {
        this.dtMovement = dtMovement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @JoinColumn(name = "cpf_client", referencedColumnName = "cpf")
    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private ClientEntity clientEntity;

    private Integer quantity;
    private LocalDateTime dtMovement;

    public LocalDateTime getInactivationDate() {
        return inactivationDate;
    }

    public void inactivateSale() {
        this.inactivationDate = LocalDateTime.now();
    }
    private LocalDateTime inactivationDate;
    protected void setInactivationDate(final LocalDateTime inactivationDate) {
        this.inactivationDate = inactivationDate;
    }
    @Override
    public String toString() {
        return "SaleEntity{" +
                "id=" + id +
                ", clientEntity=" + clientEntity +
                ", quantity=" + quantity +
                ", dtMovement=" + dtMovement +
                ", inactivationDate=" + inactivationDate +
                ", idProduct=" + idProduct +
                '}';
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    private Integer idProduct;
}
