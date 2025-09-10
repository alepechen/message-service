package org.example.repository;

import org.example.dto.UserData;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final ConcurrentHashMap<String, UserData> users = new ConcurrentHashMap<>();

    public Optional<UserData> getUserData(String msisdn) {
        return Optional.ofNullable(users.get(msisdn));
    }
    public void addUserData (String msisdn, UserData userData) {
        users.put(msisdn, userData);
    }
}
