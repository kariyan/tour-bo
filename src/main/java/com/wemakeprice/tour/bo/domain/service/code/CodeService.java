package com.wemakeprice.tour.bo.domain.service.code;

import com.wemakeprice.tour.bo.service.logic.code.CodeServiceLogic;

import java.util.List;

public interface CodeService {
    List<CodeServiceLogic.CodeEnumType> findCodeEnums();
}
