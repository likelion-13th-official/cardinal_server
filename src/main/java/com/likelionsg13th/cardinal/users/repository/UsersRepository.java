package com.likelionsg13th.cardinal.users.repository;

import com.likelionsg13th.cardinal.users.domain.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
}
