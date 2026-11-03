package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.infrastructure.exception.PersistenciaException;
import br.com.ifba.infrastructure.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

public class GenericDaoImpl<Entity extends PersistenceEntity> implements GenericDao<Entity> {

    private final Class<Entity> type;

    public GenericDaoImpl(Class<Entity> type) {
        this.type = Objects.requireNonNull(type, "type não pode ser nulo");
    }

    @Override
    public void save(Entity entity) {
        Objects.requireNonNull(entity, "entity não pode ser nula");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Não foi possível salvar " + type.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Entity entity) {
        Objects.requireNonNull(entity, "entity não pode ser nula");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Não foi possível atualizar " + type.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Entity entity) {
        Objects.requireNonNull(entity, "entity não pode ser nula");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Entity managedEntity = em.merge(entity);
            em.remove(managedEntity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Não foi possível excluir " + type.getSimpleName(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Entity findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(type, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Entity> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Entity> query = em.createQuery(
                    "SELECT e FROM " + type.getSimpleName() + " e", type);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}