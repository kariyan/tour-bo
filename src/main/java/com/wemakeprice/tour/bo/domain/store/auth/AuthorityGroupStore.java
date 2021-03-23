package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface AuthorityGroupStore {

    int insert(@NotNull AuthorityGroup authorityGroup);

    List<AuthorityGroup> selectAll();
}
