package com.erasm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.erasm.entity.Skill;
import com.erasm.service.SkillService;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    @Mock private SkillService skillService;
    @InjectMocks private SkillController controller;

    @Test
    void addSkill_returnsCreated() {
        Skill s = new Skill();
        when(skillService.addSkill(any(Skill.class))).thenReturn(s);

        ResponseEntity<Skill> res = controller.addSkill(new Skill());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(s, res.getBody());
    }

    @Test
    void getAllSkills_returnsOk() {
        when(skillService.getAllSkills()).thenReturn(List.of(new Skill()));

        ResponseEntity<List<Skill>> res = controller.getAllSkills();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void updateSkill_returnsOk() {
        Skill s = new Skill();
        when(skillService.updateSkill(eq(1), any(Skill.class))).thenReturn(s);

        ResponseEntity<Skill> res = controller.updateSkill(1, new Skill());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(s, res.getBody());
    }

    @Test
    void deleteSkill_returnsNoContent() {
        ResponseEntity<Void> res = controller.deleteSkill(1);

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
        verify(skillService, times(1)).deleteSkill(1);
    }
}
