package ec.com.technoloqie.crmbot.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a sale transaction.
 */
@Entity
@Table(name = "sale")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "business_id", nullable = false)
	private String businessId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "sale_date", nullable = false)
	private LocalDate saleDate;

	@Column(name = "sale_time")
	private LocalTime saleTime;

	@Column(name = "total_amount", precision = 19, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "item_count")
	private Integer itemCount;

	@Column(name = "source")
	private String source;

	@Column(name = "average_confidence")
	private Double averageConfidence;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<SaleItem> items = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id")
	@Builder.Default
	private List<NotebookImage> images = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
