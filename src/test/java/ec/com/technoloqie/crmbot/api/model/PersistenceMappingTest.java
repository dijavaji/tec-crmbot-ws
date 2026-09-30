package ec.com.technoloqie.crmbot.api.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@org.springframework.test.context.ActiveProfiles("test")
class PersistenceMappingTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void shouldPersistSaleWithItemsAndImages() {
		// Given
		Sale sale = Sale.builder()
				.businessId("business-1")
				.userId("user-1")
				.saleDate(LocalDate.now())
				.totalAmount(new BigDecimal("100.00"))
				.itemCount(2)
				.source("AI_OCR")
				.averageConfidence(0.95)
				.build();

		SaleItem item1 = SaleItem.builder()
				.productName("Product 1")
				.quantity(1)
				.totalAmount(new BigDecimal("40.00"))
				.ocrConfidence(0.98)
				.sale(sale)
				.build();

		SaleItem item2 = SaleItem.builder()
				.productName("Product 2")
				.quantity(2)
				.totalAmount(new BigDecimal("60.00"))
				.ocrConfidence(0.92)
				.sale(sale)
				.build();

		sale.getItems().add(item1);
		sale.getItems().add(item2);

		NotebookImage image = NotebookImage.builder()
				.storageUrl("http://storage.com/image.jpg")
				.processingStatus(ProcessingStatus.PENDING)
				.widthPx(1920)
				.heightPx(1080)
				.build();

		sale.getImages().add(image);

		// When
		UUID saleId = entityManager.persistAndFlush(sale).getId();
		entityManager.clear();

		// Then
		Sale persistedSale = entityManager.find(Sale.class, saleId);
		assertThat(persistedSale).isNotNull();
		assertThat(persistedSale.getBusinessId()).isEqualTo("business-1");
		assertThat(persistedSale.getItems()).hasSize(2);
		assertThat(persistedSale.getImages()).hasSize(1);
		assertThat(persistedSale.getCreatedAt()).isNotNull();
		assertThat(persistedSale.getImages().get(0).getUploadedAt()).isNotNull();

		// Verify Cascade Delete
		entityManager.remove(persistedSale);
		entityManager.flush();
		
		assertThat(entityManager.find(SaleItem.class, item1.getId())).isNull();
		assertThat(entityManager.find(SaleItem.class, item2.getId())).isNull();
		// NotebookImage is also cascaded because of @OneToMany on Sale
		assertThat(entityManager.find(NotebookImage.class, image.getId())).isNull();
	}
}
