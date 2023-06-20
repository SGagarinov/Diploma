package com.netology.diploma.repository;

import com.netology.diploma.dto.auth.AuthRequest;
import com.netology.diploma.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Repository
public class UserRepository {

    private EntityManager manager;
    private BCryptPasswordEncoder encoder;

    public UserRepository(EntityManager manager, BCryptPasswordEncoder encoder) {
        this.manager = manager;
        this.encoder = encoder;
    }

    public Boolean getByLogin(AuthRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
                .where(builder.and(builder.equal(root.get("login"), request.getLogin())));
//                                builder.isTrue(root.get("is_active"))));
        List<User> result = manager.createQuery(query).getResultList();
        if (result.isEmpty())
            return false;
        return encoder.matches(request.getPassword(), result.get(0).getPassword());
    }
}
