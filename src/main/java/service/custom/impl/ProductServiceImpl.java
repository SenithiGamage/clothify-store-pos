package service.custom.impl;

import model.Product;
import repository.RepositoryFactory;
import repository.custom.ProductRepository;
import service.custom.ProductService;
import util.RepositoryType;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    ProductRepository prodRepo = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.PRODUCT);

    @Override
    public boolean saveProduct(Product product) throws Exception {
        return prodRepo.save(product);
    }

    @Override
    public boolean updateProduct(Product product) throws Exception {
        return prodRepo.update(product);
    }

    @Override
    public boolean deleteProduct(int productId) throws Exception {
        return prodRepo.delete(productId);
    }

    @Override
    public Product searchProduct(int productId) throws Exception {
        return prodRepo.search(productId);
    }

    @Override
    public Product findByProductCode(String code) throws Exception {
        return prodRepo.findByProductCode(code);
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        return prodRepo.getAll();
    }

    @Override
    public boolean updateStock(int productId, int newQty) throws Exception {
        return prodRepo.updateQuantity(productId, newQty);
    }

    @Override
    public int getLowStockCount(int threshold) throws Exception {
        return prodRepo.getLowStockCount(threshold);
    }
}
