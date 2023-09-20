package com.example.newver3.Daointerface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newver3.Database.BaiBao;

import java.util.List;

@Dao
public interface BaiBaoDAO {

    @Insert
    void insertBaiBao(BaiBao baiBao);
    @Update
    void updateBaiBao(BaiBao... baiBao);
    @Delete
    void deleteBaiBao(BaiBao baiBao);
    @Query("SELECT * FROM baiBaos")
    List<BaiBao> getAllBaiBao();
    @Query("Select * from baiBaos where id =:id")
    BaiBao getBaiBaoById(long id);
    @Query("Select * from baiBaos where downLoadnguon=:downLoadnguon")
    List<BaiBao> getBaiBaoByDownload(String downLoadnguon);

}
