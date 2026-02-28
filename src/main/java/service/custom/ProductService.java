package service.custom;

import model.Product;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {
    boolean saveProduct(Product product) throws Exception;
    boolean updateProduct(Product product) throws Exception;
    boolean deleteProduct(int productId) throws Exception;
    Product searchProduct(int productId) throws Exception;
    Product findByProductCode(String code) throws Exception;
    List<Product> getAllProducts() throws Exception;
    boolean updateStock(int productId, int newQty) throws Exception;
    int getLowStockCount(int threshold) throws Exception;
}
