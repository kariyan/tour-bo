package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.DepartmentAuthority;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
public interface DepartmentAuthorityStore {

    int insertAll(@NotEmpty List<String> departmentCds);

    List<DepartmentAuthority> selectAll();

    int deleteAllByDepartmentCds(@NotEmpty List<String> departmentCds);
}
