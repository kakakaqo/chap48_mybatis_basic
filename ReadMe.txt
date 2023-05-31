[Spring AOP]

 - AOP 이론 설명
 - AOP XML 설정관련 프로젝트

1. 목적 
 - Xml 설정을 통하여 AOP를 적용할 수 있다.
 
2. 핵심 
  1) XML 환경설정 파일을 통한 Advice Bean, Aspect를 적용할 수 있다.
  2) 어드바이스에 실행될 코드를 작성할 수 있다.
 
 
[진행단계]

1. chap36_before_aop 프로젝트 복사해서 이름 변경
 -> chap37_aop_basic
 
2. pom.xml 디펜던시(라이브러리) 추가
	<!-- AspectJ -->
	<dependency>
		<groupId>org.aspectj</groupId>
		<artifactId>aspectjrt</artifactId>
		<version>${aspectj-version}</version>
	</dependency>	
	<dependency>
	    <groupId>org.aspectj</groupId>
	    <artifactId>aspectjweaver</artifactId>
	    <version>${aspectj-version}</version>
	    <scope>runtime</scope>
	</dependency>
	<dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjtools</artifactId>
        <version>${aspectj-version}</version>
    </dependency>	


3. com.javalab.board.common 패키지에 추가 클래스 생성(어드바이스)
 - BeforeAdvice 클래스 생성(메소드 실행전 로그 남기는 어드바이스)

4. service layer에서 BoardServiceImpl 클래스에서
   LogAdvice 관련 사항 모두 주석처리
 
4. root-context.xml에 다음 설정[주의할점] src/main/resources
  폴더에 있는 root-context.xml을 수정해야 함.(WEB-INF폴더 아님)
  
 1) 데이터베이스 properties
  - location="classpath:config/database.properties"
  
 2) <aop> xmlns에 aop 체크 설정 추가
  - <aop:config>
  - <aop:pointcut expression>
  - <aop:aspect
 3) 위에서 표현식에 대한 설명은 인터넷서 검색(image) "포인트컷 표현식"설명
 
5. before 어드바이스 단위 테스트

 1) BoardServiceTest 클래스로 테스트
  - testSelectBoardList 테스트
  - testGetBoardById 테스트(데이터베이스에 있는 게시물 번호)
  
 2) 모든 메소드에서 포인트컷을 한정한다. get메소드로
  - root-context.xml에 포인터 추가(get메소드만 대상이 되도록)
    getPointcut 포인트컷 추가하고 애스펙트에서 pointcut-ref="getPointcut"으로
    
 3) 단위테스트
  - testSelectBoardList 메소드로 테스트 -> 어드바이스 적용 안됨.
  - testGetBoardById 메소드 테스트 -> 어드바이스 적용됨.
  - testInsertBoard 적용안됨. 오로직 get으로 시작하는 메소드만 적용됨.

 4) 테스트후 AOP 개념도로 다시 한번 설명

6. After 어드바이스 단위 테스트
 1) AfterAdvice 어드바이스 클래스 추가
 
 2) root-context.xml <!-- 어드바이스 빈 등록 -->
	<bean id="after" class="com.javalab.board.common.AfterAdvice"></bean>

 3) <aop:config> 설정
  - <aop:aspect ref="after">
    [주의] AfterAdvice 에는 finallyLog() 메소드가 있음 !!!!!!
  - method="finallyLog"로 바꿔서 테스트할것
  
7. around 어드바이스
 1) around 어드바이스 클래스 생성
 
 2) Around Advice Bean registration
 	<!-- 3. AOP AroundAvice 설정 -->
	<!-- 어드바이스 빈 등록 -->
	<bean id="around" class="com.javalab.board.common.AroundAdvice"></bean>

 3) 단위테스트	
	
