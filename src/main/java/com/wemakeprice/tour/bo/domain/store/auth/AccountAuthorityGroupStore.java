package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccountAuthorityGroup;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Validated
public interface AccountAuthorityGroupStore {

    int insert(@NotNull AccountAuthorityGroup accountAuthorityGroup);

    Optional<AccountAuthorityGroup> selectByAccountId(@NotBlank String accountId);

    List<AccountAuthorityGroup> selectAllByAccountIds(@NotEmpty Collection<String> accountIds);

    int update(@Valid AccountAuthorityGroup accountAuthorityGroup);

    int delete(@NotBlank String accountId);
}
