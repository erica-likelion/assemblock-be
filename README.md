## 코드 실행에서 db 에러가 나는 경우 ##

### Config 폴더 안에 TableCleaner.java 생성


```
package com.assemblock.assemblock_be.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TableCleaner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public TableCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // 외래키 제약조건 무시하고 삭제 (안전장치 해제)
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            jdbcTemplate.execute("DROP TABLE IF EXISTS user");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
            
            System.out.println("테이블이 삭제되었습니다.");
        } catch (Exception e) {
            System.out.println("삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
```



- 코드 입력 후 서버 실행


- -> "테이블이 삭제되었습니다." 멘트 확인 후 서버 종료  



- TableCleaner.java 파일 삭제 후 서버 재실행

---

## swagger
