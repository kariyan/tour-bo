package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.AccLog;
import org.springframework.stereotype.Repository;

@TourBoMapper
@Repository
public interface AccLogMapper {
    void insertAccLog(AccLog accLog);
}
