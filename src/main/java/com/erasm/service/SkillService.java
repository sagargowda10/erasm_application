package com.erasm.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.exception.SkillNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.entity.Skill;
import com.erasm.repository.SkillRepository;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    public Skill getSkillById(Integer id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + id));
    }

    public Skill updateSkill(Integer id, Skill updated) {
        Skill skill = getSkillById(id);
        skill.setName(updated.getName());
        logger.info("Updated skill id: {}", id);
        return skillRepository.save(skill);
    }

    public void deleteSkill(Integer id) {
        Skill skill = getSkillById(id);
        skillRepository.delete(skill);
        logger.warn("Deleted skill id: {}", id);
    }
}