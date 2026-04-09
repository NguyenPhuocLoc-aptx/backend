package com.zosh.service;

import java.util.List;

import com.zosh.exception.ChatException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Message;

public interface MessageService {

    Message sendMessage(String senderId, String projectId, String content)  // ✅ Long → String
            throws UserException, ChatException, ProjectException;

    List<Message> getMessagesByProjectId(String projectId)  // ✅ Long → String
            throws ProjectException, ChatException;
}