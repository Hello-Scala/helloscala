package com.helloscala.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.request.BOCreateResourceRequest;
import com.helloscala.admin.controller.request.BOUpdateResourceRequest;
import com.helloscala.admin.controller.view.BOResourceView;
import com.helloscala.admin.service.BOResourceService;
import com.helloscala.common.annotation.OperationLogger;
import com.helloscala.common.web.response.EmptyResponse;
import com.helloscala.common.web.response.Response;
import com.helloscala.common.web.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

// todo name
@RestController
@RequestMapping(value = "/system/resource")
public class ResourceController {
    @Autowired
    private BOResourceService resourceService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Response<Page<BOResourceView>> listByPage(@RequestParam(name = "type", required = false) String type) {
        Page<BOResourceView> resourcePage = resourceService.listByPage(type);
        return ResponseHelper.ok(resourcePage);
    }


    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public Response<BOResourceView> get(@PathVariable(value = "id") String id) {
        BOResourceView resource = resourceService.get(id);
        return ResponseHelper.ok(resource);
    }


    @OperationLogger(value = "Add resource")
    @SaCheckPermission("system:resource:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public EmptyResponse create(@RequestBody BOCreateResourceRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        resourceService.create(userId, request);
        return ResponseHelper.ok();
    }


    @OperationLogger(value = "Update resource")
    @SaCheckPermission("system:resource:update")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public EmptyResponse update(@RequestBody BOUpdateResourceRequest request) {
        String userId = StpUtil.getLoginIdAsString();
        resourceService.update(userId, request);
        return ResponseHelper.ok();
    }


    @OperationLogger(value = "Delete resource")
    @SaCheckPermission("system:resource:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public EmptyResponse bulkDelete(@RequestBody List<String> ids) {
        String userId = StpUtil.getLoginIdAsString();
        resourceService.bulkDelete(userId, new HashSet<>(ids));
        return ResponseHelper.ok();
    }
}
