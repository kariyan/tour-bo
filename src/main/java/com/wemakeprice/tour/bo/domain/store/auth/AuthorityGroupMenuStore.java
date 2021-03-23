package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroupMenu;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Validated
public interface AuthorityGroupMenuStore {

    List<AuthorityGroupMenu> selectByAuthorityGroupId(@NotNull Long authorityGroupId);

    int insertAll(@NotEmpty List<AuthorityGroupMenu> authorityGroupMenus);

    int deleteAllByAuthorityGroupIds(@NotEmpty Collection<Long> authorityGroupIds);
}
