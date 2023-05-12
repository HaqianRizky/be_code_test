package com.accenture.backend.codetest.repository;

import com.accenture.backend.codetest.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Integer> {
    @Query("select m from UserSetting m WHERE m.userId.id = ?1 ")
    List<UserSetting> findByIdUser(Integer id);
}
