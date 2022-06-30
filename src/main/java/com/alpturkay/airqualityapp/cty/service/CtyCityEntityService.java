package com.alpturkay.airqualityapp.cty.service;

import com.alpturkay.airqualityapp.cty.dao.CtyCityDao;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.gen.service.GenericEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CtyCityEntityService extends GenericEntityService<CtyCity, CtyCityDao> {
    public CtyCityEntityService(CtyCityDao dao) {
        super(dao);
    }


    public boolean existsByCityName(String cityName) {
        return getDao().existsByCityName(cityName);
    }

    public CtyCity findByCityName(String cityName) {
        return getDao().findByCityName(cityName);
    }
}