8. AfterReturning Advice
  
 2) AfterReturningAdvice 어드바이스 클래스 생성
 	<!-- 어드바이스 빈 등록 -->
	<bean id="afterReturning" class="com.javalab.board.common.AfterReturningAdvice"></bean>	
	<!-- 애스펙트 -->  
	<aop:aspect ref="afterReturning">
		<aop:after-returning method="afterLog" pointcut-ref="getPointcut"
			returning="returnObj" />
	</aop:aspect>
	
 3) 단위테스트 get메소드 테스트
   @Test //@Ignore
    public void testGetBoardById() { 
  

9. AfterThrowingAdvice
 1) BoardDao 클래스 복사해서 BoardDao2 생성
  - BoardService 와 Impl 복사해서 각각 2로 백업해둘것.
  
 2) BoardDao의 insertBoard 메소드 수정(예외 발생하도록)
  - 메소드 시그너처에 throws Exception 추가
  - 자체 try ~ catch 주석처리
  - 쿼리문 수정
 
 3) BoardService 와 Impl에 다음 코드 추가
  - int insertBoard(BoardVo vo) throws Exception;
 
 4) 컨트롤러에서 오류나는 것은   throws Exception 추가

 5) 루트 컨텍스트에 다음 추가
  	<!-- 5. AOP After Throwing Avice 설정 -->
	<!-- 어드바이스 빈 등록 -->
	<bean id="afterThrowing" class="com.javalab.board.common.AfterThrowingAdvice"></bean>
 
 6) 애스펙트 추가
		<aop:aspect ref="afterThrowing">
			<aop:after-throwing method="exceptionLog" pointcut-ref="allPointcut"
				throwing="exceptObj" />
		</aop:aspect>
		
 7)	단위테스트 하는 insertBoard 메소드 수정
  - public int insertBoard(BoardVo vo) throws Exception{ 

 8) 컨트롤러에서 오류나는 것은 
  	 
10. 테스트 끝났으면 Dao Service 원복

============= 다음 부분은 확인후 진행 ==================

5. applicationContext.xml에서 수정(모두 test/BoardServiceClient 자바실행)
 5.1. 자세한 설명이 되어있는 내용 실습
 5.2. getPointcut 실습
 5.3. after 실습 
 5.4. before Advice 실습
 5.5. after-throwing, aop:after 같이 실습
  - BoardServiceImpl insertBoard 메소드에 다음 코드 추가
  -if(vo.getSeq() == 0){
		throw new IllegalArgumentException("0번 글은 등록할 수 없습니다.");
	}	
 5.6. around 실습
  -	BoardServiceImpl insertBoard 메소드에서  vo.getSeq() 삭제
  - 

6. joinPoint
 6.1. LogAdvice 어드바이스에 새로운    printLog(JoinPoint jp)메소드 생성
	// 매개변수로 JoinPoint(대소문자 구분)
	// org.aspectj.lang.JoinPoint;
	public void printLog(JoinPoint jp){
		System.out.println("[공통 로그] 비지니스 로직 수행전 동작 타겟 : " + jp.getTarget());
	}  
  
7. before JoinPoint와 바인딩 실습[MemberSerivceImpl, MemberVo로 테스트]
 
  7.1 데이터베이스 작업
   7.1.1 board 데이터베이스의 members 테이블에 role 컬럼 추가
   - ALTER SESSION SET "_ORACLE_SCRIPT"=true; 
   - alter table members add role varchar2(50);
   7.1.2.아래 데이터 변경
   - dream 1234 이정미 user
   - hong 1234 홍순남 admin 
   
 7.2 데이터베이스 관련 설정 변경 : JDBCUtil에서 데이터베이스(url, 계정)
 
 7.3. BoardVo 클래스 복사/생성
   - role 멤버 변수 추가 / 게터 / 세터
  
 7.4 BeforeAdvice 다음 메소드 추가
 	public void beforeLogJp(JoinPoint jp){
		String method = jp.getSignature().getName();
		Object[] args = jp.getArgs();
		System.out.println("[사전 처리] " + method + "() 메소드 Args 정보 : " + args[0].toString());
	}
 7.5 applicationContext.xml 다음 코드 추가
 	<!-- Before Advice JoinPoint 실습 -->
 	<bean id="log" class="com.javalab.spring.common.BeforeAdvice"></bean>
	<aop:config>
		<aop:pointcut id="allPointcut" expression="execution(* com.javalab.spring..*Impl.*(..))" />
		<aop:pointcut id="getPointcut" expression="execution(* com.javalab.spring..*Impl.get*(..))" />
		
		<aop:aspect ref="log">
			<aop:before method="beforeLogJp" pointcut-ref="allPointcut" />
		</aop:aspect>
	</aop:config>

  
 
 7.6. test / MemberServiceClient에서 테스트  	
		MemberVo vo = new MemberVo();
		vo.setId("dream");
		vo.setPwd("1234");
	
 7.7. [결과] 이정미님 환영합니다.	
	
