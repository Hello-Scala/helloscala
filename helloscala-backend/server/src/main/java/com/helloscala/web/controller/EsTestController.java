package com.helloscala.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class EsTestController {
//
//    private final ApiArticleService apiArticleService;
//
//    private final EasyesMapper easyesMapper;
//
//    private final DataEventPublisherService dataEventPublisherService;
//
//
//    @GetMapping("createIndex")
//    public void createIndex(){
//        easyesMapper.createIndex();
//    }
//
//
//    @GetMapping("search")
//    public ResponseResult test(String keyword){
//       return apiArticleService.searchArticle(keyword);
//    }
//
//
//    @DeleteMapping("delete/{id}")
//    public void deleteData(@PathVariable(value = "id") Long id){
//        easyesMapper.deleteById(id);
//    }
//
//    @PutMapping("update")
//    public void update(@RequestBody ArticleElastic articleElastic){
//        easyesMapper.updateById(articleElastic);
//    }
//
//    @PostMapping ("add")
//    public String add(@RequestBody ArticleElastic articleElastic){
//        easyesMapper.insert(articleElastic);
//        dataEventPublisherService.publishData(DataEventEnum.ES_ADD,articleElastic);
//        return "success";
//    }
}
