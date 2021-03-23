package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccLog;

public interface AccLogStore {
    void insertAccLog(AccLog accLog);
}
