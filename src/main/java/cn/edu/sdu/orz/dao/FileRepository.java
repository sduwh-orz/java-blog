package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FileRepository extends JpaRepository<File, Integer> {
    @Transactional
    @Modifying
    @Query("update File f set f.name = ?1 where f.id = ?2")
    int updateNameById(String name, Integer id);
    @Transactional
    @Modifying
    @Query("update File f set f.md5 = ?1 where f.id = ?2")
    int updateMd5ById(String md5, Integer id);
    @Query("select (count(f) > 0) from File f where f.md5 = ?1")
    boolean existsByMd5(String md5);
}
