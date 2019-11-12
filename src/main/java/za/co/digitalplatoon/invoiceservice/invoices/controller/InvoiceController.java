package za.co.digitalplatoon.invoiceservice.invoices.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import za.co.digitalplatoon.invoiceservice.invoices.entity.Invoice;
import za.co.digitalplatoon.invoiceservice.invoices.service.InvoiceRegisterService;


@RestController
public class InvoiceController {
	
    @Autowired
	private InvoiceRegisterService invoiceService;

    private long id;
	
	@GetMapping("/invoices")
	public ResponseEntity<?> viewAllInvoices() {
		try{
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.findAllInvoices());	
		} catch(Exception e) {
		   return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no values found");	
		}
	}
	
	@GetMapping("/invoices/{id}")
	public Optional<?> viewInvoice(@PathVariable long id){
		return invoiceService.getInvoiceById(id);
	}
  
    @PostMapping("/invoices")
    public ResponseEntity<Long> addInvoice(@RequestBody Invoice invoice){
    	
    	try {
    	  id = invoiceService.loadInvoice(invoice);
    	  URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
  				.buildAndExpand(id).toUri();
    	  return ResponseEntity.created(location).build();
    	} catch(Exception e){
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    	}
    	
    	
	}
}

