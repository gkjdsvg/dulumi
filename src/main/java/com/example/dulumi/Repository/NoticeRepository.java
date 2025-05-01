package com.example.dulumi.Repository;

import com.example.dulumi.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository <Notice, Long> {
}
