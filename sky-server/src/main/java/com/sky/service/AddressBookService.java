package com.sky.service;

import com.sky.entity.AddressBook;
import java.util.List;

public interface AddressBookService {

    /**
     * 条件查询
     * @param addressBook 查询条件
     * @return 地址列表
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增
     * @param addressBook 地址信息对象
     */
    void save(AddressBook addressBook);

    /**
     * 根据id查询
     * @param id 地址id
     * @return 地址信息对象
     */
    AddressBook getById(Long id);

    /**
     * 根据id修改
     * @param addressBook 地址信息对象
     */
    void update(AddressBook addressBook);

    /**
     * 根据 用户id修改 是否默认地址
     * @param addressBook 地址信息对象
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id 地址id
     */
    void deleteById(Long id);

}
