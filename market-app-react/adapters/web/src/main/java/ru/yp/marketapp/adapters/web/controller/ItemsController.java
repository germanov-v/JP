package ru.yp.marketapp.adapters.web.controller;


import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.controller.base.CookieController;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.adapters.web.view.PagingView;
import ru.yp.marketapp.appplication.model.CartActionEnum;
import ru.yp.marketapp.appplication.model.SortEnum;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemsController implements CookieController {

    private final CatalogQuery catalog;
    private final CartUseCase cart;

    public ItemsController(CatalogQuery catalog, CartUseCase cart) {
        this.catalog = catalog;
        this.cart = cart;
    }

    @GetMapping
    public Mono<String> items(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "sort", required = false, defaultValue = "NO") SortEnum sort,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
            ServerHttpResponse response,
            Model model
    ) {
        return cart.getOrCreateCartId(cartIdCookie)
                .flatMap(cartId -> {
                    setCartCookie(response, cartId, cartIdCookie);

                    return catalog.findItems(search, sort, pageNumber, pageSize, cartId)
                            .map(page -> {
                                model.addAttribute("items", toRows(page.items(), 3));
                                model.addAttribute("search", search);
                                model.addAttribute("sort", sort);
                                model.addAttribute("paging",
                                        new PagingView(page.pageNumber(), page.pageSize(), page.hasPrev(), page.hasNext()));
                                return "items";
                            });
                });
    }

    @PostMapping
    public Mono<String> changeFromList(
            @RequestParam(name = "id") long id,
            @RequestParam(name = "action") CartActionEnum action,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "sort", required = false, defaultValue = "NO") SortEnum sort,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
            ServerHttpResponse response
    ) {
        return cart.getOrCreateCartId(cartIdCookie)
                .flatMap(cartId -> {
                    setCartCookie(response, cartId, cartIdCookie);
                    return cart.changeCount(cartId, id, action)
                            .thenReturn("redirect:/items?search=" + search
                                    + "&sort=" + sort
                                    + "&pageSize=" + pageSize
                                    + "&pageNumber=" + pageNumber);
                });
    }

    // TODO: пересмотреть, но вроде тут реактивщины и не нужно
    private static List<List<ItemView>> toRows(List<ItemView> items, int columns) {
        var rows = new ArrayList<List<ItemView>>();
        for (int i = 0; i < items.size(); i += columns) {
            var row = new ArrayList<ItemView>(columns);
            for (int j = 0; j < columns; j++) {
                int idx = i + j;
                row.add(idx < items.size() ? items.get(idx) : ItemView.emptyCell());
            }
            rows.add(row);
        }
        return rows;
    }
}