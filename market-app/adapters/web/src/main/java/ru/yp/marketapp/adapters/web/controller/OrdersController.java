package ru.yp.marketapp.adapters.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yp.marketapp.adapters.web.service.base.OrdersQuery;
import ru.yp.marketapp.adapters.web.view.OrderView;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersQuery orders;

    public OrdersController(OrdersQuery orders) {
        this.orders = orders;
    }

    @GetMapping
    public String orders(Model model) {
        model.addAttribute("orders", orders.findAll());
        return "orders";
    }

    @GetMapping("/{id}")
    public String order(@PathVariable long id,
                        @RequestParam(required = false, defaultValue = "false") boolean newOrder,
                        Model model) {
        OrderView order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("order", order);
        model.addAttribute("newOrder", newOrder);
        return "order";
    }


    public String redirectToNewOrder(long orderId, RedirectAttributes ra) {
        ra.addAttribute("newOrder", true);
        return "redirect:/orders/" + orderId;
    }
}
