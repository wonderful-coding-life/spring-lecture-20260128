package com.example.demo.tool;

import com.example.demo.repository.ProductOrderRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderTool {
    @Autowired
    private ProductOrderRepository repository;

    @Tool(description = "상품 주문 목록을 알려줍니다")
    public String getProductOrders() {
        var productOrders = repository.findAll();
        String result = "주문 목록은 다음과 같아요\n";
        for (var productOrder : productOrders) {
            result += "주문번호: " + productOrder.getOrderNumber();
            result += ", 상품이름: " + productOrder.getProductName();
            result += ", 배송주소: " + productOrder.getShippingAddress();
            result += ", 배송상태: " + productOrder.getShippingStatus();
            result += "\n";
        }
        return result;
    }

    @Tool(description = "상품 주문을 취소한다")
    public String cancelProductOrder(@ToolParam(description = "주문번호") String orderNumber) {
        var productOrder = repository.findByOrderNumber(orderNumber);
        if (productOrder != null) {
            if (productOrder.getShippingStatus().equals("배송중")) {
                return "배송중인 상품은 취소할 수 없습니다.";
            } else {
                repository.delete(productOrder);
                return "주문이 취소되었습니다.";
            }
        } else {
            return "없는 주문 번호입니다.";
        }
    }
}
