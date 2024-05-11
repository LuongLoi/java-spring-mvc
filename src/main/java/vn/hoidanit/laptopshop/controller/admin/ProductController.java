package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/admin/product")
    public String getAdminProductPage(Model model) {
        return "/admin/product/show_admin_product";
    }
}