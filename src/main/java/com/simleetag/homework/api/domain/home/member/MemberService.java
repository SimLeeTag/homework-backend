package com.simleetag.homework.api.domain.home.member;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.simleetag.homework.api.common.exception.HomeJoinException;
import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.dto.MemberModifyRequest;
import com.simleetag.homework.api.domain.home.member.repository.MemberRepository;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserService;
import com.simleetag.homework.api.domain.work.task.TaskDslRepository;
import com.simleetag.homework.api.domain.work.task.TaskStatus;
import com.simleetag.homework.api.domain.work.task.api.TaskRateResponse;
import com.simleetag.homework.api.domain.work.task.api.TaskResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final UserService userService;

    private final MemberRepository memberRepository;

    private final MemberFinder memberFinder;

    private final TaskDslRepository taskDslRepository;

    public Member join(Home home, Long userId) {
        final User user = userService.findById(userId);
        if (user.getMembers().size() >= 3) {
            throw new HomeJoinException("최대 3개의 집에 소속될 수 있습니다.");
        }

        return home.getMembers()
                   .stream()
                   .filter(member -> member.getUser().getId().equals(userId))
                   .findAny()
                   .orElseGet(() -> memberRepository.save(new Member(home, 0, user)));
    }

    public Member modify(Long homeId, Long memberId, MemberModifyRequest request) {
        final Member member = memberFinder.findMemberByIdAndHomeId(memberId, homeId);
        member.modify(request);
        return member;
    }

    public void expireAll(Long homeId) {
        memberFinder.findAllByHomeId(homeId).forEach(Member::expire);
    }

    public Member expire(Long homeId, Long memberId) {
        final Member member = memberFinder.findMemberByIdAndHomeId(memberId, homeId);
        member.expire();
        return member;
    }

    public List<TaskRateResponse> calculateTaskRatesByDueDates(Long memberId, LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        List<TaskRateResponse> list = new ArrayList<>();
        for (int i = 0; i < period.getDays() + 1; i++) {
            var tasks = taskDslRepository.findAllWithTaskGroupByHomeIdAndOwnerAndDueDate(memberId, startDate.plusDays(i));
            double allTasks = tasks.size();
            double doneTasks = tasks.stream().filter(task -> task.getTaskStatus().equals(TaskStatus.COMPLETED)).count();
            double rate = -1;
            if (allTasks != 0) {
                rate = doneTasks / allTasks * 100.0;
            }
            TaskRateResponse response = new TaskRateResponse(startDate.plusDays(i), (int) rate);
            list.add(response);
        }
        return list;
    }

    public List<TaskResponse> findAllTasksByDueDate(Long memberId, LocalDate date) {
        return TaskResponse.from(taskDslRepository.findAllWithTaskGroupByHomeIdAndOwnerAndDueDate(memberId, date));
    }


}
