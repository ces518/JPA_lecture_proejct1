package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-21
 * Time: 21:10
 **/
public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    // select m from Member m where m.name = ?
    List<Member> findByName(String name);
}
