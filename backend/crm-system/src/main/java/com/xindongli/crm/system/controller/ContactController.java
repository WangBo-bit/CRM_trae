package com.xindongli.crm.system.controller;

import com.xindongli.crm.common.core.result.Result;
import com.xindongli.crm.system.dto.request.ContactCreateRequest;
import com.xindongli.crm.system.dto.request.ContactQueryRequest;
import com.xindongli.crm.system.dto.request.ContactUpdateRequest;
import com.xindongli.crm.system.dto.response.ContactResponse;
import com.xindongli.crm.system.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 联系人管理控制器
 * 
 * @author 芯动力科技
 */
@Tag(name = "联系人管理", description = "联系人管理相关接口")
@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@Validated
public class ContactController {

    private final ContactService contactService;

    /**
     * 创建联系人
     */
    @Operation(summary = "创建联系人")
    @PostMapping
    public Result<Long> createContact(@Valid @RequestBody ContactCreateRequest request) {
        Long contactId = contactService.createContact(request);
        return Result.success("创建成功", contactId);
    }

    /**
     * 更新联系人
     */
    @Operation(summary = "更新联系人")
    @PutMapping("/{id}")
    public Result<Void> updateContact(
            @Parameter(description = "联系人ID") @PathVariable Long id,
            @Valid @RequestBody ContactUpdateRequest request) {
        request.setId(id);
        contactService.updateContact(request);
        return Result.success("更新成功");
    }

    /**
     * 删除联系人
     */
    @Operation(summary = "删除联系人")
    @DeleteMapping("/{id}")
    public Result<Void> deleteContact(@Parameter(description = "联系人ID") @PathVariable Long id) {
        contactService.deleteContact(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除联系人
     */
    @Operation(summary = "批量删除联系人")
    @DeleteMapping
    public Result<Void> deleteContacts(@RequestBody List<Long> ids) {
        contactService.deleteContacts(ids);
        return Result.success("删除成功");
    }

    /**
     * 获取联系人详情
     */
    @Operation(summary = "获取联系人详情")
    @GetMapping("/{id}")
    public Result<ContactResponse> getContactById(@Parameter(description = "联系人ID") @PathVariable Long id) {
        ContactResponse response = contactService.getContactById(id);
        return Result.success(response);
    }

    /**
     * 查询联系人列表
     */
    @Operation(summary = "查询联系人列表")
    @GetMapping
    public Result<List<ContactResponse>> getContactList(@Valid ContactQueryRequest request) {
        List<ContactResponse> list = contactService.getContactList(request);
        return Result.success(list);
    }

    /**
     * 根据客户ID查询联系人列表
     */
    @Operation(summary = "根据客户ID查询联系人列表")
    @GetMapping("/customer/{customerId}")
    public Result<List<ContactResponse>> getContactsByCustomerId(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<ContactResponse> list = contactService.getContactsByCustomerId(customerId);
        return Result.success(list);
    }

    /**
     * 设置主要联系人
     */
    @Operation(summary = "设置主要联系人")
    @PostMapping("/{id}/actions/set-primary")
    public Result<Void> setPrimaryContact(@Parameter(description = "联系人ID") @PathVariable Long id) {
        contactService.setPrimaryContact(id);
        return Result.success("设置成功");
    }

}
