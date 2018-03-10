package com.somerandomapps.yata.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TodoItemDao {
    @Query("SELECT * FROM todo_item order by done asc")
    List<TodoItem> getAll();

    @Query("SELECT * FROM todo_item where done = 0 or ( done = 1 and done_at > :includeDoneAfter ) order by done asc")
    List<TodoItem> getUndoneAndDoneAfter(String includeDoneAfter);

    @Insert
    void insertItemWithName(TodoItem item);

    @Update
    void updateItem(TodoItem item);

    @Query("DELETE FROM todo_item")
    void removeAll();
}
