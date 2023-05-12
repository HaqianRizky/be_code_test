package com.accenture.backend.codetest.controller;

import com.accenture.backend.codetest.model.request.UserModRequest;
import com.accenture.backend.codetest.model.response.UserCollectionResponse;
import com.accenture.backend.codetest.model.response.UserModResponse;
import com.accenture.backend.codetest.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UserCollectionResponse> all(@RequestParam(defaultValue = "5") int max_records, @RequestParam(defaultValue = "0") int offset) {
        try {
            return new ResponseEntity<>(userService.getAll(max_records, offset), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserModResponse> getDataById(@PathVariable("id") Integer id) throws ResponseStatusException {
        try {
            UserModResponse user = userService.getUserById(id);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find resource with id " + id);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<UserModResponse> CreateUser(@Valid @RequestBody UserModRequest request)
            throws ResponseStatusException {
        try {

            var ssn = request.getSsn();

            if (ssn == null || ssn.length() != 16) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for field ssn, rejected value: null" + ssn);
            }
            if (request.getFirstName() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for first_name, rejected value: null" + request.getSsn());
            }
            if (request.getLastName() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for field family_name, rejected value:" + request.getSsn());
            }

            UserModResponse user = userService.createUser(request);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserModResponse> UpdateUser(@RequestBody UserModRequest request, @PathVariable("id") Integer id) {
        try {
            UserModResponse user = userService.putUser(request, id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/users/{id}/setting")
    public ResponseEntity<UserModResponse> UpdateUser(@PathVariable("id") Integer id, @RequestBody List<Map<String, String>> data) {
        try {
            UserModResponse user = userService.putUserSetting(data, id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        try {
            if (!userService.deleteUser(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/users/{id}/refresh")
    public ResponseEntity<UserModResponse> UpdateUserRefresh(@PathVariable("id") Integer id) {
        try {
            UserModResponse user = userService.refreshUser(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
