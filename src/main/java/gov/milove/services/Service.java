package gov.milove.services;

import java.io.Serializable;
import java.util.List;

public interface Service<T, ID extends Serializable> {

    T find(ID id);

    List<T> findAll();

    void save(T t);

    void update(T t);

    void delete(T t);
}
