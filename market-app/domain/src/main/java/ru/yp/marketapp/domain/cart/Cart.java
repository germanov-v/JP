package ru.yp.marketapp.domain.cart;

import ru.yp.marketapp.domain.base.AggregateRoot;
import ru.yp.marketapp.domain.product.Product;

import java.time.OffsetDateTime;
import java.util.List;

public class Cart extends AggregateRoot {


   private List<CartItem> items;





   public List<CartItem> getItems() {
      return items;
   }

   public void setItems(List<CartItem> items) {
      this.items = items;
   }
}
