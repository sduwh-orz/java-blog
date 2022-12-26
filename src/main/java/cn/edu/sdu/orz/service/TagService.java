package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.Tag;

public interface TagService {
    public Tag getTag(Integer id);
    public Boolean createTag(String name, String description);
//    public Boolean deleteTag(String name);
}
