/**
 * 
 */
package za.co.digitalplatoon.invoiceservice.invoices.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import reactor.util.annotation.Nullable;



@Entity
public class Invoice {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       private Long id;
      
       @NotNull
       private String client;
       @NotNull
       private Long vatRate;
       @NotNull
       private Date invoiceDate;
      /* @NotNull
       private BigDecimal total;*/
      /* @NotNull
       private BigDecimal vat;*/
      
       /* The list of purchased items.*/
       @ElementCollection
       private List<LineItem> lineItemList;
	   
    /**
    * Default constructor to initialize a new invoice
    */        
    public Invoice(){
     /*DO NOTHING*/  
     super();
    }
    
    /**
 	 * @param id
 	 * @param client
 	 * @param vatRate
 	 * @param invoiceDate
 	 * @param total
 	 * @param vat
 	 * @param lineItemsList
 	 */ 
    public Invoice(Long id, 
    		       String client, 
    		       Long vatRate, 
    		       Date invoiceDate, 
    		       BigDecimal total,
    		       BigDecimal vat,
    		       List<LineItem> lineItemList) {
		super();
    	this.id = id;
		this.client = client;
		this.vatRate = vatRate;
		this.invoiceDate = invoiceDate;
		//this.total = total;
		//this.vat = vat;
		this.lineItemList = new ArrayList<LineItem>();
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
	 * @return the client
	 */
	public String getClient() {
		return client;
	}
	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}
	/**
	 * @return the vatRate
	 */
	public Long getVatRate() {
		return vatRate;
	}
	
	/**
	 * @return the total
	 */
	/*public void setTotal(BigDecimal total) {
		this.total = total;
	}
	*/
	/**
	 * @param vatRate the vat rate to set
	 */
	public void setVatRate(long vatRate) {
	    this.vatRate = vatRate;	
	}
	
	/**
	 * @return the invoice date
	 */
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	
	/**
	 * @return the vat
	 */
/*	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}*/
	/**
	 * set the list of purchased items.
	 * 
	 */
	public void setLineItemList(List<LineItem> lineItemList) {
		this.lineItemList = lineItemList;
	}
	
	/**
	 * @return get the list of purchased items.
	 * 
	 */
	public List<LineItem> getLineItemList() {
		return lineItemList;
	}
	
	/**
	 * 
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	/**
	 * 
	 * @return BigDecimal - gets sub-total of line items purchased
	 */
	public BigDecimal getSubTotal(List<LineItem> lineItemList){
		BigDecimal subItemTot,tmpTot; 
		
		 subItemTot = new BigDecimal(0.0);
		 tmpTot = new BigDecimal(0.0);
		 
		for (LineItem listItem: lineItemList){
			tmpTot  = listItem.getLineItemTotal().add(subItemTot);	 
			subItemTot = tmpTot;
		 }
		 
		 return subItemTot;
	}
	
	/**
	 * 
	 * @return BigDecimal - add vat to subtotal
	 */
	public BigDecimal getVat(){
		BigDecimal convertedVat,conversionFactor;
		
		//HERE WE ASSUME THAT INPUT VAT RATE IS ALWAYS GIVEN AS AN INTEGER, NOT GIVEN AS A PERCENTAGE. HENCE THE CONVERSION
		conversionFactor = new BigDecimal(100);
		convertedVat =  BigDecimal.valueOf(getVatRate()).divide(conversionFactor);
    	return (getSubTotal(lineItemList).multiply(convertedVat)).setScale(2, RoundingMode.HALF_UP);
	}
    
	/**
	 * 
	 * @return BigDecimal - adds total vat to the sub-total
	 */
    public BigDecimal getTotal(){
	  return (getVat().add(getSubTotal(lineItemList))).setScale(2, RoundingMode.HALF_UP);
	}
	
}
