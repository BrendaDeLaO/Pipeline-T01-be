package pe.edu.vallegrande.wines.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.wines.model.SaleDetail;
import reactor.core.publisher.Flux;

public interface SaleDetailRepository extends ReactiveMongoRepository<SaleDetail, String> {

    Flux<SaleDetail> findBySaleId(String saleId);
}
