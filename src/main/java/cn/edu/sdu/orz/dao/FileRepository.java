package cn.edu.sdu.orz.dao;

import cn.edu.sdu.orz.po.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<File, Integer> {
    @Query("select (count(f) > 0) from File f where f.md5 = ?1")
    boolean existsByMd5(String md5);
}
