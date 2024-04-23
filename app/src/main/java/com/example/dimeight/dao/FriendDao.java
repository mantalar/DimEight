package com.example.dimeight.dao;

import com.example.dimeight.model.Friend;

import java.util.List;

public interface FriendDao {
    long insert(Friend f);

    void update(Friend f);

    void delete(int id);

    Friend getAFriendById(int id);

    List<Friend> getAllFriends();
}
