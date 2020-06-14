package com.jtang.account.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtang.account.service.ITbUserService;
import com.jtang.account.query.TbUserQueryDTO;
import com.jtang.account.entity.TbUser;
import com.jtang.web.utils.PageUtils;
import com.jtang.web.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

/**
* 用户 前端控制器
* @author jtang
* @since 2020-06-13
* @version v1.0
*/
@Api(tags = "用户模块")
@RestController
@AllArgsConstructor
@RequestMapping("/account/tb-user")
public class TbUserController {

    @Autowired
    private ITbUserService service;

    @PostMapping
    @ApiOperation(value = "用户添加")
    public ResultUtils addCtFile(@Valid @RequestBody TbUser entity){
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
    @ApiOperation(value = "修改用户信息")
    public ResultUtils modifyTbUser(@Valid @RequestBody TbUser entity){
        service.getBaseMapper().updateById(entity);
        return ResultUtils.success;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据ID查询用户信息")
    public ResultUtils getCtFile(@Valid @PathVariable Long id){
        TbUser entity = service.getBaseMapper().selectById(id);
        return ResultUtils.build(entity);
    }

    @GetMapping("/list")
    @ApiOperation(value = "用户列表查询")
    public ResultUtils getList(@Valid TbUserQueryDTO queryDTO) {
        QueryWrapper<TbUser> queryWrapper =  new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<TbUser> page = new Page<>(queryDTO.getPageNum(),queryDTO.getPageSize());
        IPage<TbUser> iPage = service.getBaseMapper().selectPage(page,queryWrapper);
        return ResultUtils.build(PageUtils.converterToPagination(iPage));
    }

}