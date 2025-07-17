package com.cms.repository;

import com.cms.model.Comment;
import com.cms.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByComplaint(Complaint complaint);
}

