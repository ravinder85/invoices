/**
 * 
 */
package za.co.digitalplatoon.invoiceservice.invoices.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import za.co.digitalplatoon.invoiceservice.invoices.entity.Invoice;
import za.co.digitalplatoon.invoiceservice.invoices.entity.LineItem;
import za.co.digitalplatoon.invoiceservice.invoices.exceptions.InvoiceException;
import za.co.digitalplatoon.invoiceservice.invoices.repository.InvoiceRepo;

/**
 * @author Samukelo Jiyane
 *
 */
@Repository
public class InvoiceRegisterService {
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private InvoiceRepo invoiceRepo;
	private Invoice invoice;
	private List<LineItem> lineItemList, tmpList;
    private boolean isValid = false;
	
    /**
     * Default constructor to initialize a new line item
     */   
	public InvoiceRegisterService() {
     /*DO NOTHING*/  
	 super();
	}
	
	/**
	 * @param invoice
	 */
	public InvoiceRegisterService(Invoice invoice) {
		super();
		this.invoice = invoice;
		
	}

	/**
	 * @return the lineItems
	 */
	public List<LineItem> getInvoiceLineItems(Invoice invoice) {
		return invoice.getLineItemList();
	}

	/**
	 * @param lineItem the lineItem to set
	 */
	public void setInvoiceLineItems(Invoice invoice) {
		invoice.setLineItemList(lineItemList);
	}

	/**
	 * @return the lineItem
	 */
	public void addInvoiceLineItem(List<LineItem> lineItemList, LineItem lineItem) {
		lineItemList.add(lineItem);
	}
	
	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}
	
	
	/**
	 * @param invoice id
	 * @return the invoice by id
	 * @throws IllegalArgumentException
	 */
	public Optional<?> getInvoiceById(long id) {
		Optional<Invoice> invoice = invoiceRepo.findById(id);
	    
		if (id <= 0){
		 throw new IllegalArgumentException("Invalid request - ID cannot be 0 or below 0");	
		}
		
		if (!invoice.isPresent()) {
			throw new InvoiceException("Invoice not found for the given id: " + id);	
		}
		return invoice;
	}
    
	/**
	 * @return find all invoices
	 * @throws user defined InvoiceException
	 */
	public List<?> findAllInvoices() {
		List<Invoice> invoiceList = invoiceRepo.findAll(); 
		
		if (!invoiceList.isEmpty()) {
			return invoiceList;  	
		}else{
			throw new InvoiceException("Invoice registry is empty"); 
		}
	}
	
	public boolean isInvoiceValid(Invoice invoice) {
	   String client = invoice.getClient();
	   Long vatRate = invoice.getVatRate();
	   Date invoiceDate = invoice.getInvoiceDate();
	   BigDecimal vat = invoice.getVat();
	   BigDecimal total = invoice.getTotal();
	   
	   if (!client.isEmpty() && (vatRate != null && vatRate >= 0) && (!invoiceDate.equals(null)) && (vat !=null && !vat.equals("null")) && (total != null && !total.equals("null"))){
		   isValid = true;  
	   }
	   
	   tmpList = new ArrayList<LineItem>();
	   for (LineItem lineItem: invoice.getLineItemList()){
	    	Long qty = lineItem.getQuantity();
	    	String descr = lineItem.getDescription();
	    	BigDecimal unitPr = lineItem.getUnitPrice();
	    	
	    	if (qty != null && descr != null && unitPr !=null) {
	    		isValid = true;	
	    	}
		 } 
		return isValid; 	
	}
	
	/**
	 * @param the invoice to load
	 */
	@Transactional
	public Long loadInvoice(Invoice invoice) {
        		
		tmpList = new ArrayList<LineItem>();
		for (LineItem lineItem: invoice.getLineItemList()){
	    	LineItem tmpItem = new LineItem();
	    	
	    	tmpItem.setId(lineItem.getId());
	    	tmpItem.setQuantity(lineItem.getQuantity());
	    	tmpItem.setDescription(lineItem.getDescription());
	    	tmpItem.setUnitPrice(lineItem.getUnitPrice());
	    	tmpList.add(tmpItem);
	    	
	    	em.persist(tmpItem);
		 }
	     
	    Invoice newInvoice = new Invoice();
	    
	    newInvoice.setId(invoice.getId());
	    newInvoice.setClient(invoice.getClient());
	    newInvoice.setVatRate(invoice.getVatRate());
	    newInvoice.setInvoiceDate(invoice.getInvoiceDate());
	   // newInvoice.setVat(invoice.getVat());
	    //newInvoice.setTotal(invoice.getTotal());
	    newInvoice.setLineItemList(tmpList);

	    em.persist(newInvoice);
	    em.close();
	    
	    if (isInvoiceValid(newInvoice)){
	    	return newInvoice.getId();	
	    } else {
	    	return (long) 0;
	    }
	   
	}
}
