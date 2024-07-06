package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.entity.Resource;
import com.helloscala.common.service.ResourceService;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
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
    public Response<Page<Resource>> selectResourceList(@RequestParam(name = "type", required = false) String type) {
        Page<Resource> resourcePage = resourceService.selectResourceList(type);
        return ResponseHelper.ok(resourcePage);
    }


    @RequestMapping(value="/info/{id}", method = RequestMethod.GET)
    public Response<Resource> selectResourceById(@PathVariable(value = "id") Integer id) {
        Resource resource = resourceService.selectResourceById(id);
        return ResponseHelper.ok(resource);
    }


    @OperationLogger(value = "Add resource")
    @SaCheckPermission("system:resource:add")
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public EmptyResponse addResource(@RequestBody Resource resource) {
        resourceService.addResource(resource);
        return ResponseHelper.ok();
    }


    @OperationLogger(value = "Update resource")
    @SaCheckPermission("system:resource:update")
    @RequestMapping(value="/update", method=RequestMethod.PUT)
    public EmptyResponse updateBResource(
            @RequestBody Resource bResource) {
        resourceService.updateResource(bResource);
        return ResponseHelper.ok();
    }


    @OperationLogger(value = "Delete resource")
    @SaCheckPermission("system:resource:delete")
    @RequestMapping(value="/delete", method=RequestMethod.DELETE)
    public EmptyResponse deleteBResourceByIds(@RequestBody List<Long> ids) {
        resourceService.deleteResourceByIds(ids);
        return ResponseHelper.ok();
    }
}
