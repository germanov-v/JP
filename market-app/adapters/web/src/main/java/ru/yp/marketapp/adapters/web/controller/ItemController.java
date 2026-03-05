package ru.yp.marketapp.adapters.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.appplication.model.CartActionEnum;

@Controller
@RequestMapping("/items")
public class ItemController {

    private static final String CART_COOKIE = "cartId";

    private final CatalogQuery catalog;
    private final CartUseCase cart;

    public ItemController(CatalogQuery catalog, CartUseCase cart) {
        this.catalog = catalog;
        this.cart = cart;
    }

    @GetMapping("/{id}")
    public String item(@PathVariable long id,
                       @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
                       HttpServletResponse response,
                       Model model) {
       // todo: setCartCookie

        var item = catalog.findItem(id)
                .map(i -> new ItemView(i.id(), i.title(), i.description(), i.imgPath(), i.price(),
                        cart.getCount(cartIdCookie, i.id())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/{id}")
    public String changeFromCard(@PathVariable long id,
                                 @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
                                 @RequestParam(name = "action") CartActionEnum action) {

        // todo: setCartCookie
        cart.changeCount(cartIdCookie, id, action);
        return "redirect:/items/" + id;
    }

    private void setCartCookie(HttpServletResponse response, long cartId) {

    }
}
