package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class SbbApplicationTests {
	@Autowired //DI기능으로  QuestionRepository에서 bean에서 관리하여 가져오도록 셋팀
	private QuestionRepository questionRepository;

	@Autowired //DI기능으로  QuestionRepository에서 bean에서 관리하여 가져오도록 셋팀
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void testJpaInsert(){
		Question q1 = new Question();
		q1.setSubject("안녕하세요?");
		q1.setContent("나는 김규태입니다.");
		q1.setCreateDate(LocalDateTime.now());

		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("안녕하세요2?");
		q2.setContent("나는 김규태입니다2.");
		q2.setCreateDate(LocalDateTime.now());

		this.questionRepository.save(q2);
	}

	@Test
	void testJpaSelect(){
		List<Question> all = this.questionRepository.findAll(); //Question테이블의 모든 행을 가져온다
		assertEquals(2, all.size());

		Question q = all.get(0); //List의 0번째 행을 갖고 옴
		assertEquals("안녕하세요?", q.getSubject());
	}

	@Test
	void testJpaFindById() {
		Optional<Question> oq = this.questionRepository.findById(2);
		if (oq.isPresent()){ // oq변수 안에 question객체가 있다면,
			Question q = oq.get(); // oq변수 안에 question 가져옴
			assertEquals("안녕하세요1?", q.getSubject());
		}
	}

	@Test
	void testJpaFindBySubject() {
		Question q = this.questionRepository.findBySubject("안녕하세요?");
		assertEquals(1, q.getId());
	}

	@Test
	void testJpaFindBySubjectAndContent() {
		Question q = this.questionRepository.findBySubjectAndContent("안녕하세요?", "나는 김규태입니다.");
		assertEquals(1, q.getId());
	}

	@Test
	void testJpaFindBySubjectLike() {
		List<Question> qList = this.questionRepository.findBySubjectLike("안녕%");
		Question q = qList.get(0); //첫번째 행을 가져오기
		assertEquals("안녕하세요?", q.getSubject());
	}

	@Test
	void testJpaFindBySubjectOrContent() {
		List<Question> qList = this.questionRepository.findBySubjectOrContent("안녕하세요?", "나는 김규태입니다.");
		Question q = qList.get(0); //첫번째 행을 가져오기
		assertEquals("안녕하세요?", q.getSubject());
		assertEquals("나는 김규태입니다.", q.getContent());
	}

	@Test
	void testJpaFindBySubjectIn() {
		List<String> subjects = new ArrayList<>();
		subjects.add("안녕하세요?");
		subjects.add("안녕하세요1?");
		List<Question> qList = this.questionRepository.findBySubjectIn(subjects);
		assertEquals(7, qList.size());
	}

	@Test
	void testJpaFindByCreateDateBetween() {
		String frd = "20241207000013";
		String tod = "20241208154713";
		//String frd = "2024-12-07 00:00:13";
		//String tod = "2024-12-08 15:47:13";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime frddateTime = LocalDateTime.parse(frd, formatter);
		LocalDateTime todateTime = LocalDateTime.parse(tod, formatter);

		System.out.println("frddateTime:"+frddateTime);
		System.out.println("todateTime:"+todateTime);

		List<Question> qList = this.questionRepository.findByCreateDateBetween(frddateTime, todateTime);

		assertEquals(10, qList.size());

	}

	@Test
	void testJpaUpdate() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());

		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);

	}

	@Test
	void testJpaDelete() {
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count());

	}
	//CRUD 테스트 끝.

	@Test
	void testJpaAnswerInsert() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());

		Question q = oq.get();
		Answer a = new Answer();
		a.setQuestion(q);
		a.setContent("네 자동으로 생성됩니다.");
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);

	}

	//답변 데이터 조회하기
	@Test
	void testJpaAnswerSelect() {
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());

		Answer a = oa.get();

		//answer 테이블의 fk가 연결된 값으로 question테이블의 정보를 조회
		assertEquals(2, a.getQuestion().getId());
		assertEquals("안녕하세요1?", a.getQuestion().getSubject());

	}

	//Question 에 fetch = FetchType.EAGER 사용하면, Transactional 사용 안해도됨.
	@Transactional
	@Test
	void testJpaQuestionAnswerSelect1() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());

	}

	@Test
	void testJpaMassiveInsert(){
		for(int i = 1; i <= 300; i++) {
			this.questionService.create(String.format("테스트 데이터입니다:[%03d]", i)
					, "내용무", null);
		}
	}

}
