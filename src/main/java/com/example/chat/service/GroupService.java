/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.example.chat.service;

import com.example.chat.domain.ChatGroup;
import com.example.chat.domain.User;
import com.example.chat.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public List<ChatGroup> findAllGroups() {
        return this.groupRepository.findAll();
    }

    public ChatGroup findById(String id) {
        return this.groupRepository.findById(id);
    }

    public ChatGroup createGroup(String name) {
        String newId = UUID.randomUUID().toString();
        ChatGroup newGroup = ChatGroup.builder()
                .id(newId)
                .name(name)
                .build();
        this.groupRepository.save(newGroup);
        return newGroup;
    }

    public void enterGroup(ChatGroup group, User sender) {
        group.getUsers().add(sender);
        this.groupRepository.save(group);
    }

    public void exitGroup(ChatGroup group, User sender) {
        group.getUsers().remove(sender);
        if (group.hasNoUser()) {
            this.groupRepository.removeById(group.getId());
        }
    }

    public ChatGroup deleteGroupById(String groupId) {
        ChatGroup group = this.groupRepository.findById(groupId);
        if (null == group) {
            return null;
        }

        this.groupRepository.removeById(groupId);
        return group;
    }
}
