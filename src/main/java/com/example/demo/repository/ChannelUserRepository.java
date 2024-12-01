package com.example.demo.repository;

import com.example.demo.model.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {
    @Query("SELECT cu FROM ChannelUser cu WHERE cu.userId IN :userIds")
    List<ChannelUser> findByUserIds(@Param("userIds") List<Long> userIds);
}
