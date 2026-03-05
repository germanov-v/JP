package ru.yp.marketapp.adapters.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.CartActionView;
import ru.yp.marketapp.adapters.web.view.ItemView;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final CatalogQuery catalog;
    private final CartUseCase cart;

    public ItemController(CatalogQuery catalog, CartUseCase cart) {
        this.catalog = catalog;
        this.cart = cart;
    }

    @GetMapping("/{id}")
    public String item(@PathVariable long id, Model model) {
        var item = catalog.findItem(id)
                .map(i -> new ItemView(i.id(), i.title(), i.description(), i.imgPath(), i.price(), cart.getCount(i.id())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/{id}")
    public String changeFromCard(@PathVariable long id,
                                 @RequestParam(name = "action") CartActionView action) {
        cart.changeCount(id, action);
        return "redirect:/items/" + id;
    }
}
