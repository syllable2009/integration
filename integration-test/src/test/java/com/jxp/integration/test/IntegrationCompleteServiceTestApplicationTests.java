package com.jxp.integration.test;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jxp.event.publisher.EventPublisher;
import com.jxp.integration.test.api.WeChatController;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@WebMvcTest(WeChatController.class)
class IntegrationCompleteServiceTestApplicationTests {

    @Resource
    private EventPublisher eventPublisher;

//    @ExtendWith(MockitoExtension.class)
//    @Mock
    @MockBean
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        Mockito.when(rocketMQTemplate.getCharset()).thenReturn("abc");
        String charset = rocketMQTemplate.getCharset();
        Assert.notBlank(charset);
        Assert.equals("abc", charset);
        log.info("charset:{}",charset);
        EventPublisher ep1 = Mockito.mock(EventPublisher.class);
    }

    @Test
    void test1() throws Exception {
        List<Book> books = new ArrayList<>();
        Mockito.when(rocketMQTemplate.equals(Mockito.any())).thenReturn(true);

//        final MockHttpServletRequestBuilder requestBuilder =
//                new MockHttpServletRequestBuilder(HttpMethod.GET, new URI("/readinglist/yulaoba"));
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(view().name("readingList"))
//                .andExpect(model().attributeExists("books"))
//                .andExpect(model().attribute("books", is(empty())));
    }


    @Test
    void testSendMsg(){
        log.info("start publish->>>>>>>>>>>>>>>>>>");
        try {
            eventPublisher.publish("hello");
        } catch (Exception e) {
            log.error("err", e);
        }

//        eventPublisher.publish(new ApplicationEvent() {
//            @Override
//            public Object getSource() {
//                return "999";
//            }
//        });
    }
}
