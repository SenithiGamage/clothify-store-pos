package repository.custom;

import model.Product;
import repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findByProductCode(String productCode) throws Exception;
    boolean updateQuantity(int productId, int newQty) throws Exception;
    int getLowStockCount(int threshold) throws Exception;
}
