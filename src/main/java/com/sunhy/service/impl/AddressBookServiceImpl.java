package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.AddressBook;
import com.sunhy.mapper.AddressBookMapper;
import com.sunhy.service.IAddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {
}
