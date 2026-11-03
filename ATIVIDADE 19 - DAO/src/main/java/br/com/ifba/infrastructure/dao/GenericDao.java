package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.entity.PersistenceEntity;

import java.util.List;

public interface GenericDao<Entity extends PersistenceEntity> {

    void save(Entity entity);

    void update(Entity entity);

    void delete(Entity entity);

    Entity findById(Long id);

    List<Entity> findAll();
}