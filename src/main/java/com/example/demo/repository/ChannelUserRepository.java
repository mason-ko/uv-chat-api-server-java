package com.example.demo.repository;

import com.example.demo.model.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {
}
