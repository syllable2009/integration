package com.jxp.integration.test.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-10-20 10:52
 */
@Slf4j
@RestController
@RequestMapping("/api/ms")
public class MiaoshaController {


    @GetMapping("/{goodsId}/getUrl")
    public ResponseEntity<String> getGoodUrl(@PathVariable("goodsId") Long seckillGoodsId) {
        //goodsId表示是什么商品，然后根据该商品的数据库依次获得尚未被秒杀的每个商品的唯一ID，然后根据商品的唯一ID来生成唯一的秒杀URL
        //seckillGoodsId为某个商品的唯一id
        // 其中这一步可以省，之间将goodsId表示的传递给exportSeckillUrl也可以完成，类似SPU和SKU的关系
        // 注意这里最终是从秒杀商品表中取值，而非商品表
        //        seckillGoodService.getById("");
        //        String xx = md5("");
        return ResponseEntity.ok("返回md5之后的带链接对象");
    }

    // 具体执行秒杀操作的接口
    @GetMapping("/{goodsId}/{md5}/seckillGood")
    public ResponseEntity<Boolean> seckillGood(@PathVariable("goodsId") Long seckillGoodsId) {
        //        md5校验
        //        是否秒杀成功过
        //        秒杀商品并异步生成订单通知等
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
