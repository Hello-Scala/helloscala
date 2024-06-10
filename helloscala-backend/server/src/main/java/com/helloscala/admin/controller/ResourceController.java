package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.Resource;
import com.helloscala.common.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// todo name
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


    @OperationLogger(value = "Add resource")
    @SaCheckPermission("system:resource:add")
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public ResponseResult addResource(@RequestBody Resource resource) {
        return resourceService.addResource(resource);
    }


    @OperationLogger(value = "Update resource")
    @SaCheckPermission("system:resource:update")
    @RequestMapping(value="/update", method=RequestMethod.PUT)
    public ResponseResult updateBResource(
            @RequestBody Resource bResource) {
        return resourceService.updateResource(bResource);
    }


    @OperationLogger(value = "Delete resource")
    @SaCheckPermission("system:resource:delete")
    @RequestMapping(value="/delete", method=RequestMethod.DELETE)
    public ResponseResult deleteBResourceByIds(@RequestBody List<Long> ids) {
        return resourceService.deleteResourceByIds(ids);
    }
}
