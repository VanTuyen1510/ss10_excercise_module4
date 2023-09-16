package com.example.session_cookie.dto;

import com.example.session_cookie.entity.Product;

import java.util.HashMap;
import java.util.Map;

public class CartDto {
    private Map<ProductDto, Integer> productMap = new HashMap<>();

    public CartDto() {
    }

    public Map<ProductDto, Integer> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<ProductDto, Integer> productMap) {
        this.productMap = productMap;
    }

    public Map.Entry<ProductDto, Integer> selectItemInCart(ProductDto productDto){
        for(Map.Entry<ProductDto,Integer> entry :productMap.entrySet()){
            if(entry.getKey().getId().equals(productDto.getId())){
                return entry;
            }
        }
        return null;
    }


    public void addProduct(ProductDto productDto) {
//        HashMap containsKey() dùng để kiểm tra một Key có tồn tại trong HashMap hay không. Nếu không tồn tại nó sẽ trả về false, ngược lại true.
        if (productMap.containsKey(productDto)){
            // update value + 1
            Integer currentValue = productMap.get(productDto);
//            productMap.put(productDto,currentValue + 1);
            productMap.replace(productDto,currentValue + 1);
        }else {
            productMap.put(productDto,1); //  lần đầu được thêm vào
        }
    }


    public Float countTotalPayment(){
        float payment = 0;
        for (Map.Entry<ProductDto,Integer> entry: productMap.entrySet()){
            payment += entry.getKey().getPrice() * entry.getValue();
        }
        return payment;
    }

    public void deleteProduct(ProductDto productDto){
       Map.Entry<ProductDto,Integer> entry = selectItemInCart(productDto);
       productMap.remove(entry.getKey());
    }


}