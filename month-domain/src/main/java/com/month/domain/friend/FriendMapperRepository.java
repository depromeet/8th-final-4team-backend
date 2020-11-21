package com.month.domain.friend;

import com.month.domain.friend.repository.FriendMapperRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendMapperRepository extends JpaRepository<FriendMapper, Long>, FriendMapperRepositoryCustom {

}
