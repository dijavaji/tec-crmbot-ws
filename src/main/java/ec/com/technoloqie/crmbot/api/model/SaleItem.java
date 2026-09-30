package ec.com.technoloqie.crmbot.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entity representing an item within a sale.
 */
@Entity
@Table(name = "sale_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id", nullable = false)
	private Sale sale;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "total_amount", precision = 19, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "ocr_confidence")
	private Double ocrConfidence;

	@Column(name = "manually_edited")
	@Builder.Default
	private Boolean manuallyEdited = false;
}
