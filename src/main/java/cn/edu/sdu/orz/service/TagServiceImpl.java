package cn.edu.sdu.orz.service;

import cn.edu.sdu.orz.dao.TagRepository;
import cn.edu.sdu.orz.po.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag getTag(Integer id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean createTag(String name, String description) {
        try {
            Tag prevTag = tagRepository.findByName(name);
            if (prevTag != null) {
                if (prevTag.getStatus().equals("deleted")) {
                    tagRepository.updateStatus("normal", prevTag.getId());
                    prevTag.setDescription(description);
                    tagRepository.save(prevTag);
                } else {
                    return false;
                }
            } else {
                Tag tag = new Tag();
                tag.setName(name);
                tag.setDescription(description);
                tag.setStatus("normal");
                tagRepository.save(tag);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    @Override
//    public Boolean deleteTag(String name) {
//        try {
//            Tag tag = tagRepository.findByName(name);
//            if (tag != null) {
//                if (tag.getStatus().equals("deleted")) {
//                    return false;
//                }
//                tagRepository.updateStatus("deleted", tag.getId());
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
