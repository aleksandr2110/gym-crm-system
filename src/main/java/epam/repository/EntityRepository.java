package epam.repository;

public interface EntityRepository<T, ID> {

    T save(T entity);
    T select(ID id);
    default T update(T entity) {
        throw new UnsupportedOperationException("Delete operation is not supported");
    }
    default void delete(ID id) {
        throw new UnsupportedOperationException("Delete operation is not supported");
    }
}
