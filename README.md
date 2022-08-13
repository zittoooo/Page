# Page
java spring crud

https://velog.io/@cheesecookie/series/%EC%9A%B0%EB%8B%B9%ED%83%95%ED%83%95

### 목차
1. [스프링의 장점](#스프링의-장점)


#### 스프링의 장점

✳ 선언적 트랜잭션 관리 \
메서드에 @Transactional 애너테이션을 설정하면 스프링이 트랜잭션을 관리한다.

✳️ 스프링 시큐리티 \
스프링 시큐리티는 자바 애플리케이션을 안전하게 만들기 위해 필요한 사용자 인증(authentication)과 권한 부여(authorization) 기능을 제공한다.
@Secured 애너테이션을 설정해서 권한 요구 사항을 해결 할 수 있다.

✳️ JMX \
Java Management Extension (자바 관리 확장) 지원을 사용하면 JMX 기술을 덧붙일 수 있다.
클래스에 @ManagedResource를 설정하면 클래스의 인스턴스를 MBean서버에 등록할 수 있고, 메서드에 @ManagedOperation을 설정하면 @ManagedResource가 설정한 클래스의 메서드를 JMX 연산으로 노출시킬 수 있다.

✳️ JMS \
Java Message Service (자바 메세지 서비스)를 사용하면 JMS 제공자에게 메시지를 받거나 보낼 수 있다. JmsTemplate 클래스는 메시지를 보내거나 동기적으로 메시지를 받을 때 리소스를 생성하고 제거하는 처리를 해주므로 JMS의 사용을 간단하게 해준다.

✳️ 캐시 \
스프링의 캐시 추상화를 사용하면 일관성 있게 캐시를 사용할 수 있다.
@Cachable 애너테이션
