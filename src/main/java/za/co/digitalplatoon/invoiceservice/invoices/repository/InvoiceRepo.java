/**
 * 
 */
package za.co.digitalplatoon.invoiceservice.invoices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.digitalplatoon.invoiceservice.invoices.entity.Invoice;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long>{

}
