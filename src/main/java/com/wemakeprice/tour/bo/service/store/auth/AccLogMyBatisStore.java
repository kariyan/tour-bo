package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccLog;
import com.wemakeprice.tour.bo.domain.store.auth.AccLogStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.AccLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccLogMyBatisStore implements AccLogStore {

    private final AccLogMapper accLogMapper;

    @Override
    public void insertAccLog(AccLog accLog) {
        accLogMapper.insertAccLog(accLog);
    }
}
