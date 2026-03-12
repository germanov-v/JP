package ru.yp.marketapp.adapters.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
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

//    @PostMapping
//    public Mono<String> changeFromList(
//            @RequestParam(name = "id") long id,
//            @RequestParam(name = "action") CartActionEnum action,
//            @RequestParam(name = "search", required = false, defaultValue = "") String search,
//            @RequestParam(name = "sort", required = false, defaultValue = "NO") SortEnum sort,
//            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
//            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
//            @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
//            ServerHttpResponse response
//    ) {
//        return cart.getOrCreateCartId(cartIdCookie)
//                .flatMap(cartId -> {
//                    setCartCookie(response, cartId, cartIdCookie);
//                    return cart.changeCount(cartId, id, action)
//                            .thenReturn("redirect:/items?search=" + search
//                                    + "&sort=" + sort
//                                    + "&pageSize=" + pageSize
//                                    + "&pageNumber=" + pageNumber);
//                });
//    }

    @PostMapping
    public Mono<String> changeFromList(
            @CookieValue(name = CART_COOKIE, required = false) Long cartIdCookie,
            ServerHttpResponse response,
            ServerWebExchange exchange
    ) {
        // https://habr.com/ru/companies/cft/articles/648821/
        return exchange.getFormData()
                .flatMap(formData -> {
                    String rawId = formData.getFirst("id");
                    String rawAction = formData.getFirst("action");
                    String search = formData.getFirst("search");
                    String rawSort = formData.getFirst("sort");
                    String rawPageSize = formData.getFirst("pageSize");
                    String rawPageNumber = formData.getFirst("pageNumber");

                    if (rawId == null || rawAction == null) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing form fields"));
                    }

                    long id;
                    CartActionEnum action;
                    SortEnum sort;
                    int pageSize;
                    int pageNumber;

                    try {
                        id = Long.parseLong(rawId);
                        action = CartActionEnum.valueOf(rawAction);
                        sort = rawSort == null ? SortEnum.NO : SortEnum.valueOf(rawSort);
                        pageSize = rawPageSize == null ? 10 : Integer.parseInt(rawPageSize);
                        pageNumber = rawPageNumber == null ? 1 : Integer.parseInt(rawPageNumber);
                    } catch (IllegalArgumentException ex) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid form fields"));
                    }

                    if (search == null) {
                        search = "";
                    }

                    final String finalSearch = search;
                    final SortEnum finalSort = sort;
                    final int finalPageSize = pageSize;
                    final int finalPageNumber = pageNumber;

                    return cart.getOrCreateCartId(cartIdCookie)
                            .flatMap(cartId -> {
                                setCartCookie(response, cartId, cartIdCookie);
                                return cart.changeCount(cartId, id, action)
                                        .thenReturn("redirect:/items?search=" + finalSearch
                                                + "&sort=" + finalSort
                                                + "&pageSize=" + finalPageSize
                                                + "&pageNumber=" + finalPageNumber);
                            });
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