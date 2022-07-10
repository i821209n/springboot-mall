package com.cylin.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cylin.springbootmall.dto.BuyItem;
import com.cylin.springbootmall.dto.BuyItemsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 創建訂單
    @Transactional
    @Test
    public void createOrder_success() throws Exception {
        BuyItemsRequest buyItemsRequest = new BuyItemsRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(5);
        buyItemList.add(buyItem1);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(2);
        buyItemList.add(buyItem2);

        buyItemsRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(buyItemsRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.order_id", notNullValue()))
                .andExpect(jsonPath("$.user_id", equalTo(1)))
                .andExpect(jsonPath("$.total_amount", equalTo(750)))
                .andExpect(jsonPath("$.orderItemList", hasSize(2)))
                .andExpect(jsonPath("$.created_date", notNullValue()))
                .andExpect(jsonPath("$.last_modified_date", notNullValue()));
    }

    @Transactional
    @Test
    public void createOrder_illegalArgument_emptyBuyItemList() throws Exception {
        BuyItemsRequest buyItemsRequest = new BuyItemsRequest();
        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemsRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(buyItemsRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_userNotExist() throws Exception {
        BuyItemsRequest buyItemsRequest = new BuyItemsRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        buyItemsRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(buyItemsRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_productNotExist() throws Exception {
        BuyItemsRequest buyItemsRequest = new BuyItemsRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(100);
        buyItem1.setQuantity(1);
        buyItemList.add(buyItem1);

        buyItemsRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(buyItemsRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_stockNotEnough() throws Exception {
        BuyItemsRequest buyItemsRequest = new BuyItemsRequest();
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(10000);
        buyItemList.add(buyItem1);

        buyItemsRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(buyItemsRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 查詢訂單列表
    @Test
    public void getOrders() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[0].order_id", notNullValue()))
                .andExpect(jsonPath("$.result[0].user_id", equalTo(1)))
                .andExpect(jsonPath("$.result[0].total_amount", equalTo(100000)))
                .andExpect(jsonPath("$.result[0].orderItemList", hasSize(1)))
                .andExpect(jsonPath("$.result[0].created_date", notNullValue()))
                .andExpect(jsonPath("$.result[0].last_modified_date", notNullValue()))
                .andExpect(jsonPath("$.result[1].order_id", notNullValue()))
                .andExpect(jsonPath("$.result[1].user_id", equalTo(1)))
                .andExpect(jsonPath("$.result[1].total_amount", equalTo(500690)))
                .andExpect(jsonPath("$.result[1].orderItemList", hasSize(3)))
                .andExpect(jsonPath("$.result[1].created_date", notNullValue()))
                .andExpect(jsonPath("$.result[1].last_modified_date", notNullValue()));
    }

    @Test
    public void getOrders_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1)
                .param("limit", "2")
                .param("offset", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.result", hasSize(0)));
    }

    @Test
    public void getOrders_userHasNoOrder() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 2);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.result", hasSize(0)));
    }

    @Test
    public void getOrders_userNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 100);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("$.limit", notNullValue()))
//                .andExpect(jsonPath("$.offset", notNullValue()))
//                .andExpect(jsonPath("$.total", notNullValue()))
//                .andExpect(jsonPath("$.result", hasSize(0)));
    }
}