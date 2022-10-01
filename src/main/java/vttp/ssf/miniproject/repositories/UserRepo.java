package vttp.ssf.miniproject.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import vttp.ssf.miniproject.models.User;

@Repository
public class UserRepo {

    @Autowired
    @Qualifier("template")
    private RedisTemplate<String, String> redisTemplate;

    public void saveUserDetail(User u){
        String email = u.getEmail();

        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(email, u.toJson(u).toString());
        System.out.println("printing" + u.toJson(u).toString());
    }

    public Optional<String> getUserDuplicate(String email) {
        if (!redisTemplate.hasKey(email)){
            return Optional.empty();

        } else {

            ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
            String outputResult = valueOp.get(email);
            return Optional.of(outputResult);

        }

    }

    public boolean checkUser(String email) {

        if (redisTemplate.hasKey(email)) {
            return true;

        }
            return false;

    }


        // ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // String newsFeedResult = valueOp.get(id);
        // System.out.println("NewsFeedRepositories - getArticle - newsFeedResult: " + newsFeedResult);
        // return Optional.of(newsFeedResult);

}
