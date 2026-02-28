package service.custom.impl;

import model.Supplier;
import repository.RepositoryFactory;
import repository.custom.SupplierRepository;
import service.custom.SupplierService;
import util.RepositoryType;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {

    SupplierRepository suppRepo = RepositoryFactory.getInstance().getRepositoryType(RepositoryType.SUPPLIER);

    @Override
    public boolean saveSupplier(Supplier supplier) throws Exception {
        return suppRepo.save(supplier);
    }

    @Override
    public boolean updateSupplier(Supplier supplier) throws Exception {
        return suppRepo.update(supplier);
    }

    @Override
    public boolean deleteSupplier(int supplierId) throws Exception {
        return suppRepo.delete(supplierId);
    }

    @Override
    public Supplier searchSupplier(int supplierId) throws Exception {
        return suppRepo.search(supplierId);
    }

    @Override
    public List<Supplier> getAllSuppliers() throws Exception {
        return suppRepo.getAll();
    }

    @Override
    public List<Supplier> searchSuppliers(String searchText) throws Exception {
        return suppRepo.searchByNameOrContact(searchText);
    }
}
