package csc.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Basic(optional = false)
	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Length(max = 100)
	@Column(name = "contract_number")
	private String contractNumber;

	@Column(name = "name_service", length = 10485760)
	private String nameService;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "index_consumed")
	private Float indexConsumed;

	@Basic(optional = false)
	@Column(name = "total")
	private BigDecimal total;

	@Basic(optional = false)
	@Column(name = "vat")
	private float vat;

	@Column(name = "ptef")
	private BigDecimal ptef;

	@Basic(optional = false)
	@Column(name = "grand_total")
	private BigDecimal grandTotal;

	@JsonIgnore
	@JoinColumn(name = "id_customer", referencedColumnName = "id_customer")
	@ManyToOne(optional = false)
	private Customer idCustomer;

	@JsonIgnoreProperties("invoiceCollection")
	@JoinColumn(name = "id_type", referencedColumnName = "id_type")
	@ManyToOne(optional = false)
	private TypeInvoice idType;

	@Basic(optional = false)
	@Length(max = 100)
	@Column(name = "id_cpn")
	private String idCpn;

	public Invoice() {
	}

	public Invoice(Long id) {
		this.id = id;
	}

	public Invoice(Long id, Date date, BigDecimal total, float vat, BigDecimal grandTotal) {
		this.id = id;
		this.date = date;
		this.total = total;
		this.vat = vat;
		this.grandTotal = grandTotal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getNameService() {
		return nameService;
	}

	public void setNameService(String nameService) {
		this.nameService = nameService;
	}

	public Float getIndexConsumed() {
		return indexConsumed;
	}

	public void setIndexConsumed(Float indexConsumed) {
		this.indexConsumed = indexConsumed;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public float getVat() {
		return vat;
	}

	public void setVat(float vat) {
		this.vat = vat;
	}

	public BigDecimal getPtef() {
		return ptef;
	}

	public void setPtef(BigDecimal ptef) {
		this.ptef = ptef;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Customer getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(Customer idCustomer) {
		this.idCustomer = idCustomer;
	}

	public TypeInvoice getIdType() {
		return idType;
	}

	public void setIdType(TypeInvoice idType) {
		this.idType = idType;
	}

	public String getIdCpn() {
		return idCpn;
	}

	public void setIdCpn(String idCpn) {
		this.idCpn = idCpn;
	}
}
