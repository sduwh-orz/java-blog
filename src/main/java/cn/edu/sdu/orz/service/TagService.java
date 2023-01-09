package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.po.Tag;

import java.util.List;

public interface TagService {
    public Tag getTag(Integer id);
    public Tag getTag(String id);
    public Boolean createTag(String name, String description);
//    public Boolean deleteTag(String name);
    public List<Tag> getAllTag();
}
