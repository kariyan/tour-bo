package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccLog;
import com.wemakeprice.tour.bo.domain.service.auth.AccLogService;
import com.wemakeprice.tour.bo.domain.store.auth.AccLogStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccLogServiceLogic implements AccLogService {

    private final AccLogStore accLogStore;

    @Override
    public void registerAccLog(AccLog accLog) {
        accLogStore.insertAccLog(accLog);
    }
}
