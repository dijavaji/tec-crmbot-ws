package ec.com.technoloqie.crmbot.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing metadata for a notebook image.
 */
@Entity
@Table(name = "notebook_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotebookImage {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "storage_url", length = 512)
	private String storageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "processing_status")
	@Builder.Default
	private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

	@Column(name = "width_px")
	private Integer widthPx;

	@Column(name = "height_px")
	private Integer heightPx;

	@Column(name = "uploaded_at", updatable = false)
	private LocalDateTime uploadedAt;

	@PrePersist
	protected void onUpload() {
		uploadedAt = LocalDateTime.now();
	}
}
