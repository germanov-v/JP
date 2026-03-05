package ru.yp.marketapp.adapters.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.CartActionView;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.adapters.web.view.PagingView;
import ru.yp.marketapp.adapters.web.view.SortView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private final CatalogQuery catalog;
    private final CartUseCase cart;

    public ItemsController(CatalogQuery catalog, CartUseCase cart) {
        this.catalog = catalog;
        this.cart = cart;
    }

    @GetMapping
    public String items(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "sort", required = false, defaultValue = "NO") SortView sort,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            Model model
    ) {
        var page = catalog.findItems(search, sort, pageNumber, pageSize);

        // добавить ли count из корзины или CatalogQuery как времянку?
        var enriched = page.items().stream()
                .map(i -> new ItemView(i.id(), i.title(), i.description(), i.imgPath(), i.price(), cart.getCount(i.id())))
                .toList();

        //  ЛистЛистАйтимВью
        model.addAttribute("items", toRows(enriched, 3));
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", new PagingView(page.pageNumber(), page.pageSize(), page.hasPrev(), page.hasNext()));
        return "items";
    }

    @PostMapping
    public String changeFromList(
            @RequestParam long id,
            @RequestParam CartActionView action,


            @RequestParam(name="", required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "NO") SortView sort,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,

            RedirectAttributes ra
    ) {
        cart.changeCount(id, action);

        ra.addAttribute("search", search);
        ra.addAttribute("sort", sort);
        ra.addAttribute("pageSize", pageSize);
        ra.addAttribute("pageNumber", pageNumber);
        return "redirect:/items";
    }

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