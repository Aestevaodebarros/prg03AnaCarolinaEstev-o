package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.infrastructure.exception.PersistenciaException;
import br.com.ifba.infrastructure.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class GenericDaoImpl<Entity extends PersistenceEntity> implements GenericDao<Entity> {

    private final Class<Entity> entityClass;
    private final Supplier<EntityManager> entityManagerProvider;

    public GenericDaoImpl(Class<Entity> entityClass) {
        this(entityClass, JPAUtil::getEntityManager);
    }

    public GenericDaoImpl(Class<Entity> entityClass, Supplier<EntityManager> entityManagerProvider) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass não pode ser nulo");
        this.entityManagerProvider = Objects.requireNonNull(
                entityManagerProvider, "entityManagerProvider não pode ser nulo");
    }

    @Override
    public void save(Entity entity) {
        Objects.requireNonNull(entity, "entity não pode ser nula");
        EntityManager em = entityManagerProvider.get();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Não foi possível salvar " + entityClass.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Entity entity) {
        Objects.requireNonNull(entity, "entity não pode ser nula");
        EntityManager em = entityManagerProvider.get();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Não foi possível atualizar " + entityClass.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Entity entity) {
        Objects.requireNonNull(entity, "entity não pode ser nula");
        EntityManager em = entityManagerProvider.get();
        try {
            em.getTransaction().begin();
            Entity managedEntity = em.merge(entity);
            em.remove(managedEntity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Não foi possível excluir " + entityClass.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Entity findById(Long id) {
        EntityManager em = entityManagerProvider.get();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Entity> findAll() {
        EntityManager em = entityManagerProvider.get();
        try {
            TypedQuery<Entity> query = em.createQuery(
                    "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}