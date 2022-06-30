package com.alpturkay.airqualityapp.gen.service;

import com.alpturkay.airqualityapp.gen.entity.BaseAdditionalFields;
import com.alpturkay.airqualityapp.gen.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public abstract class GenericEntityService<E extends BaseEntity, D extends JpaRepository<E, Long>> {
    private final D dao;

    public List<E> findAll(){
        return dao.findAll();
    }

    public Optional<E> findById(Long id){
        return dao.findById(id);
    }

    public E findByIdWithControl(Long id){
        Optional<E> optionalEntity = dao.findById(id);

        E entity;
        if (optionalEntity.isPresent()){
            entity = optionalEntity.get();
        } else {
            // Todo: make this ItemNotFound custom exception.
            throw new RuntimeException("Item not found");
        }

        return entity;
    }

    public E save(E entity){

        setAdditionalFields(entity);

        return dao.save(entity);
    }

    public void delete(E e){
        dao.delete(e);
    }

    private void setAdditionalFields(E entity) {
        BaseAdditionalFields baseAdditionalFields = entity.getBaseAdditionalFields();

        if (baseAdditionalFields == null){
            baseAdditionalFields = new BaseAdditionalFields();
            entity.setBaseAdditionalFields(baseAdditionalFields);
        }

        if (entity.getId() == null){
            baseAdditionalFields.setCreatedDate(new Date());
        }

        baseAdditionalFields.setUpdatedDate(new Date());
    }
}
