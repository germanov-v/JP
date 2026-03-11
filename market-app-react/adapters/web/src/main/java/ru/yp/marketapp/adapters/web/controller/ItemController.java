package ru.yp.marketapp.adapters.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.controller.base.CookieController;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.appplication.model.CartActionEnum;

@Controller
@RequestMapping("/items")
public class ItemController implements CookieController {

    private final CatalogQuery catalog;
    private final CartUseCase cart;

    public ItemController(CatalogQuery catalog, CartUseCase cart) {
        this.catalog = catalog;
        this.cart = cart;
    }

    @GetMapping("/{id}")
    public Mono<String> item(@PathVariable Long id,
                             @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
                             ServerHttpResponse response,
                             Model model) {
        return cart.getOrCreateCartId(cartIdCookie)
                .flatMap(cartId -> {
                    setCartCookie(response, cartId, cartIdCookie);

                    return catalog.findItem(id, cartId)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(item -> {
                                model.addAttribute("item", item);
                                return "item";
                            });
                });
    }

    @PostMapping("/{id}")
    public Mono<String> changeFromCard(@PathVariable long id,
                                       @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
                                       ServerHttpResponse response,
                                       @RequestParam(name = "action") CartActionEnum action) {
        return cart.getOrCreateCartId(cartIdCookie)
                .flatMap(cartId -> {
                    setCartCookie(response, cartId, cartIdCookie);
                    return cart.changeCount(cartId, id, action)
                            .thenReturn("redirect:/items/" + id);
                });
    }
}
