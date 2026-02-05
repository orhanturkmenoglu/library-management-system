package com.library.module.user.service;

import com.library.module.user.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getCurrentUser();

    List<UserDTO> getAllUsers();
}
