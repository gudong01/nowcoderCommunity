package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTest {
    @Autowired
    SensitiveFilter sensitiveFilter;

    @Test
    public void test(){
        String text = "这里可以赌博,可以读博，这里可以★开★★票★，这里可以★嫖★娼★，这里可以**吸***毒*";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

    }
    @Test
    public void isSymbol(){
        System.out.println(sensitiveFilter.isSymbol('s'));
    }
}
