package repository.custom;

import model.Supplier;
import repository.CrudRepository;

import java.util.List;

public interface SupplierRepository extends CrudRepository<Supplier, Integer> {
    List<Supplier> searchByNameOrContact(String searchText) throws Exception;
}
