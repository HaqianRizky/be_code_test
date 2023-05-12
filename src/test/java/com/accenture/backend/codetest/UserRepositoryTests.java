package com.accenture.backend.codetest;

import com.accenture.backend.codetest.entity.User;
import com.accenture.backend.codetest.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest() {
        Date date = new Date();
        LocalDateTime datetime = LocalDateTime.now();
        User user = User.builder()
                .ssn("0000000000002942")
                .firstName("Ned")
                .familyName("Stark")
                .birthDate(date)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .createdTime(datetime)
                .updateTime(datetime)
                .isActive(true)
                .build();

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getUserTest() {
        User user = userRepository.findById(1).get();
        Assertions.assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @Order(3)
    public void getListOfUserTest() {
        List<User> user = userRepository.findAll();
        Assertions.assertThat(user.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateUserTest() {
        User user = userRepository.findById(1).get();
        user.setSsn("16longstringtest");
        User userUpdate = userRepository.save(user);
        Assertions.assertThat(userUpdate.getSsn()).isEqualTo("16longstringtest");
    }

//    @Test
//    @Order(5)
//    @Rollback(value = false)
//    public void updateUserInvalidTest() {
//        User user = userRepository.findById(1).get();
//        user.setSsn("asd");
//
//        try{
//            User userUpdate = userRepository.save(user);
//            Assert.isNull(userUpdate, "should be null");
//        }
//        catch (Exception ex){
//            Assert.isInstanceOf(TransactionSystemException.class, ex);
//        }
//
//    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteUserTest() {

        User user = userRepository.findById(1).get();
        userRepository.delete(user);
        User anotherUser = null;
        Optional<User> optionalUser = userRepository.findById(1);

        if (optionalUser.isPresent()) {
            anotherUser = optionalUser.get();
        }
        Assertions.assertThat(anotherUser).isNull();
    }
}
