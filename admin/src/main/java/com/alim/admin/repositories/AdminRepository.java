package com.alim.admin.repositories;

import com.alim.admin.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<UserEntity, String> {
}
