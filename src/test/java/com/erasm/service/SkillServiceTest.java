package com.erasm.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.erasm.entity.Skill;
import com.erasm.repository.SkillRepository;
@ExtendWith(MockitoExtension.class)
class SkillServiceTest {
    @Mock
    private SkillRepository skillRepository;
    @InjectMocks
    private SkillService skillService;
    @Test
    void addSkill_success() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("Java");
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        Skill result = skillService.addSkill(skill);
        assertEquals(1, result.getId());
        assertEquals("Java", result.getName());
    }
    @Test
    void getAllSkills_returnsList() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("Java");
        when(skillRepository.findAll()).thenReturn(List.of(skill));
        List<Skill> result = skillService.getAllSkills();
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
    }
}