package ru.yp.marketapp.adapters.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.service.base.OrdersQuery;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersQuery orders;

    public OrdersController(OrdersQuery orders) {
        this.orders = orders;
    }

    @GetMapping
    public Mono<String> orders(Model model) {
        return orders.findAll()
                .collectList()
                .map(orderViews -> {
                    model.addAttribute("orders", orderViews);
                    return "orders";
                });
    }

    @GetMapping("/{id}")
    public Mono<String> order(@PathVariable(name="id") long id,
                              @RequestParam(name="newOrder", required = false, defaultValue = "false") boolean newOrder,
                              Model model) {
        return orders.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("newOrder", newOrder);
                    return "order";
                });
    }

    // https://stackoverflow.com/questions/57853619/redirection-inside-reactive-spring-webflux-rest-controller
    public Mono<String> redirectToNewOrder(long orderId) {
        return Mono.just("redirect:/orders/" + orderId + "?newOrder=true");
    }
}
