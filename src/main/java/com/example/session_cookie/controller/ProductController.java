package com.example.session_cookie.controller;

import com.example.session_cookie.dto.CartDto;
import com.example.session_cookie.dto.ProductDto;
import com.example.session_cookie.entity.Product;
import com.example.session_cookie.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping(value = "/shop")
@SessionAttributes("cart") // khai báo session có tên là 'cart'
public class ProductController {

    // Khởi tạo giá trị mặc định cho session cart
    @ModelAttribute("cart")
    public CartDto initCart(){
        return new CartDto();
    }

    @Autowired
    ProductService productService;

    @GetMapping
    public ModelAndView showListPage(Model model,@CookieValue(value = "idProduct",defaultValue = "-1") Long idProduct){
        if(idProduct != -1){
            model.addAttribute("historyProduct",productService.findById(idProduct).get());
        }
        return new ModelAndView("product/list","productList",productService.findAll());
    }

    @GetMapping("/detail/{id}")
    public ModelAndView showDetail(@PathVariable long id, HttpServletResponse response){
         // Tạo cookie
        Cookie cookie = new Cookie("idProduct", id + "");
//        cookie.setMaxAge(1 * 24 * 60 * 60); // Thời gian tồn tại trong 1 ngày
        cookie.setMaxAge(5);
//        cookie.setMaxAge(0); //  Hủy cookie
         cookie.setPath("/");
         response.addCookie(cookie);

        return new ModelAndView("product/detail","product",productService.findById(id).get());
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
//                            @ModelAttribute("cart") CartDto cart){
        @SessionAttribute("cart") CartDto cart){
        Optional<Product> productOption  = productService.findById(id);
        if(productOption.isPresent()){
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(productOption.get(),productDto);
            cart.addProduct(productDto);
        }
        return "redirect:/cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteInCart(@PathVariable Long id , @SessionAttribute("cart") CartDto cartDto){
        Optional<Product> productOptional = productService.findById(id);
        if(productOptional.isPresent()){
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(productOptional.get(),productDto);
            cartDto.deleteProduct(productDto);
        }
        return "redirect:/cart";
    }

}
