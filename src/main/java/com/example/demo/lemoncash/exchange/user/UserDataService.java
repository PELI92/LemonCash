package com.example.demo.lemoncash.exchange.user;

import com.example.demo.lemoncash.exceptions.CreateEntityException;
import lombok.Builder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@Builder
public class UserDataService {

    @Resource
    private final UserDAO userDao;


    public Optional<User> getById(Long id) {
        return userDao.getById(id).stream()
                .findFirst();
    }

    public Optional<User> getByAlias(String alias) {
        return userDao.getByAlias(alias).stream()
                .findFirst();
    }

    public Optional<User> getByEmail(String email) {
        return userDao.getByEmail(email).stream().findFirst();
    }

    public Long create(User user) throws CreateEntityException {
        return userDao.save(user);
    }

}
