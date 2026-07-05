package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erasm.entity.Skill;
import com.erasm.exception.SkillNotFoundException;
import com.erasm.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock private SkillRepository skillRepository;

    @InjectMocks private SkillService skillService;

    private Skill skill(int id, String name) {
        Skill s = new Skill();
        s.setId(id);
        s.setName(name);
        return s;
    }

    @Test
    void addSkill_success() {
        when(skillRepository.save(any(Skill.class))).thenAnswer(inv -> inv.getArgument(0));

        Skill result = skillService.addSkill(skill(0, "Java"));

        assertEquals("Java", result.getName());
        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    @Test
    void getAllSkills_returnsList() {
        when(skillRepository.findAll()).thenReturn(List.of(skill(1, "Java"), skill(2, "Spring Boot")));

        assertEquals(2, skillService.getAllSkills().size());
        verify(skillRepository, times(1)).findAll();
    }

    @Test
    void getSkillById_success() {
        when(skillRepository.findById(1)).thenReturn(Optional.of(skill(1, "Java")));

        assertEquals("Java", skillService.getSkillById(1).getName());
    }

    @Test
    void getSkillById_notFound_throws() {
        when(skillRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.getSkillById(99));
    }

    @Test
    void updateSkill_success() {
        Skill existing = skill(1, "Old");
        when(skillRepository.findById(1)).thenReturn(Optional.of(existing));
        when(skillRepository.save(any(Skill.class))).thenAnswer(inv -> inv.getArgument(0));

        Skill result = skillService.updateSkill(1, skill(0, "React"));

        assertEquals("React", result.getName());
        verify(skillRepository, times(1)).save(existing);
    }

    @Test
    void updateSkill_notFound_throws() {
        when(skillRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.updateSkill(99, skill(0, "X")));
    }

    @Test
    void deleteSkill_success() {
        Skill existing = skill(1, "Java");
        when(skillRepository.findById(1)).thenReturn(Optional.of(existing));

        skillService.deleteSkill(1);

        verify(skillRepository, times(1)).delete(existing);
    }

    @Test
    void deleteSkill_notFound_throws() {
        when(skillRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.deleteSkill(99));
    }
}