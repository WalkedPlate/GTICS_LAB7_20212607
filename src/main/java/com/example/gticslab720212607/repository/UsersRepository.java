package com.example.gticslab720212607.repository;

import com.example.gticslab720212607.entity.Resources;
import com.example.gticslab720212607.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

    List<Users> findByAuthorizedResource(Resources resources);

}