8. after-returning JoinPoint와 바인딩 실습

 8.1 AfterReturningAdvice 클래스에 다음 메소드 추가
 
  	public void afterLogJp(JoinPoint jp, Object returnObj){
		String method = jp.getSignature().getName();
		
		if(returnObj instanceof MemberVo) {
			MemberVo member = (MemberVo) returnObj;
			if(member.getRole().equals("admin")) {
				System.out.println("관리자 " + member.getName() + " 님 환영합니다.");
			}else {
				System.out.println("일반사용자 " + member.getName() + " 님 환영합니다.");
			}
		}
		System.out.println("[사후 차리] " + method + "() 메소드 리턴값 : " 
							+ returnObj.toString());
	}
  
  8.2. MemberServiceClient 다음 코드 수정
  
  		// 3. 회원 로그 체크를 위한 값설정 객체 생성
		MemberVo vo = new MemberVo();
		vo.setId("hong");	//dream - 일반사용자, hong - 관리자
		vo.setPwd("1234");
		
		// 4. 회원 로그 체크 요청
		MemberVo member = userService.getMember(vo); // DB에 회원정보 확인
		
		// 5. 회원정보가 있으면
		/*
		if(member != null){
			System.out.println(member.getName() + "님 환영합니다.");
		} else {
			System.out.println("로그인 실패");
		}
		*/
  
 8.3. applicationContext.xml에 다음 코드 추가
<!-- <bean id="afterReturning" class="com.javalab.spring.common.AfterReturningAdvice"></bean>
	
	<aop:config>
		<aop:pointcut  id="getPointcut" expression="execution(* com.javalab.spring..*Impl.get*(..))"/>
		
		<aop:aspect ref="afterReturning">
			<aop:after-returning method="afterLogJp" pointcut-ref="getPointcut" 
								 returning="returnObj"/>
		</aop:aspect>
	</aop:config> -->
	
9. After Throwing Advice JoinPoint, 	 

 1) MemberServiceImpl 클래스의 getMember() 내용 변경[포인트컷]
 	@Override
	public MemberVo getMember(MemberVo vo) {
		if(vo.getId().equals("")) {
			throw new NullPointerException("ID가 비어서 Null Point Exception발생");
		}
		return memberDAO.getMember(vo);
	}
 
 2) AfterThrowingAdvice 에 다음 메소드 추가
	// exceptionLogJp Advice 메소드(인자로 JoinPoint, Exception)
	public void exceptionLogJp(JoinPoint jp, Exception exceptObj){
		String method = jp.getSignature().getName();
		
		System.out.println("[예외 처리] " + method + "() 메소드 수행중 발생된 예외 메시지 : " 
				+ exceptObj.getMessage());
	}
	    
 3) MemberServiceClient 에서 다음과 같이 코드 수정(id를 빈칸으로 세팅)
 
	// 3. 회원 로그 체크를 위한 값설정 객체 생성
	MemberVo vo = new MemberVo();
	vo.setId("");	//dream - 일반사용자, hong - 관리자
	vo.setPwd("1234");	    
  
 4)applicationContext.xml 다음 코드 추가
  	<bean id="afterThrowing" class="com.javalab.spring.common.AfterThrowingAdvice"></bean>
		
	<aop:config>
		<aop:pointcut expression="execution(* com.javalab.spring..*Impl.*(..))" id="allPointcut"/>
		
		<aop:aspect ref="afterThrowing">
			<aop:after-throwing method="exceptionLogJp" pointcut-ref="allPointcut" 
								throwing="exceptObj" />
		</aop:aspect>
	</aop:config>
  

10. AroundAdvice 에 ProceedingJoinPoint
 
 1) MemberServiceImpl의 getMember()의 예외 발생 구문 주석처리
 
 2) AroundAdvice 다음 메소드 추가 
 	// AroundAdvice + ProceedingJoinPoint 연결된 Advice 메소드
	public Object aroundLogPjP(ProceedingJoinPoint pjp) throws Throwable{

		String method = pjp.getSignature().getName();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		System.out.println("[" + method + " 핵심 메소드 수행전] 시간 : " + stopWatch.getTotalTimeMillis());
		
		// 인자로 주어진 ProceedingJoinPoint 통해서 핵심 비즈니스 메소드 호출 가능.
		// 이걸 해주지 않으면 타겟 메소드(핵심 메소드) 호출 불가함.
		Object obj = pjp.proceed();
		
		stopWatch.stop();
		
		System.out.println("[" + method + " 핵심 메소드 수행후] 걸린 시간 : " 
							+ stopWatch.getTotalTimeMillis() + "(ms)초");
		return obj;
	}
 3) applicationContext.xml에 다음 추가
   	<bean id="around" class="com.javalab.spring.common.AroundAdvice"></bean>
	
	<aop:config>
		<aop:pointcut expression="execution(* com.javalab.spring..*Impl.*(..))" id="allPointcut"/>
		<aop:aspect ref="around">
			<aop:around method="aroundLogPjP" pointcut-ref="allPointcut" />
		</aop:aspect>
	</aop:config>
	
  
  
  
  
  
  
  
  
  
  
  