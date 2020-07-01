package com.jtang.oauth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtang.business.query.PlatformRoleQueryDTO;
import com.jtang.common.model.auth.PlatformRole;
import com.jtang.oauth.service.IPlatformRoleService;
import com.jtang.web.utils.PageUtils;
import com.jtang.common.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

/**
* 角色 前端控制器
* @author jtang
* @since 2020-06-30
* @version v1.0
*/
@Api(tags = "角色模块")
@RestController
@AllArgsConstructor
@RequestMapping("/oauth/platform-role")
public class PlatformRoleController {

    @Autowired
    private IPlatformRoleService service;

    @PostMapping
    @ApiOperation(value = "角色添加")
    public ResultUtils addCtFile(@Valid @RequestBody PlatformRole entity){
        service.save(entity);
        return ResultUtils.success;
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "根据ID删除信息")
    public ResultUtils addCtFile(@Valid @PathVariable Long id){
        service.getBaseMapper().deleteById(id);
        return ResultUtils.success;
    }

    @PutMapping
    @ApiOperation(value = "修改角色信息")
    public ResultUtils modifyPlatformRole(@Valid @RequestBody PlatformRole entity){
        service.getBaseMapper().updateById(entity);
        return ResultUtils.success;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据ID查询角色信息")
    public ResultUtils getCtFile(@Valid @PathVariable Long id){
        PlatformRole entity = service.getBaseMapper().selectById(id);
        return ResultUtils.build(entity);
    }

    @GetMapping("/list")
    @ApiOperation(value = "角色列表查询")
    public ResultUtils getList(@Valid PlatformRoleQueryDTO queryDTO) {
        QueryWrapper<PlatformRole> queryWrapper =  new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<PlatformRole> page = new Page<>(queryDTO.getPageNum(),queryDTO.getPageSize());
        IPage<PlatformRole> iPage = service.getBaseMapper().selectPage(page,queryWrapper);
        return ResultUtils.build(PageUtils.converterToPagination(iPage));
    }

}