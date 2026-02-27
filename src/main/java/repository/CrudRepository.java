package repository;

import java.util.List;

public interface CrudRepository<T, ID> extends SuperRepository{
    boolean save(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(ID id) throws Exception;
    T search(ID id) throws Exception;
    List<T> getAll() throws Exception;
}
