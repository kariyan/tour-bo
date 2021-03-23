package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroupMenu;
import com.wemakeprice.tour.bo.domain.store.auth.AuthorityGroupMenuStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.AuthorityGroupMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorityGroupMenuMyBatisStore implements AuthorityGroupMenuStore {

    private final AuthorityGroupMenuMapper authorityGroupMenuMapper;

    @Override
    public List<AuthorityGroupMenu> selectByAuthorityGroupId(Long authorityGroupId) {
        return authorityGroupMenuMapper.selectByAuthorityGroupId(authorityGroupId);
    }

    @Override
    public int insertAll(List<AuthorityGroupMenu> authorityGroupMenus) {
        return authorityGroupMenuMapper.insertAll(authorityGroupMenus);
    }

    @Override
    public int deleteAllByAuthorityGroupIds(Collection<Long> authorityGroupIds) {
        return authorityGroupMenuMapper.deleteAllByAuthorityGroupIds(authorityGroupIds);
    }
}
