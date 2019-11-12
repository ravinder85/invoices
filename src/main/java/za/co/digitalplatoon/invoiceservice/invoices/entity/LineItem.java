
package za.co.digitalplatoon.invoiceservice.invoices.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class LineItem {
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;
     
	 @NotNull
	 private Long quantity;
	 @NotNull
     private String description;
     @NotNull
	 private BigDecimal unitPrice;
     
     
     /**
      * Default constructor to initialize a new line item
      */        
      public LineItem(){
       /*DO NOTHING*/  
       super();
      }
     /**
 	 * @param id
 	 * @param quantity
 	 * @param description
 	 * @param unitPrice
 	 */
     public LineItem(Long id, Long quantity, String description, BigDecimal unitPrice) {
 		super();
    	this.id = id;
 		this.quantity = quantity;
 		this.description = description;
 		this.unitPrice = unitPrice;
 	}
      
	/**
	 * @return the id
	 */
 	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
		if (quantity >= 0) {
			this.quantity = quantity;
		} else{
			this.quantity = (long) 0.0;
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	
	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;	
	}
	
	/**
	 * calculates and returns item total
	 * 
	 * @return line item total
	 */
	public BigDecimal getLineItemTotal() {
		return (getUnitPrice().multiply(new BigDecimal(getQuantity()))).setScale(2, RoundingMode.HALF_UP);
	}
    
}