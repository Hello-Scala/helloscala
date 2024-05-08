package com.helloscala.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.Resource;
import com.helloscala.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/system/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;


    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ResponseResult selectResourceList(@RequestParam(name = "type", required = false) String type) {
        return resourceService.selectResourceList(type);
    }


    @RequestMapping(value="/info/{id}", method = RequestMethod.GET)
    public ResponseResult selectResourceById(@PathVariable(value = "id") Integer id) {
        return resourceService.selectResourceById(id);
    }


    @SaCheckPermission("system:resource:add")
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public ResponseResult addBResource(@RequestBody Resource bResource) {
        return resourceService.addResource(bResource);
    }


    @SaCheckPermission("system:resource:update")
    @RequestMapping(value="/update", method=RequestMethod.PUT)
    public ResponseResult updateBResource(
            @RequestBody Resource bResource) {
        return resourceService.updateResource(bResource);
    }


    @SaCheckPermission("system:resource:delete")
    @RequestMapping(value="/delete", method=RequestMethod.DELETE)
    public ResponseResult deleteBResourceByIds(@RequestBody List<Long> ids) {
        return resourceService.deleteResourceByIds(ids);
    }
}
