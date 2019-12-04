package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "PURCHASES")
public class Purchase /*tem uma Fatura.class */ {
    @Id
    private int id;

    private String name;

    @Version
    private int version;
    /*
    list payment
    list products
    compradoA
    preco
    descontos/vouchers
     */
}
