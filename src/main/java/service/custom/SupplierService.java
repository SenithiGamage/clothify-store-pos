package service.custom;

import model.Supplier;
import service.SuperService;

import java.util.List;

public interface SupplierService extends SuperService {
    boolean saveSupplier(Supplier supplier) throws Exception;
    boolean updateSupplier(Supplier supplier) throws Exception;
    boolean deleteSupplier(int supplierId) throws Exception;
    Supplier searchSupplier(int supplierId) throws Exception;
    List<Supplier> getAllSuppliers() throws Exception;
    List<Supplier> searchSuppliers(String searchText) throws Exception;
}
