package repository;

import repository.custom.impl.*;
import util.RepositoryType;

public class RepositoryFactory {
    private static RepositoryFactory instance;
    private RepositoryFactory(){}

    public static RepositoryFactory getInstance() {
        return instance ==  null ? instance = new RepositoryFactory() : instance;
    }

    public <T extends SuperRepository> T getRepositoryType(RepositoryType repositoryType){
        switch (repositoryType) {
            case ORDER: return (T) new OrderRepositoryImpl();
            case PRODUCT: return (T) new ProductRepositoryImpl();
            case EMPLOYEE: return (T) new EmployeeRepositoryImpl();
            case SUPPLIER: return (T) new SupplierRepositoryImpl();
        }
        return null;
    }
}
