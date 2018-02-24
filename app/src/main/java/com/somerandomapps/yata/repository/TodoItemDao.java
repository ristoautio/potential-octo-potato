package com.somerandomapps.yata.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TodoItemDao {
    @Query("SELECT * FROM todo_item")
    List<TodoItem> getAll();

    @Insert
    void insertItemWithName(TodoItem item);
}
