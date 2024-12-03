package com.example.demo.repository;

import com.example.demo.model.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {
    @Query("""
    SELECT cu.channelId
    FROM ChannelUser cu
    WHERE cu.userId IN (:userIds)
    GROUP BY cu.channelId
    HAVING COUNT(cu.userId) = 2
""")
    List<Long> findCommonChannelIds(@Param("userIds") List<Long> userIds);

    @Query("""
    SELECT cu.userId
    FROM ChannelUser cu
    WHERE cu.channelId = :channelId
    AND cu.userId <> :userId
""")
    Long findUserIdInSameChannelWithDifferentIds(@Param("channelId") Long channelId, @Param("userId") Long userId);
}
