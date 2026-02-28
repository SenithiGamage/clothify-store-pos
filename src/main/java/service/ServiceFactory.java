package service;

import service.custom.impl.*;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType serviceType) {
        switch (serviceType) {
            case ORDER:
                return (T) new OrderServiceImpl();
            case PRODUCT:
                return (T) new ProductServiceImpl();
            case SUPPLIER:
                return (T) new SupplierServiceImpl();
            case EMPLOYEE:
                return (T) new EmployeeServiceImpl();
        }
        return null;
    }
}
