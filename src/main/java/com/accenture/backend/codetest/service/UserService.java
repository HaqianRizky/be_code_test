package com.accenture.backend.codetest.service;


import com.accenture.backend.codetest.entity.User;
import com.accenture.backend.codetest.entity.UserSetting;
import com.accenture.backend.codetest.model.request.UserModRequest;
import com.accenture.backend.codetest.model.response.UserCollectionResponse;
import com.accenture.backend.codetest.model.response.UserDataResponse;
import com.accenture.backend.codetest.model.response.UserModResponse;
import com.accenture.backend.codetest.repository.UserRepository;
import com.accenture.backend.codetest.repository.UserSettingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSettingRepository userSettingRepository;

    public UserCollectionResponse getAll(int maxRecords, int offset) {
        try {
            List<User> users = userRepository.findUserWithLimitAndOffset(maxRecords, offset);
            List<UserDataResponse> userDataRs = new ArrayList<>();
            if (!users.isEmpty()) {
                for (User user : users) {
                    if (user.getIsActive().equals(true)) {
                        userDataRs.add(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .firstName(user.getFirstName())
                                .lastName(user.getFamilyName())
                                .birthDate(user.getBirthDate().toString())
                                .createdTime(user.getCreatedTime().toString())
                                .updatedTime(user.getUpdateTime().toString())
                                .createdBy(user.getCreatedBy())
                                .updatedBy(user.getUpdatedBy())
                                .isActive(user.getIsActive())
                                .build());
                    }
                }
            }
            return UserCollectionResponse.builder()
                    .userData(userDataRs)
                    .maxRecords(maxRecords)
                    .offset(offset)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserModResponse getUserById(Integer id) {
        try {
            Optional<User> userDb = userRepository.findById(id);
            User user = new User();

            if (userDb.isPresent()) {
                user = userDb.get();
            }

            if (user.getId() > 0 && user.getIsActive().equals(true)) {
                List<UserSetting> uSets = userSettingRepository.findByIdUser(id);
                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());

                return UserModResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .firstName(user.getFirstName())
                                .lastName(user.getFamilyName())
                                .birthDate(user.getBirthDate().toString())
                                .createdTime(user.getCreatedTime().toString())
                                .updatedTime(user.getUpdateTime().toString())
                                .createdBy(user.getCreatedBy())
                                .updatedBy(user.getUpdatedBy())
                                .isActive(user.getIsActive())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserModResponse createUser(UserModRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            LocalDateTime datetime = LocalDateTime.now();
            User userNew = User.builder()
                    .ssn(request.getSsn())
                    .firstName(request.getFirstName())
                    .familyName(request.getLastName())
                    .birthDate(request.getBirthDate())
                    .createdBy("SYSTEM")
                    .updatedBy("SYSTEM")
                    .createdTime(datetime)
                    .updateTime(datetime)
                    .isActive(true)
                    .build();

            User user = userRepository.save(userNew);

            Map<String, String> userSet = new HashMap<>();

            String[] settings = {"biometric_login", "push_notification", "sms_notification", "show_onboarding", "widget_order"};
            for (int i = 0; i < settings.length; i++) {
                UserSetting uSetting = new UserSetting();
                uSetting.setUserId(user);
                uSetting.setKey(settings[i]);
                if (i == 4) {
                    uSetting.setValue("1,2,3,4,5");
                    userSet.put(settings[i], "1,2,3,4,5");
                } else {
                    uSetting.setValue("false");
                    userSet.put(settings[i], "false");
                }
                userSettingRepository.save(uSetting);
            }
            UserModResponse response = UserModResponse.builder()
                    .userData(UserDataResponse.builder()
                            .id(user.getId())
                            .ssn(user.getSsn())
                            .firstName(user.getFirstName())
                            .lastName(user.getFamilyName())
                            .birthDate(user.getBirthDate().toString())
                            .createdTime(user.getCreatedTime().toString())
                            .updatedTime(user.getUpdateTime().toString())
                            .createdBy(user.getCreatedBy())
                            .updatedBy(user.getUpdatedBy())
                            .isActive(user.getIsActive())
                            .build())
                    .build();

            List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());
            response.setUserSettings(listAll);

            return response;

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserModResponse putUser(UserModRequest request, Integer id) {
        try {
            Optional<User> userDb = userRepository.findById(id);
            User user = new User();
            if (userDb.isPresent()) {
                user = userDb.get();
            }
            if (user.getIsActive().equals(true)) {
                user.setSsn(request.getSsn());
                user.setFirstName(request.getFirstName());
                user.setFamilyName(request.getLastName());
                user.setBirthDate(request.getBirthDate());
                user.setUpdateTime(LocalDateTime.now());

                userRepository.save(user);

                List<UserSetting> uSets = userSettingRepository.findByIdUser(id);

                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());

                return UserModResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .firstName(user.getFirstName())
                                .lastName(user.getFamilyName())
                                .birthDate(user.getBirthDate().toString())
                                .createdTime(user.getCreatedTime().toString())
                                .updatedTime(user.getUpdateTime().toString())
                                .createdBy(user.getCreatedBy())
                                .updatedBy(user.getUpdatedBy())
                                .isActive(user.getIsActive())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserModResponse putUserSetting(List<Map<String, String>> userSettings, Integer id) {
        try {
            Optional<User> getOneUser = userRepository.findById(id);
            User user = new User();
            if (getOneUser.isPresent()) {
                user = getOneUser.get();
            }

            if (user.getIsActive().equals(true)) {
                user.setUpdateTime(LocalDateTime.now());
                userRepository.save(user);

                List<UserSetting> uSetRes = userSettingRepository.findByIdUser(id);

                for (UserSetting update : uSetRes) {
                    UserSetting getOne = userSettingRepository.findById(update.getId()).get();
                    for (Map<String, String> map : userSettings) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (Objects.equals(getOne.getKey(), entry.getKey())) {
                                getOne.setValue(entry.getValue());
                            }
                            userSettingRepository.save(getOne);
                        }
                    }
                }

                List<UserSetting> uSets = userSettingRepository.findByIdUser(id);
                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());

                return UserModResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .firstName(user.getFirstName())
                                .lastName(user.getFamilyName())
                                .birthDate(user.getBirthDate().toString())
                                .createdTime(user.getCreatedTime().toString())
                                .updatedTime(user.getUpdateTime().toString())
                                .createdBy(user.getCreatedBy())
                                .updatedBy(user.getUpdatedBy())
                                .isActive(user.getIsActive())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean deleteUser(Integer id) {
        try {
            Optional<User> userDb = userRepository.findById(id);
            User user = new User();

            if (userDb.isPresent()) {
                user = userDb.get();
            }

            if (user.getIsActive().equals(true)) {
                user.setIsActive(false);
                user.setDeletedTime(LocalDateTime.now());

                userRepository.save(user);

                return user == null;
            }

            return false;

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public UserModResponse refreshUser(Integer id) {
        try {
            Optional<User> userDb = userRepository.findById(id);
            User user = new User();

            if (userDb.isPresent()) {
                user = userDb.get();
            }

            if (user.getIsActive().equals(false)) {
                user.setUpdateTime(LocalDateTime.now());
                user.setDeletedTime(null);
                user.setIsActive(true);
                userRepository.save(user);

                List<UserSetting> uSets = userSettingRepository.findByIdUser(id);

                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());


                return UserModResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .firstName(user.getFirstName())
                                .lastName(user.getFamilyName())
                                .birthDate(user.getBirthDate().toString())
                                .createdTime(user.getCreatedTime().toString())
                                .updatedTime(user.getUpdateTime().toString())
                                .createdBy(user.getCreatedBy())
                                .updatedBy(user.getUpdatedBy())
                                .isActive(user.getIsActive())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
