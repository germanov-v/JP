package cart;

import base.AggregateRoot;
import product.Product;

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
