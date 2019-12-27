package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RECEIPTS")
public class Receipt {
    //TODO: incomplete

    @Id
    @GeneratedValue
    private int id;

    private String companyName;
    private String companyTIN;//Taxpayer Identification Number=NIF

    public Receipt() {
    }
}
